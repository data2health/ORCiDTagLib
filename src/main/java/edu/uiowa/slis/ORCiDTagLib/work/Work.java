package edu.uiowa.slis.ORCiDTagLib.work;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.ORCiDTagLib.profile.Profile;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;
import edu.uiowa.slis.ORCiDTagLib.Sequence;

@SuppressWarnings("serial")

public class Work extends ORCiDTagLibTagSupport {

	static Work currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Work.class);

	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();

	int ID = 0;
	int seqnum = 0;
	String putCode = null;
	String title = null;
	String subtitle = null;
	String translation = null;
	String translationLanguage = null;
	String journal = null;
	String description = null;
	String citation = null;
	String citationType = null;
	String type = null;
	String publicationYear = null;
	String publicationMonth = null;
	String publicationDay = null;
	String url = null;
	String language = null;
	String country = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			if (theProfile!= null)
				parentEntities.addElement(theProfile);

			if (theProfile == null) {
			} else {
				ID = theProfile.getID();
			}

			WorkIterator theWorkIterator = (WorkIterator)findAncestorWithClass(this, WorkIterator.class);

			if (theWorkIterator != null) {
				ID = theWorkIterator.getID();
				seqnum = theWorkIterator.getSeqnum();
			}

			if (theWorkIterator == null && theProfile == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new Work and to generate a new seqnum
				seqnum = Sequence.generateID();
				log.debug("generating new Work " + seqnum);
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a Work from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select put_code,title,subtitle,translation,translation_language,journal,description,citation,citation_type,type,publication_year,publication_month,publication_day,url,language,country from orcid_dump.work where id = ? and seqnum = ?");
				stmt.setInt(1,ID);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (putCode == null)
						putCode = rs.getString(1);
					if (title == null)
						title = rs.getString(2);
					if (subtitle == null)
						subtitle = rs.getString(3);
					if (translation == null)
						translation = rs.getString(4);
					if (translationLanguage == null)
						translationLanguage = rs.getString(5);
					if (journal == null)
						journal = rs.getString(6);
					if (description == null)
						description = rs.getString(7);
					if (citation == null)
						citation = rs.getString(8);
					if (citationType == null)
						citationType = rs.getString(9);
					if (type == null)
						type = rs.getString(10);
					if (publicationYear == null)
						publicationYear = rs.getString(11);
					if (publicationMonth == null)
						publicationMonth = rs.getString(12);
					if (publicationDay == null)
						publicationDay = rs.getString(13);
					if (url == null)
						url = rs.getString(14);
					if (language == null)
						language = rs.getString(15);
					if (country == null)
						country = rs.getString(16);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving seqnum " + seqnum);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update orcid_dump.work set put_code = ?, title = ?, subtitle = ?, translation = ?, translation_language = ?, journal = ?, description = ?, citation = ?, citation_type = ?, type = ?, publication_year = ?, publication_month = ?, publication_day = ?, url = ?, language = ?, country = ? where id = ? and seqnum = ?");
				stmt.setString(1,putCode);
				stmt.setString(2,title);
				stmt.setString(3,subtitle);
				stmt.setString(4,translation);
				stmt.setString(5,translationLanguage);
				stmt.setString(6,journal);
				stmt.setString(7,description);
				stmt.setString(8,citation);
				stmt.setString(9,citationType);
				stmt.setString(10,type);
				stmt.setString(11,publicationYear);
				stmt.setString(12,publicationMonth);
				stmt.setString(13,publicationDay);
				stmt.setString(14,url);
				stmt.setString(15,language);
				stmt.setString(16,country);
				stmt.setInt(17,ID);
				stmt.setInt(18,seqnum);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException {
		try {
			if (seqnum == 0) {
				seqnum = Sequence.generateID();
				log.debug("generating new Work " + seqnum);
			}

			if (putCode == null)
				putCode = "";
			if (title == null)
				title = "";
			if (subtitle == null)
				subtitle = "";
			if (translation == null)
				translation = "";
			if (translationLanguage == null)
				translationLanguage = "";
			if (journal == null)
				journal = "";
			if (description == null)
				description = "";
			if (citation == null)
				citation = "";
			if (citationType == null)
				citationType = "";
			if (type == null)
				type = "";
			if (publicationYear == null)
				publicationYear = "";
			if (publicationMonth == null)
				publicationMonth = "";
			if (publicationDay == null)
				publicationDay = "";
			if (url == null)
				url = "";
			if (language == null)
				language = "";
			if (country == null)
				country = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into orcid_dump.work(id,seqnum,put_code,title,subtitle,translation,translation_language,journal,description,citation,citation_type,type,publication_year,publication_month,publication_day,url,language,country) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setInt(2,seqnum);
			stmt.setString(3,putCode);
			stmt.setString(4,title);
			stmt.setString(5,subtitle);
			stmt.setString(6,translation);
			stmt.setString(7,translationLanguage);
			stmt.setString(8,journal);
			stmt.setString(9,description);
			stmt.setString(10,citation);
			stmt.setString(11,citationType);
			stmt.setString(12,type);
			stmt.setString(13,publicationYear);
			stmt.setString(14,publicationMonth);
			stmt.setString(15,publicationDay);
			stmt.setString(16,url);
			stmt.setString(17,language);
			stmt.setString(18,country);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
	}

	public int getID () {
		return ID;
	}

	public void setID (int ID) {
		this.ID = ID;
	}

	public int getActualID () {
		return ID;
	}

	public int getSeqnum () {
		return seqnum;
	}

	public void setSeqnum (int seqnum) {
		this.seqnum = seqnum;
	}

	public int getActualSeqnum () {
		return seqnum;
	}

	public String getPutCode () {
		if (commitNeeded)
			return "";
		else
			return putCode;
	}

	public void setPutCode (String putCode) {
		this.putCode = putCode;
		commitNeeded = true;
	}

	public String getActualPutCode () {
		return putCode;
	}

	public String getTitle () {
		if (commitNeeded)
			return "";
		else
			return title;
	}

	public void setTitle (String title) {
		this.title = title;
		commitNeeded = true;
	}

	public String getActualTitle () {
		return title;
	}

	public String getSubtitle () {
		if (commitNeeded)
			return "";
		else
			return subtitle;
	}

	public void setSubtitle (String subtitle) {
		this.subtitle = subtitle;
		commitNeeded = true;
	}

	public String getActualSubtitle () {
		return subtitle;
	}

	public String getTranslation () {
		if (commitNeeded)
			return "";
		else
			return translation;
	}

	public void setTranslation (String translation) {
		this.translation = translation;
		commitNeeded = true;
	}

	public String getActualTranslation () {
		return translation;
	}

	public String getTranslationLanguage () {
		if (commitNeeded)
			return "";
		else
			return translationLanguage;
	}

	public void setTranslationLanguage (String translationLanguage) {
		this.translationLanguage = translationLanguage;
		commitNeeded = true;
	}

	public String getActualTranslationLanguage () {
		return translationLanguage;
	}

	public String getJournal () {
		if (commitNeeded)
			return "";
		else
			return journal;
	}

	public void setJournal (String journal) {
		this.journal = journal;
		commitNeeded = true;
	}

	public String getActualJournal () {
		return journal;
	}

	public String getDescription () {
		if (commitNeeded)
			return "";
		else
			return description;
	}

	public void setDescription (String description) {
		this.description = description;
		commitNeeded = true;
	}

	public String getActualDescription () {
		return description;
	}

	public String getCitation () {
		if (commitNeeded)
			return "";
		else
			return citation;
	}

	public void setCitation (String citation) {
		this.citation = citation;
		commitNeeded = true;
	}

	public String getActualCitation () {
		return citation;
	}

	public String getCitationType () {
		if (commitNeeded)
			return "";
		else
			return citationType;
	}

	public void setCitationType (String citationType) {
		this.citationType = citationType;
		commitNeeded = true;
	}

	public String getActualCitationType () {
		return citationType;
	}

	public String getType () {
		if (commitNeeded)
			return "";
		else
			return type;
	}

	public void setType (String type) {
		this.type = type;
		commitNeeded = true;
	}

	public String getActualType () {
		return type;
	}

	public String getPublicationYear () {
		if (commitNeeded)
			return "";
		else
			return publicationYear;
	}

	public void setPublicationYear (String publicationYear) {
		this.publicationYear = publicationYear;
		commitNeeded = true;
	}

	public String getActualPublicationYear () {
		return publicationYear;
	}

	public String getPublicationMonth () {
		if (commitNeeded)
			return "";
		else
			return publicationMonth;
	}

	public void setPublicationMonth (String publicationMonth) {
		this.publicationMonth = publicationMonth;
		commitNeeded = true;
	}

	public String getActualPublicationMonth () {
		return publicationMonth;
	}

	public String getPublicationDay () {
		if (commitNeeded)
			return "";
		else
			return publicationDay;
	}

	public void setPublicationDay (String publicationDay) {
		this.publicationDay = publicationDay;
		commitNeeded = true;
	}

	public String getActualPublicationDay () {
		return publicationDay;
	}

	public String getUrl () {
		if (commitNeeded)
			return "";
		else
			return url;
	}

	public void setUrl (String url) {
		this.url = url;
		commitNeeded = true;
	}

	public String getActualUrl () {
		return url;
	}

	public String getLanguage () {
		if (commitNeeded)
			return "";
		else
			return language;
	}

	public void setLanguage (String language) {
		this.language = language;
		commitNeeded = true;
	}

	public String getActualLanguage () {
		return language;
	}

	public String getCountry () {
		if (commitNeeded)
			return "";
		else
			return country;
	}

	public void setCountry (String country) {
		this.country = country;
		commitNeeded = true;
	}

	public String getActualCountry () {
		return country;
	}

	public static int IDValue() throws JspException {
		try {
			return currentInstance.getID();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function IDValue()");
		}
	}

	public static int seqnumValue() throws JspException {
		try {
			return currentInstance.getSeqnum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function seqnumValue()");
		}
	}

	public static String putCodeValue() throws JspException {
		try {
			return currentInstance.getPutCode();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function putCodeValue()");
		}
	}

	public static String titleValue() throws JspException {
		try {
			return currentInstance.getTitle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function titleValue()");
		}
	}

	public static String subtitleValue() throws JspException {
		try {
			return currentInstance.getSubtitle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function subtitleValue()");
		}
	}

	public static String translationValue() throws JspException {
		try {
			return currentInstance.getTranslation();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function translationValue()");
		}
	}

	public static String translationLanguageValue() throws JspException {
		try {
			return currentInstance.getTranslationLanguage();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function translationLanguageValue()");
		}
	}

	public static String journalValue() throws JspException {
		try {
			return currentInstance.getJournal();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function journalValue()");
		}
	}

	public static String descriptionValue() throws JspException {
		try {
			return currentInstance.getDescription();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function descriptionValue()");
		}
	}

	public static String citationValue() throws JspException {
		try {
			return currentInstance.getCitation();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function citationValue()");
		}
	}

	public static String citationTypeValue() throws JspException {
		try {
			return currentInstance.getCitationType();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function citationTypeValue()");
		}
	}

	public static String typeValue() throws JspException {
		try {
			return currentInstance.getType();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function typeValue()");
		}
	}

	public static String publicationYearValue() throws JspException {
		try {
			return currentInstance.getPublicationYear();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function publicationYearValue()");
		}
	}

	public static String publicationMonthValue() throws JspException {
		try {
			return currentInstance.getPublicationMonth();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function publicationMonthValue()");
		}
	}

	public static String publicationDayValue() throws JspException {
		try {
			return currentInstance.getPublicationDay();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function publicationDayValue()");
		}
	}

	public static String urlValue() throws JspException {
		try {
			return currentInstance.getUrl();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function urlValue()");
		}
	}

	public static String languageValue() throws JspException {
		try {
			return currentInstance.getLanguage();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function languageValue()");
		}
	}

	public static String countryValue() throws JspException {
		try {
			return currentInstance.getCountry();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function countryValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		seqnum = 0;
		putCode = null;
		title = null;
		subtitle = null;
		translation = null;
		translationLanguage = null;
		journal = null;
		description = null;
		citation = null;
		citationType = null;
		type = null;
		publicationYear = null;
		publicationMonth = null;
		publicationDay = null;
		url = null;
		language = null;
		country = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ORCiDTagLibTagSupport>();

	}

}
