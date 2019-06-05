package edu.uiowa.slis.ORCiDTagLib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class Loader {
    static Logger logger = Logger.getLogger(Loader.class);
    static Properties prop_file = PropertyLoader.loadProperties("cd2h");
    static Connection localConn = null;

    public static void main(String[] args) throws FileNotFoundException, DocumentException, ClassNotFoundException, SQLException {
	PropertyConfigurator.configure(args[0]);
	localConn = getConnection();

	scan("/Volumes/Pegasus3/Corpora/ORCiD/data/v2.0-summaries");
    }

    static void scan(String rootDirectory) throws FileNotFoundException, DocumentException {
	File root = new File(rootDirectory);
	for (File directory : root.listFiles()) {
	    logger.info("directory: " + directory.getName());
	    for (File file : directory.listFiles()) {
//		logger.info("\tfile: " + file.getName());
		try {
		    SAXReader reader = new SAXReader(false);
		    Document document = reader.read(new FileInputStream(file));
		    NameSpaceEradicator.removeAllNamespaces(document);
		    String orcidID = document.getRootElement().selectSingleNode("orcid-identifier").selectSingleNode("path").getText();
		    logger.info("\torcidID: " + orcidID);
		    
		    PreparedStatement stmt = localConn.prepareStatement("insert into orcid.xml(orcid_id,raw) values(?,?::xml)");
		    stmt.setString(1, orcidID);
		    stmt.setString(2, document.asXML());
		    stmt.execute();
		    stmt.close();
		} catch (Exception e) {
		    logger.info("skipping...");
		}
	    }
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
