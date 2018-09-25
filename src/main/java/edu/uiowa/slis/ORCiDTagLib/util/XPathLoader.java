package edu.uiowa.slis.ORCiDTagLib.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XPathLoader implements Runnable {
    static Logger logger = Logger.getLogger(XPathLoader.class);
    static Properties prop_file = PropertyLoader.loadProperties("loader");
    static Connection localConn = null;

    static Vector<Document> documentVector = new Vector<Document>();
    static Vector<Thread> threadVector = new Vector<Thread>();
    static boolean running = true;

    public static void main(String[] args) throws Exception {
	PropertyConfigurator.configure(args[0]);
	logger.setLevel(Level.INFO);

	localConn = getConnection();

	PreparedStatement stmt = localConn.prepareStatement("truncate orcid_dump.profile cascade");
	stmt.execute();
	stmt.close();
	localConn.commit();

	Runtime runtime = Runtime.getRuntime();
	logger.info("Available processors: " + runtime.availableProcessors());

	for (int i = 0; i < runtime.availableProcessors() - 1; i++) {
	    logger.info("initiating thread " + i + "...");
	    Thread thread = new Thread(new XPathLoader());
	    thread.start();
	    threadVector.add(thread);
	}

	processTarFile("/Volumes/SSD/ORCID/ORCID_public_data_file_2014.tar");
	running = false;

	for (Thread thread : threadVector) {
	    thread.join();
	}
    }

    static Connection getConnection() throws ClassNotFoundException, SQLException {
	Connection local = null;
	Properties props = new Properties();
	props.setProperty("user", "eichmann");
	props.setProperty("password", "translational");
	// props.setProperty("sslfactory",
	// "org.postgresql.ssl.NonValidatingFactory");
	// props.setProperty("ssl", "true");
	Class.forName("org.postgresql.Driver");
	local = DriverManager.getConnection("jdbc:postgresql://localhost/loki", props);
	local.setAutoCommit(false);
	return local;
    }

    static void processTarFile(String path) throws Exception {
	File input = new File(path);
	InputStream is = new FileInputStream(input);
	CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream("gz", is);

	ArchiveInputStream tin = new ArchiveStreamFactory().createArchiveInputStream("tar", in);
	TarArchiveEntry entry = (TarArchiveEntry) tin.getNextEntry();

	while (entry != null) {
	    if (!entry.isDirectory() && !entry.getName().endsWith(".json")) {
		while (documentVector.size() > 1000) {
		    Thread.sleep(1000);
		}

		logger.info("entry: " + entry.getName());
		logger.debug("\tmodification time: " + entry.getModTime());
		logger.debug("\tsize: " + entry.getSize());
		try {
		    SAXReader reader = new SAXReader(false);
		    Document document = reader.read(new WontCloseBufferedInputStream(tin));
		    documentVector.add(document);
		} catch (Exception e) {
		    logger.error("Error parsing document: ", e);
		}
	    }

	    entry = (TarArchiveEntry) tin.getNextEntry();
	}
    }

    int id = 0;
    String uri = null;
    String path = null;
    String host = null;
    String locale = null;
    String method = null;
    String completion = null;
    String submission = null;
    String modified = null;
    boolean claimed = false;
    String source = null;
    String deactivationDate = null;
    boolean verifiedEmail = false;
    boolean verifiedPrimaryEmail = false;
    Connection conn = null;

    public XPathLoader() throws Exception {
	conn = getConnection();
    }

    public void run() {
	Document document = null;

	while (running) {
	    try {
		while ((document = documentVector.remove(0)) != null) {
		    try {
			processProfile(document);
		    } catch (SQLException e) {
			logger.error("error processing document:", e);
		    }
		}
	    } catch (Exception e) {
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException e1) {
		}
	    }
	}
    }

    void processProfile(Document document) throws SQLException {
	NameSpaceEradicator.removeAllNamespaces(document);
	Element root = document.getRootElement();
	logger.debug("document root: " + root.getName() + "\t" + root.asXML());
	Element profile = (Element) root.selectSingleNode("orcid-profile");
	logger.debug("profile: " + profile.getName());

	uri = root.selectSingleNode("orcid-profile/orcid-identifier/uri").getText();
	logger.debug("\turi: " + uri);
	path = root.selectSingleNode("orcid-profile/orcid-identifier/path").getText();
	logger.info("path: " + path);
	host = root.selectSingleNode("orcid-profile/orcid-identifier/host").getText();
	logger.debug("\thost: " + host);

	locale = root.selectSingleNode("orcid-profile/orcid-preferences/locale").getText();
	logger.debug("\tlocale: " + locale);

	method = getElementString(root, "orcid-profile/orcid-history/creation-method");
	logger.debug("\tmethod: " + method);
	completion = getElementString(root, "orcid-profile/orcid-history/completion-date");
	logger.debug("\tcompletion: " + completion);
	submission = getElementString(root, "orcid-profile/orcid-history/submission-date");
	logger.debug("\tsubmission: " + submission);
	modified = getElementString(root, "orcid-profile/orcid-history/last-modified-date");
	logger.debug("\tmodified: " + modified);
	claimed = Boolean.parseBoolean(root.selectSingleNode("orcid-profile/orcid-history/claimed").getText());
	logger.debug("\tclaimed: " + claimed);
	source = getElementString(root, "orcid-profile/orcid-history/source");
	logger.debug("\tsource: " + source);
	deactivationDate = getElementString(root, "orcid-profile/orcid-history/deactivtaion-date");
	logger.debug("\tdeactivationDate: " + deactivationDate);
	verifiedEmail = Boolean.parseBoolean(root.selectSingleNode("orcid-profile/orcid-history/verified-email").getText());
	logger.debug("\tverifiedEmail: " + verifiedEmail);
	verifiedPrimaryEmail = Boolean.parseBoolean(root.selectSingleNode("orcid-profile/orcid-history/verified-primary-email").getText());
	logger.debug("\tverifiedPrimaryEmail: " + verifiedPrimaryEmail);

	PreparedStatement stmt = conn.prepareStatement(
		"insert into orcid_dump.profile(uri,path,host,locale,method,completion,submission,modified,claimed) values (?,?,?,?,?,?,?,?,?)",
		Statement.RETURN_GENERATED_KEYS);
	stmt.setString(1, uri);
	stmt.setString(2, path);
	stmt.setString(3, host);
	stmt.setString(4, locale);
	stmt.setString(5, method);
	// if (completion == null)
	// stmt.setNull(6, Types.TIMESTAMP);
	// else
	// stmt.setTimestamp(6, new Timestamp(Date.parse(completion)));
	// if (completion == null)
	// stmt.setNull(7, Types.TIMESTAMP);
	// else
	// stmt.setTimestamp(7, new Timestamp(Date.parse(submission)));
	// if (completion == null)
	// stmt.setNull(8, Types.TIMESTAMP);
	// else
	// stmt.setTimestamp(8, new Timestamp(Date.parse(modified)));
	stmt.setString(6, completion);
	stmt.setString(7, submission);
	stmt.setString(8, modified);
	stmt.setBoolean(9, claimed);
	stmt.execute();
	ResultSet rs = stmt.getGeneratedKeys();
	while (rs.next()) {
	    id = rs.getInt(1);
	    logger.debug("id: " + id);
	}
	stmt.close();

	processBio((Element) root.selectSingleNode("orcid-profile/orcid-bio"));
	processAffiliations((Element) root.selectSingleNode("orcid-profile/orcid-activities/affiliations"));
	processWorks((Element) root.selectSingleNode("orcid-profile/orcid-activities/orcid-works"));
	processFundingList((Element) root.selectSingleNode("orcid-profile/orcid-activities/funding-list"));

	conn.commit();
    }

    @SuppressWarnings("unchecked")
    void processBio(Element bio) throws SQLException {
	if (bio == null)
	    return;

	String givenNames = null;
	String familyName = null;
	String creditName = null;
	String biography = null;
	String country = null;

	givenNames = getElementString(bio, "personal-details/given-names");
	logger.info("\tgivenNames: " + givenNames);
	familyName = getElementString(bio, "personal-details/family-name");
	logger.info("\tfamilyName: " + familyName);
	creditName = getElementString(bio, "personal-details/credit-name");
	logger.debug("\tcreditName: " + creditName);

	biography = getElementString(bio, "biography");
	logger.debug("\tbiography: " + biography);

	country = getElementString(bio, "contact-details/address/country");
	logger.debug("\tcountry: " + country);

	PreparedStatement stmt = conn.prepareStatement("insert into orcid_dump.bio values(?,?,?,?,?,?)");
	stmt.setInt(1, id);
	stmt.setString(2, givenNames);
	stmt.setString(3, familyName);
	stmt.setString(4, creditName);
	stmt.setString(5, biography);
	stmt.setString(6, country);
	stmt.execute();
	stmt.close();

	int seqnum = 1;
	List<Node> otherNames = bio.selectNodes("personal-details/other-names/other-name");
	for (Node node : otherNames) {
	    String otherName = node.getText();
	    logger.debug("\totherName: " + otherName);

	    PreparedStatement substmt = conn.prepareStatement("insert into orcid_dump.other_name values(?,?,?)");
	    substmt.setInt(1, id);
	    substmt.setInt(2, seqnum++);
	    substmt.setString(3, otherName);
	    substmt.execute();
	    substmt.close();
	}

	seqnum = 1;
	List<Node> urls = bio.selectNodes("researcher-urls/researcher-url");
	for (Node node : urls) {
	    String name = getElementString((Element) node, "url-name");
	    logger.debug("\tname: " + name);
	    String url = getElementString((Element) node, "url");
	    logger.debug("\t\turl: " + url);

	    PreparedStatement substmt = conn.prepareStatement("insert into orcid_dump.url values(?,?,?,?)");
	    substmt.setInt(1, id);
	    substmt.setInt(2, seqnum++);
	    substmt.setString(3, name);
	    substmt.setString(4, url);
	    substmt.execute();
	    substmt.close();
	}

	seqnum = 1;
	List<Node> emails = bio.selectNodes("contact-details/email");
	for (Node node : emails) {
	    boolean primary = getAttributeBoolean((Element) node, "primary");
	    boolean current = getAttributeBoolean((Element) node, "current");
	    boolean verified = getAttributeBoolean((Element) node, "verified");
	    String source = getAttributeString((Element) node, "source");
	    String sourceClientID = getAttributeString((Element) node, "source-client-id");
	    String email = node.getText();
	    logger.debug("\temail: " + email);
	    logger.debug("\t\tprimary: " + primary);
	    logger.debug("\t\tcurrent: " + current);
	    logger.debug("\t\tverified: " + verified);
	    logger.info("\t\tsource: " + source);
	    logger.info("\t\tsourceClientID: " + sourceClientID);

	    PreparedStatement substmt = conn.prepareStatement("insert into orcid_dump.email values(?,?,?,?,?,?)");
	    substmt.setInt(1, id);
	    substmt.setInt(2, seqnum++);
	    substmt.setString(3, email);
	    substmt.setBoolean(4, primary);
	    substmt.setBoolean(5, current);
	    substmt.setBoolean(6, verified);
	    substmt.execute();
	    substmt.close();
	}

	seqnum = 1;
	List<Node> keywords = bio.selectNodes("keywords/keyword");
	for (Node node : keywords) {
	    String keyword = node.getText();
	    logger.debug("\tkeyword: " + keyword);

	    PreparedStatement substmt = conn.prepareStatement("insert into orcid_dump.keyword values(?,?,?)");
	    substmt.setInt(1, id);
	    substmt.setInt(2, seqnum++);
	    substmt.setString(3, keyword);
	    substmt.execute();
	    substmt.close();
	}

	seqnum = 1;
	List<Node> externals = bio.selectNodes("external-identifiers/external-identifier");
	for (Node node : externals) {
	    String name = getElementString((Element) node, "external-id-common-name");
	    logger.debug("\t\tname: " + name);
	    String reference = getElementString((Element) node, "external-id-reference");
	    logger.debug("\t\treference: " + reference);
	    String url = getElementString((Element) node, "external-id-url");
	    logger.debug("\t\turl: " + url);
	    String source = getElementString((Element) node, "source");
	    logger.debug("\t\tsource: " + source);

	    PreparedStatement substmt = conn.prepareStatement("insert into orcid_dump.external_id values(?,?,?,?,?,?,?,?)");
	    substmt.setInt(1, id);
	    substmt.setInt(2, seqnum++);
	    substmt.setString(3, null); // TODO fix
	    substmt.setString(4, null);
	    substmt.setString(5, null);
	    substmt.setString(6, name);
	    substmt.setString(7, reference);
	    substmt.setString(8, url);
	    substmt.execute();
	    substmt.close();
	}
    }

    @SuppressWarnings("unchecked")
    void processAffiliations(Element affiliations) throws SQLException {
	if (affiliations == null)
	    return;
	int seqnum = 1;
	for (Element affiliation : (List<Element>) affiliations.selectNodes("affiliation")) {
	    logger.debug("\taffiliation: " + affiliation.asXML());
	    String type = getElementString(affiliation, "type");
	    logger.debug("\t\ttype: " + type);
	    String departmentName = getElementString(affiliation, "department-name");
	    logger.debug("\t\tdepartmentName: " + departmentName);
	    String title = getElementString(affiliation, "role-title");
	    logger.debug("\t\ttitle: " + title);
	    Date startDate = getElementDate(affiliation, "start-date");
	    logger.debug("\t\tstartDate: " + startDate);
	    Date endDate = getElementDate(affiliation, "end-date");
	    logger.debug("\t\tendDate: " + endDate);
	    String organization = getElementString(affiliation, "organization/name");
	    logger.debug("\t\torganization: " + organization);
	    String orgCity = getElementString(affiliation, "organization/address/city");
	    logger.debug("\t\torgCity: " + orgCity);
	    String orgRegion = getElementString(affiliation, "organization/address/region");
	    logger.debug("\t\torgRegion: " + orgRegion);
	    String orgCountry = getElementString(affiliation, "organization/address/country");
	    logger.debug("\t\torgCountry: " + orgCountry);
	    String disambigID = getElementString(affiliation, "organization/disambiguated-organization/disambiguated-organization-identifier");
	    logger.debug("\t\tdisambigID: " + disambigID);
	    String disambigSource = getElementString(affiliation, "organization/disambiguated-organization/disambiguation-source");
	    logger.debug("\t\tdisambigSource: " + disambigSource);

	    PreparedStatement stmt = conn.prepareStatement("insert into orcid_dump.affiliation values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    stmt.setInt(1, id);
	    stmt.setInt(2, seqnum++);
	    stmt.setString(3, type);
	    stmt.setString(4, departmentName);
	    stmt.setString(5, title);
	    stmt.setDate(6, startDate);
	    stmt.setDate(7, endDate);
	    stmt.setString(8, organization);
	    stmt.setString(9, orgCity);
	    stmt.setString(10, orgRegion);
	    stmt.setString(11, orgCountry);
	    stmt.setString(12, disambigID);
	    stmt.setString(13, disambigSource);
	    stmt.execute();
	    stmt.close();
	}
    }

    @SuppressWarnings("unchecked")
    void processWorks(Element works) throws SQLException {
	if (works == null)
	    return;
	int seqnum = 1;
	List<Element> workList = works.selectNodes("orcid-work");
	for (Element work : workList) {
	    logger.debug("\torcid-work: " + work.asXML());
	    String putCode = getAttributeString(work, "put-code");
	    logger.debug("\twork put-code: " + putCode);
	    String title = getElementString(work, "work-title/title");
	    logger.debug("\t\ttitle: " + title);
	    String subtitle = getElementString(work, "work-title/subtitle");
	    logger.debug("\t\tsubtitle: " + subtitle);
	    String translationTitle = null;
	    String translationLanguage = null;
	    Element translation = (Element) work.selectSingleNode("work-title/translated-title");
	    if (translation != null) {
		translationTitle = translation.getText();
		translationLanguage = getAttributeString(translation, "language-code");
	    }
	    logger.debug("\t\ttranslation title: " + translationTitle);
	    logger.debug("\t\t\tlanguage: " + translationLanguage);

	    String journal = getElementString(work, "journal-title");
	    logger.debug("\t\tjournal: " + journal);
	    String description = getElementString(work, "short-description");
	    logger.debug("\t\tdescription: " + description);
	    String citation = getElementString(work, "work-citation/citation");
	    logger.debug("\t\tcitation: " + citation);
	    String citationType = getElementString(work, "work-citation/work-citation-type");
	    logger.debug("\t\tcitationType: " + citationType);
	    String type = getElementString(work, "work-type");
	    logger.debug("\t\ttype: " + type);
	    String publicationYear = getElementString(work, "publication-date/year");
	    logger.debug("\t\tpublicationYear: " + publicationYear);
	    String publicationMonth = getElementString(work, "publication-date/month");
	    logger.debug("\t\tpublicationMonth: " + publicationMonth);
	    String publicationDay = getElementString(work, "publication-date/day");
	    logger.debug("\t\tpublicationDay: " + publicationDay);

	    String url = getElementString(work, "url");
	    logger.debug("\t\twork url: " + url);

	    String language = getElementString(work, "language-code");
	    logger.debug("\t\twork language: " + language);
	    String country = getElementString(work, "country");
	    logger.debug("\t\twork country: " + country);

	    PreparedStatement stmt = conn.prepareStatement("insert into orcid_dump.work values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    stmt.setInt(1, id);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, putCode);
	    stmt.setString(4, title);
	    stmt.setString(5, subtitle);
	    stmt.setString(6, translationTitle);
	    stmt.setString(7, translationLanguage);
	    stmt.setString(8, journal);
	    stmt.setString(9, description);
	    stmt.setString(10, citation);
	    stmt.setString(11, citationType);
	    stmt.setString(12, type);
	    stmt.setString(13, publicationYear);
	    stmt.setString(14, publicationMonth);
	    stmt.setString(15, publicationDay);
	    stmt.setString(16, url);
	    stmt.setString(17, language);
	    stmt.setString(18, country);
	    stmt.execute();
	    stmt.close();

	    int worknum = 1;
	    List<Element> ids = work.selectNodes("work-external-identifiers/work-external-identifier");
	    for (Element node : ids) {
		String idType = getElementString(node, "work-external-identifier-type");
		logger.debug("\t\tidType: " + idType);
		String idID = getElementString(node, "work-external-identifier-id");
		logger.debug("\t\tidID: " + idID);

		PreparedStatement substmt = conn.prepareStatement("insert into orcid_dump.work_external_id values(?,?,?,?,?)");
		substmt.setInt(1, id);
		substmt.setInt(2, seqnum);
		substmt.setInt(3, worknum++);
		substmt.setString(4, idType);
		substmt.setString(5, idID);
		substmt.execute();
		substmt.close();
	    }

	    worknum = 1;
	    List<Element> contributors = work.selectNodes("work-contributors/contributor");
	    for (Element node : contributors) {
		String contributorName = getElementString(node, "credit-name");
		logger.debug("\t\tcontributorName: " + contributorName);

		PreparedStatement substmt = conn.prepareStatement("insert into orcid_dump.contributor values(?,?,?,?)");
		substmt.setInt(1, id);
		substmt.setInt(2, seqnum);
		substmt.setInt(3, worknum++);
		substmt.setString(4, contributorName);
		substmt.execute();
		substmt.close();
	    }

	    seqnum++;
	}
    }

    @SuppressWarnings("unchecked")
    void processFundingList(Element fundingList) throws SQLException {
	if (fundingList == null)
	    return;
	int seqnum = 1;
	for (Element funding : (List<Element>) fundingList.selectNodes("funding")) {
	    logger.debug("\tfunding: " + funding.asXML());
	    String type = getElementString(funding, "funding-type");
	    logger.debug("\t\ttype: " + type);
	    String orgType = getElementString(funding, "organization-defined-type");
	    logger.debug("\t\torgType: " + orgType);
	    String title = getElementString(funding, "funding-title/title");
	    logger.debug("\t\ttitle: " + title);
	    String translatedTitle = getElementString(funding, "funding-title/translated-title");
	    logger.debug("\t\ttranslatedTitle: " + translatedTitle);
	    String shortDescription = getElementString(funding, "short-description");
	    logger.debug("\t\tshortDescription: " + shortDescription);
	    String amount = getElementString(funding, "amount");
	    logger.debug("\t\tamount: " + amount);
	    String currency = amount == null ? null : getAttributeString((Element) funding.selectSingleNode("amount"), "currency-code");
	    logger.debug("\t\tcurrency: " + currency);
	    String url = getElementString(funding, "url");
	    logger.debug("\t\turl: " + url);
	    Date startDate = getElementDate(funding, "start-date");
	    logger.debug("\t\tstartDate: " + startDate);
	    Date endDate = getElementDate(funding, "end-date");
	    logger.debug("\t\tendDate: " + endDate);
	    String organization = getElementString(funding, "organization/name");
	    logger.debug("\t\torganization: " + organization);
	    String orgCity = getElementString(funding, "organization/address/city");
	    logger.debug("\t\torgCity: " + orgCity);
	    String orgRegion = getElementString(funding, "organization/address/region");
	    logger.debug("\t\torgRegion: " + orgRegion);
	    String orgCountry = getElementString(funding, "organization/address/country");
	    logger.debug("\t\torgCountry: " + orgCountry);
	    String disambigID = getElementString(funding, "organization/disambiguated-organization/disambiguated-organization-identifier");
	    logger.debug("\t\tdisambigID: " + disambigID);
	    String disambigSource = getElementString(funding, "organization/disambiguated-organization/disambiguation-source");
	    logger.debug("\t\tdisambigSource: " + disambigSource);

	    PreparedStatement stmt = conn.prepareStatement("insert into orcid_dump.funding values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    stmt.setInt(1, id);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, type);
	    stmt.setString(4, orgType);
	    stmt.setString(5, title);
	    stmt.setString(6, translatedTitle);
	    stmt.setString(7, shortDescription);
	    stmt.setString(8, amount);
	    stmt.setString(9, currency);
	    stmt.setString(10, url);
	    stmt.setDate(11, startDate);
	    stmt.setDate(12, endDate);
	    stmt.setString(13, organization);
	    stmt.setString(14, orgCity);
	    stmt.setString(15, orgRegion);
	    stmt.setString(16, orgCountry);
	    stmt.setString(17, disambigID);
	    stmt.setString(18, disambigSource);
	    stmt.execute();
	    stmt.close();

	    int seqnum2 = 1;
	    for (Node node : (List<Node>) funding.selectNodes("funding-external-identifiers/funding-external-identifier")) {
		String exttype = getElementString((Element) node, "funding-external-identifier-type");
		logger.debug("\t\ttype: " + exttype);
		String extvalue = getElementString((Element) node, "funding-external-identifier-value");
		logger.debug("\t\tvalue: " + extvalue);
		String exturl = getElementString((Element) node, "funding-external-identifier-url");
		logger.debug("\t\turl: " + exturl);

		PreparedStatement substmt = conn.prepareStatement("insert into orcid_dump.funding_external_id values(?,?,?,?,?,?)");
		substmt.setInt(1, id);
		substmt.setInt(2, seqnum);
		substmt.setInt(3, seqnum2++);
		substmt.setString(4, exttype);
		substmt.setString(5, extvalue);
		substmt.setString(6, exturl);
		substmt.execute();
		substmt.close();
	    }

	    seqnum2 = 1;
	    for (Node node : (List<Node>) funding.selectNodes("funding-contributors/funding-contributor")) {
		String orcidID = getElementString((Element) node, "contributor-orcid");
		logger.debug("\t\torcidID: " + orcidID);
		String creditName = getElementString((Element) node, "credit-name");
		logger.debug("\t\tcreditName: " + creditName);
		String email = getElementString((Element) node, "contributor-email/email");
		logger.debug("\t\temail: " + email);
		String role = getElementString((Element) node, "funding-contributor-attributes/funding-contributor-role");
		logger.debug("\t\trole: " + role);

		PreparedStatement substmt = conn.prepareStatement("insert into orcid_dump.funding_contributor values(?,?,?,?,?,?,?)");
		substmt.setInt(1, id);
		substmt.setInt(2, seqnum);
		substmt.setInt(3, seqnum2++);
		substmt.setString(4, orcidID);
		substmt.setString(5, creditName);
		substmt.setString(6, email);
		substmt.setString(7, role);
		substmt.execute();
		substmt.close();
	    }

	    seqnum++;
	}
    }

    String getElementString(Element root, String xpath) {
	Element element = (Element) root.selectSingleNode(xpath);
	if (element == null)
	    return null;
	else
	    return element.getText().trim();
    }

    Date getElementDate(Element root, String xpath) {
	Element element = (Element) root.selectSingleNode(xpath);
	if (element == null || ((Node) element).selectSingleNode("year") == null)
	    return null;
	String yearString = ((Node) element).selectSingleNode("year").getText().trim();
	String monthString = getElementString(element, "month");
	String dayString = getElementString(element, "day");
	@SuppressWarnings("deprecation")
	Date newDate = new Date(Integer.parseInt(yearString) - 1900, (monthString == null ? 0 : Integer.parseInt(monthString) - 1),
		(dayString == null ? 1 : Integer.parseInt(dayString)));
	return newDate;
    }

    String getAttributeString(Element root, String label) {
	Attribute attribute = ((Element) root).attribute(label);
	if (attribute == null)
	    return null;
	else
	    return attribute.getText();
    }

    boolean getAttributeBoolean(Element root, String label) {
	Attribute attribute = ((Element) root).attribute(label);
	if (attribute == null)
	    return false;
	else
	    return Boolean.parseBoolean(attribute.getText());
    }

    static class WontCloseBufferedInputStream extends BufferedInputStream {
	public WontCloseBufferedInputStream(InputStream arg0) {
	    super(arg0);
	}

	public void close() {
	    // Do nothing.
	}

    }
}
