package edu.uiowa.slis.ORCiDTagLib.funding;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.ORCiDTagLib.profile.Profile;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;
import edu.uiowa.slis.ORCiDTagLib.Sequence;

@SuppressWarnings("serial")

public class Funding extends ORCiDTagLibTagSupport {

	static Funding currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Funding.class);

	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();

	int ID = 0;
	int seqnum = 0;
	String type = null;
	String orgType = null;
	String title = null;
	String translatedTitle = null;
	String shortDescription = null;
	String amount = null;
	String currency = null;
	String url = null;
	Date startDate = null;
	Date endDate = null;
	String organization = null;
	String city = null;
	String region = null;
	String country = null;
	String disambiguationId = null;
	String disambiguationSource = null;

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

			FundingIterator theFundingIterator = (FundingIterator)findAncestorWithClass(this, FundingIterator.class);

			if (theFundingIterator != null) {
				ID = theFundingIterator.getID();
				seqnum = theFundingIterator.getSeqnum();
			}

			if (theFundingIterator == null && theProfile == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new Funding and to generate a new seqnum
				seqnum = Sequence.generateID();
				log.debug("generating new Funding " + seqnum);
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a Funding from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select type,org_type,title,translated_title,short_description,amount,currency,url,start_date,end_date,organization,city,region,country,disambiguation_id,disambiguation_source from orcid_dump.funding where id = ? and seqnum = ?");
				stmt.setInt(1,ID);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (type == null)
						type = rs.getString(1);
					if (orgType == null)
						orgType = rs.getString(2);
					if (title == null)
						title = rs.getString(3);
					if (translatedTitle == null)
						translatedTitle = rs.getString(4);
					if (shortDescription == null)
						shortDescription = rs.getString(5);
					if (amount == null)
						amount = rs.getString(6);
					if (currency == null)
						currency = rs.getString(7);
					if (url == null)
						url = rs.getString(8);
					if (startDate == null)
						startDate = rs.getDate(9);
					if (endDate == null)
						endDate = rs.getDate(10);
					if (organization == null)
						organization = rs.getString(11);
					if (city == null)
						city = rs.getString(12);
					if (region == null)
						region = rs.getString(13);
					if (country == null)
						country = rs.getString(14);
					if (disambiguationId == null)
						disambiguationId = rs.getString(15);
					if (disambiguationSource == null)
						disambiguationSource = rs.getString(16);
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
				PreparedStatement stmt = getConnection().prepareStatement("update orcid_dump.funding set type = ?, org_type = ?, title = ?, translated_title = ?, short_description = ?, amount = ?, currency = ?, url = ?, start_date = ?, end_date = ?, organization = ?, city = ?, region = ?, country = ?, disambiguation_id = ?, disambiguation_source = ? where id = ? and seqnum = ?");
				stmt.setString(1,type);
				stmt.setString(2,orgType);
				stmt.setString(3,title);
				stmt.setString(4,translatedTitle);
				stmt.setString(5,shortDescription);
				stmt.setString(6,amount);
				stmt.setString(7,currency);
				stmt.setString(8,url);
				stmt.setDate(9,startDate == null ? null : new java.sql.Date(startDate.getTime()));
				stmt.setDate(10,endDate == null ? null : new java.sql.Date(endDate.getTime()));
				stmt.setString(11,organization);
				stmt.setString(12,city);
				stmt.setString(13,region);
				stmt.setString(14,country);
				stmt.setString(15,disambiguationId);
				stmt.setString(16,disambiguationSource);
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
				log.debug("generating new Funding " + seqnum);
			}

			if (type == null)
				type = "";
			if (orgType == null)
				orgType = "";
			if (title == null)
				title = "";
			if (translatedTitle == null)
				translatedTitle = "";
			if (shortDescription == null)
				shortDescription = "";
			if (amount == null)
				amount = "";
			if (currency == null)
				currency = "";
			if (url == null)
				url = "";
			if (organization == null)
				organization = "";
			if (city == null)
				city = "";
			if (region == null)
				region = "";
			if (country == null)
				country = "";
			if (disambiguationId == null)
				disambiguationId = "";
			if (disambiguationSource == null)
				disambiguationSource = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into orcid_dump.funding(id,seqnum,type,org_type,title,translated_title,short_description,amount,currency,url,start_date,end_date,organization,city,region,country,disambiguation_id,disambiguation_source) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setInt(2,seqnum);
			stmt.setString(3,type);
			stmt.setString(4,orgType);
			stmt.setString(5,title);
			stmt.setString(6,translatedTitle);
			stmt.setString(7,shortDescription);
			stmt.setString(8,amount);
			stmt.setString(9,currency);
			stmt.setString(10,url);
			stmt.setDate(11,startDate == null ? null : new java.sql.Date(startDate.getTime()));
			stmt.setDate(12,endDate == null ? null : new java.sql.Date(endDate.getTime()));
			stmt.setString(13,organization);
			stmt.setString(14,city);
			stmt.setString(15,region);
			stmt.setString(16,country);
			stmt.setString(17,disambiguationId);
			stmt.setString(18,disambiguationSource);
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

	public String getOrgType () {
		if (commitNeeded)
			return "";
		else
			return orgType;
	}

	public void setOrgType (String orgType) {
		this.orgType = orgType;
		commitNeeded = true;
	}

	public String getActualOrgType () {
		return orgType;
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

	public String getTranslatedTitle () {
		if (commitNeeded)
			return "";
		else
			return translatedTitle;
	}

	public void setTranslatedTitle (String translatedTitle) {
		this.translatedTitle = translatedTitle;
		commitNeeded = true;
	}

	public String getActualTranslatedTitle () {
		return translatedTitle;
	}

	public String getShortDescription () {
		if (commitNeeded)
			return "";
		else
			return shortDescription;
	}

	public void setShortDescription (String shortDescription) {
		this.shortDescription = shortDescription;
		commitNeeded = true;
	}

	public String getActualShortDescription () {
		return shortDescription;
	}

	public String getAmount () {
		if (commitNeeded)
			return "";
		else
			return amount;
	}

	public void setAmount (String amount) {
		this.amount = amount;
		commitNeeded = true;
	}

	public String getActualAmount () {
		return amount;
	}

	public String getCurrency () {
		if (commitNeeded)
			return "";
		else
			return currency;
	}

	public void setCurrency (String currency) {
		this.currency = currency;
		commitNeeded = true;
	}

	public String getActualCurrency () {
		return currency;
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

	public Date getStartDate () {
		return startDate;
	}

	public void setStartDate (Date startDate) {
		this.startDate = startDate;
		commitNeeded = true;
	}

	public Date getActualStartDate () {
		return startDate;
	}

	public void setStartDateToNow ( ) {
		this.startDate = new java.util.Date();
		commitNeeded = true;
	}

	public Date getEndDate () {
		return endDate;
	}

	public void setEndDate (Date endDate) {
		this.endDate = endDate;
		commitNeeded = true;
	}

	public Date getActualEndDate () {
		return endDate;
	}

	public void setEndDateToNow ( ) {
		this.endDate = new java.util.Date();
		commitNeeded = true;
	}

	public String getOrganization () {
		if (commitNeeded)
			return "";
		else
			return organization;
	}

	public void setOrganization (String organization) {
		this.organization = organization;
		commitNeeded = true;
	}

	public String getActualOrganization () {
		return organization;
	}

	public String getCity () {
		if (commitNeeded)
			return "";
		else
			return city;
	}

	public void setCity (String city) {
		this.city = city;
		commitNeeded = true;
	}

	public String getActualCity () {
		return city;
	}

	public String getRegion () {
		if (commitNeeded)
			return "";
		else
			return region;
	}

	public void setRegion (String region) {
		this.region = region;
		commitNeeded = true;
	}

	public String getActualRegion () {
		return region;
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

	public String getDisambiguationId () {
		if (commitNeeded)
			return "";
		else
			return disambiguationId;
	}

	public void setDisambiguationId (String disambiguationId) {
		this.disambiguationId = disambiguationId;
		commitNeeded = true;
	}

	public String getActualDisambiguationId () {
		return disambiguationId;
	}

	public String getDisambiguationSource () {
		if (commitNeeded)
			return "";
		else
			return disambiguationSource;
	}

	public void setDisambiguationSource (String disambiguationSource) {
		this.disambiguationSource = disambiguationSource;
		commitNeeded = true;
	}

	public String getActualDisambiguationSource () {
		return disambiguationSource;
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

	public static String typeValue() throws JspException {
		try {
			return currentInstance.getType();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function typeValue()");
		}
	}

	public static String orgTypeValue() throws JspException {
		try {
			return currentInstance.getOrgType();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function orgTypeValue()");
		}
	}

	public static String titleValue() throws JspException {
		try {
			return currentInstance.getTitle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function titleValue()");
		}
	}

	public static String translatedTitleValue() throws JspException {
		try {
			return currentInstance.getTranslatedTitle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function translatedTitleValue()");
		}
	}

	public static String shortDescriptionValue() throws JspException {
		try {
			return currentInstance.getShortDescription();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function shortDescriptionValue()");
		}
	}

	public static String amountValue() throws JspException {
		try {
			return currentInstance.getAmount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function amountValue()");
		}
	}

	public static String currencyValue() throws JspException {
		try {
			return currentInstance.getCurrency();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function currencyValue()");
		}
	}

	public static String urlValue() throws JspException {
		try {
			return currentInstance.getUrl();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function urlValue()");
		}
	}

	public static Date startDateValue() throws JspException {
		try {
			return currentInstance.getStartDate();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function startDateValue()");
		}
	}

	public static Date endDateValue() throws JspException {
		try {
			return currentInstance.getEndDate();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function endDateValue()");
		}
	}

	public static String organizationValue() throws JspException {
		try {
			return currentInstance.getOrganization();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function organizationValue()");
		}
	}

	public static String cityValue() throws JspException {
		try {
			return currentInstance.getCity();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function cityValue()");
		}
	}

	public static String regionValue() throws JspException {
		try {
			return currentInstance.getRegion();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function regionValue()");
		}
	}

	public static String countryValue() throws JspException {
		try {
			return currentInstance.getCountry();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function countryValue()");
		}
	}

	public static String disambiguationIdValue() throws JspException {
		try {
			return currentInstance.getDisambiguationId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function disambiguationIdValue()");
		}
	}

	public static String disambiguationSourceValue() throws JspException {
		try {
			return currentInstance.getDisambiguationSource();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function disambiguationSourceValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		seqnum = 0;
		type = null;
		orgType = null;
		title = null;
		translatedTitle = null;
		shortDescription = null;
		amount = null;
		currency = null;
		url = null;
		startDate = null;
		endDate = null;
		organization = null;
		city = null;
		region = null;
		country = null;
		disambiguationId = null;
		disambiguationSource = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ORCiDTagLibTagSupport>();

	}

}
