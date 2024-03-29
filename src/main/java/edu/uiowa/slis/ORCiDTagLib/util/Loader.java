package edu.uiowa.slis.ORCiDTagLib.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class Loader {
	static Logger logger = Logger.getLogger(Loader.class);
	static Properties prop_file = PropertyLoader.loadProperties("cd2h_neuromancer");
	static Connection localConn = null;
	static boolean initial = false;

	public static void main(String[] args) throws DocumentException, ClassNotFoundException, SQLException, IOException {
		PropertyConfigurator.configure(args[0]);
		localConn = getConnection();

		if (initial)
			scan("/Volumes/Pegasus3/Corpora/ORCiD/data/v2.0-summaries");
		else if (args.length > 1 && args[1].equals("-materialize")) {
			materialize();
		} else if (args.length > 1 && args[1].equals("-rematerialize")) {
			rematerialize();
		} else {
			update();
			rematerialize();
		}
	}

	static void materialize() throws SQLException {
		materialize("person", "id,orcid_id,trim(given_names),trim(family_name),trim(credit_name)");
		materialize("address", "*");
		materialize("biography", "*");
		materialize("education", "*");
		materialize("email", "*");
		materialize("employment", "*");
		materialize("external_identifier", "*");
		materialize("funding", "id,orcid_id,seqnum,title,translated_title,type,start_year,start_month,start_day,end_year,end_month,end_day,organization,city,region,country,org_id,id_source");
		materialize("funding_external_id", "*");
		materialize("history", "*");
		materialize("keyword", "*");
		materialize("other_name", "*");
		materialize("researcher_url", "*");
		materialize("work", "id,orcid_id,seqnum,title,type,pub_year,pub_month,pub_day");
		materialize("work_external_id", "*");
	}

	static void materialize(String table, String attributes) throws SQLException {
		PreparedStatement checkStmt = localConn.prepareStatement("select min(id), max(id) from orcid_staging.xml");
		ResultSet rs = checkStmt.executeQuery();
		while (rs.next()) {
			int min = rs.getInt(1);
			int max = rs.getInt(2);
			logger.info(table + " min: " + min / 1000000 + "\tmax: " + max / 1000000);
			for (int fence = min / 1000000; fence <= max / 1000000; fence++) {
				logger.info("\tfence: " + fence * 1000000 + " : " + (fence + 1) * 1000000);
				PreparedStatement stmt = localConn.prepareStatement("insert into orcid." + table + " select "
						+ attributes + " from orcid_staging.staging_" + table + " where id >= ? and id < ?");
				stmt.setInt(1, fence * 1000000);
				stmt.setInt(2, (fence + 1) * 1000000);
				int count = stmt.executeUpdate();
				stmt.close();
				logger.info("\tcount: " + count);
			}
		}
		checkStmt.close();
	}

	static void rematerialize() throws SQLException {
		logger.info("scanning for existing records...");
		PreparedStatement stmt = localConn.prepareStatement("delete from orcid.person where orcid_id in (select orcid_id from orcid_staging.queue)");
		int count = stmt.executeUpdate();
		stmt.close();
		logger.info("\tdeleted " + count + " existing records");
		count = 100000;

		rematerialize(count, "person", "id,orcid_id,given_names,family_name,credit_name");
		rematerialize(count, "address", "*");
		rematerialize(count, "biography", "*");
		rematerialize(count, "education", "*");
		rematerialize(count, "email", "*");
		rematerialize(count, "employment", "*");
		rematerialize(count, "external_identifier", "*");
		rematerialize(count, "funding", "id,orcid_id,seqnum,title,translated_title,type,start_year,start_month,start_day,end_year,end_month,end_day,organization,city,region,country,org_id,id_source");
		rematerialize(count, "funding_external_id", "*");
		rematerialize(count, "history", "*");
		rematerialize(count, "keyword", "*");
		rematerialize(count, "other_name", "*");
		rematerialize(count, "researcher_url", "*");
		rematerialize(count, "work", "id,orcid_id,seqnum,title,type,pub_year,pub_month,pub_day");
		rematerialize(count, "work_external_id", "*");

		logger.info("truncating queue...");
		stmt = localConn.prepareStatement("truncate orcid_staging.queue");
		count = stmt.executeUpdate();
		stmt.close();
	}

	static void rematerialize(int count, String table, String attributes) throws SQLException {
		// postmaster can die on really big XMLtable query results (memory leak, probably)
//		if (count < 200000)
			old_rematerialize(table, attributes);
//		else
//			new_rematerialize(count, table, attributes);
	}

	static void old_rematerialize(String table, String attributes) throws SQLException {
		logger.info("rematerializing " + table + "...");
		PreparedStatement stmt = localConn.prepareStatement("insert into orcid." + table + " select " + attributes
				+ " from orcid_staging.staging_" + table
				+ " where id in (select id from orcid_staging.xml natural join orcid_staging.queue)");
		int count = stmt.executeUpdate();
		stmt.close();
		logger.info("\tcount: " + count);
	}

	static void new_rematerialize(int count, String table, String attributes) throws SQLException {
		PreparedStatement checkStmt = localConn.prepareStatement(
				"select min(id), max(id) from orcid_staging.xml where exists (select * from orcid_staging.queue where xml.orcid_id = queue.orcid_id)");
		ResultSet rs = checkStmt.executeQuery();
		while (rs.next()) {
			int min = rs.getInt(1);
			int max = rs.getInt(2);
			logger.info(table + " min: " + min / count + "\tmax: " + max / count);
			for (int fence = min / count; fence <= max / count; fence++) {
				logger.info("\tfence: " + fence * count + " : " + (fence + 1) * count);
				PreparedStatement stmt = localConn.prepareStatement("insert into orcid." + table + " select "
						+ attributes + " from orcid_staging.staging_" + table
						+ " where id >= ? and id < ? and id in (select id from orcid_staging.xml where orcid_id in (select orcid_id from orcid_staging.queue))");
				stmt.setInt(1, fence * count);
				stmt.setInt(2, (fence + 1) * count);
				int rescount = stmt.executeUpdate();
				stmt.close();
				logger.info("\tcount: " + rescount);
			}
		}
		checkStmt.close();
	}

	static void update() throws IOException, SQLException {
		// read files from stdin
		BufferedReader IODesc = new BufferedReader(new InputStreamReader(System.in));
		String current = null;
		while ((current = IODesc.readLine()) != null) {
			logger.trace("log string: " + current);
			String path = current.replaceAll(".*Volumes", "/Volumes");
			String orcidID = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
			logger.debug("path: " + path + "\torcidID: " + orcidID);

			PreparedStatement stmt = localConn.prepareStatement("delete from orcid_staging.xml where orcid_id = ?");
			stmt.setString(1, orcidID);
			stmt.execute();
			stmt.close();

			loadFile(new File(path));

			stmt = localConn.prepareStatement("insert into orcid_staging.queue(orcid_id) values(?)");
			stmt.setString(1, orcidID);
			stmt.execute();
			stmt.close();
		}

		// differentially add to the "real" schema using the queue and the xmltable
		// views

		// truncate queue

	}

	static void scan(String rootDirectory) throws FileNotFoundException, DocumentException {
		File root = new File(rootDirectory);
		for (File directory : root.listFiles()) {
			logger.info("directory: " + directory.getName());
			for (File file : directory.listFiles()) {
				loadFile(file);
			}
		}
	}

	static void loadFile(File file) {
		// logger.info("\tfile: " + file.getName());
		try {
			SAXReader reader = new SAXReader(false);
			Document document = reader.read(new FileInputStream(file));
			NameSpaceEradicator.removeAllNamespaces(document);
			String orcidID = document.getRootElement().selectSingleNode("orcid-identifier").selectSingleNode("path").getText();
			logger.debug("\torcidID: " + orcidID);

			PreparedStatement stmt = localConn.prepareStatement("insert into orcid_staging.xml(orcid_id,raw) values(?,?::xml)");
			stmt.setString(1, orcidID);
			stmt.setString(2, document.asXML());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			logger.debug("skipping...");
		}
	}

	static Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection local = null;
		Properties props = new Properties();
		props.setProperty("user", prop_file.getProperty("jdbc.user"));
		props.setProperty("password", prop_file.getProperty("jdbc.password"));
		// props.setProperty("sslfactory",
		// "org.postgresql.ssl.NonValidatingFactory");
		// props.setProperty("ssl", "true");
		Class.forName("org.postgresql.Driver");
		local = DriverManager.getConnection(prop_file.getProperty("jdbc.url"), props);
//	local.setAutoCommit(false);
		return local;
	}

}
