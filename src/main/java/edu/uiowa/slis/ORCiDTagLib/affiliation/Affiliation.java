package edu.uiowa.slis.ORCiDTagLib.affiliation;

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

public class Affiliation extends ORCiDTagLibTagSupport {

	static Affiliation currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Affiliation.class);

	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();

	int ID = 0;
	int seqnum = 0;
	String type = null;
	String department = null;
	String title = null;
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

			AffiliationIterator theAffiliationIterator = (AffiliationIterator)findAncestorWithClass(this, AffiliationIterator.class);

			if (theAffiliationIterator != null) {
				ID = theAffiliationIterator.getID();
				seqnum = theAffiliationIterator.getSeqnum();
			}

			if (theAffiliationIterator == null && theProfile == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new Affiliation and to generate a new seqnum
				seqnum = Sequence.generateID();
				log.debug("generating new Affiliation " + seqnum);
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a Affiliation from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select type,department,title,start_date,end_date,organization,city,region,country,disambiguation_id,disambiguation_source from orcid_dump.affiliation where id = ? and seqnum = ?");
				stmt.setInt(1,ID);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (type == null)
						type = rs.getString(1);
					if (department == null)
						department = rs.getString(2);
					if (title == null)
						title = rs.getString(3);
					if (startDate == null)
						startDate = rs.getDate(4);
					if (endDate == null)
						endDate = rs.getDate(5);
					if (organization == null)
						organization = rs.getString(6);
					if (city == null)
						city = rs.getString(7);
					if (region == null)
						region = rs.getString(8);
					if (country == null)
						country = rs.getString(9);
					if (disambiguationId == null)
						disambiguationId = rs.getString(10);
					if (disambiguationSource == null)
						disambiguationSource = rs.getString(11);
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
				PreparedStatement stmt = getConnection().prepareStatement("update orcid_dump.affiliation set type = ?, department = ?, title = ?, start_date = ?, end_date = ?, organization = ?, city = ?, region = ?, country = ?, disambiguation_id = ?, disambiguation_source = ? where id = ? and seqnum = ?");
				stmt.setString(1,type);
				stmt.setString(2,department);
				stmt.setString(3,title);
				stmt.setDate(4,startDate == null ? null : new java.sql.Date(startDate.getTime()));
				stmt.setDate(5,endDate == null ? null : new java.sql.Date(endDate.getTime()));
				stmt.setString(6,organization);
				stmt.setString(7,city);
				stmt.setString(8,region);
				stmt.setString(9,country);
				stmt.setString(10,disambiguationId);
				stmt.setString(11,disambiguationSource);
				stmt.setInt(12,ID);
				stmt.setInt(13,seqnum);
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
				log.debug("generating new Affiliation " + seqnum);
			}

			if (type == null)
				type = "";
			if (department == null)
				department = "";
			if (title == null)
				title = "";
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
			PreparedStatement stmt = getConnection().prepareStatement("insert into orcid_dump.affiliation(id,seqnum,type,department,title,start_date,end_date,organization,city,region,country,disambiguation_id,disambiguation_source) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setInt(2,seqnum);
			stmt.setString(3,type);
			stmt.setString(4,department);
			stmt.setString(5,title);
			stmt.setDate(6,startDate == null ? null : new java.sql.Date(startDate.getTime()));
			stmt.setDate(7,endDate == null ? null : new java.sql.Date(endDate.getTime()));
			stmt.setString(8,organization);
			stmt.setString(9,city);
			stmt.setString(10,region);
			stmt.setString(11,country);
			stmt.setString(12,disambiguationId);
			stmt.setString(13,disambiguationSource);
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

	public String getDepartment () {
		if (commitNeeded)
			return "";
		else
			return department;
	}

	public void setDepartment (String department) {
		this.department = department;
		commitNeeded = true;
	}

	public String getActualDepartment () {
		return department;
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

	public static String departmentValue() throws JspException {
		try {
			return currentInstance.getDepartment();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function departmentValue()");
		}
	}

	public static String titleValue() throws JspException {
		try {
			return currentInstance.getTitle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function titleValue()");
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
		department = null;
		title = null;
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
