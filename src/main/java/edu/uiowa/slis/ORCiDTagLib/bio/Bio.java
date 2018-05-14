package edu.uiowa.slis.ORCiDTagLib.bio;

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

public class Bio extends ORCiDTagLibTagSupport {

	static Bio currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Bio.class);

	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();

	int ID = 0;
	String givenNames = null;
	String familyName = null;
	String creditName = null;
	String biography = null;
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

			BioIterator theBioIterator = (BioIterator)findAncestorWithClass(this, BioIterator.class);

			if (theBioIterator != null) {
				ID = theBioIterator.getID();
			}

			if (theBioIterator == null && theProfile == null && ID == 0) {
				// no ID was provided - the default is to assume that it is a new Bio and to generate a new ID
				ID = Sequence.generateID();
				log.debug("generating new Bio " + ID);
				insertEntity();
			} else {
				// an iterator or ID was provided as an attribute - we need to load a Bio from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select given_names,family_name,credit_name,biography,country from orcid_dump.bio where id = ?");
				stmt.setInt(1,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (givenNames == null)
						givenNames = rs.getString(1);
					if (familyName == null)
						familyName = rs.getString(2);
					if (creditName == null)
						creditName = rs.getString(3);
					if (biography == null)
						biography = rs.getString(4);
					if (country == null)
						country = rs.getString(5);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving ID " + ID);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update orcid_dump.bio set given_names = ?, family_name = ?, credit_name = ?, biography = ?, country = ? where id = ?");
				stmt.setString(1,givenNames);
				stmt.setString(2,familyName);
				stmt.setString(3,creditName);
				stmt.setString(4,biography);
				stmt.setString(5,country);
				stmt.setInt(6,ID);
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
			if (givenNames == null)
				givenNames = "";
			if (familyName == null)
				familyName = "";
			if (creditName == null)
				creditName = "";
			if (biography == null)
				biography = "";
			if (country == null)
				country = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into orcid_dump.bio(id,given_names,family_name,credit_name,biography,country) values (?,?,?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setString(2,givenNames);
			stmt.setString(3,familyName);
			stmt.setString(4,creditName);
			stmt.setString(5,biography);
			stmt.setString(6,country);
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

	public String getGivenNames () {
		if (commitNeeded)
			return "";
		else
			return givenNames;
	}

	public void setGivenNames (String givenNames) {
		this.givenNames = givenNames;
		commitNeeded = true;
	}

	public String getActualGivenNames () {
		return givenNames;
	}

	public String getFamilyName () {
		if (commitNeeded)
			return "";
		else
			return familyName;
	}

	public void setFamilyName (String familyName) {
		this.familyName = familyName;
		commitNeeded = true;
	}

	public String getActualFamilyName () {
		return familyName;
	}

	public String getCreditName () {
		if (commitNeeded)
			return "";
		else
			return creditName;
	}

	public void setCreditName (String creditName) {
		this.creditName = creditName;
		commitNeeded = true;
	}

	public String getActualCreditName () {
		return creditName;
	}

	public String getBiography () {
		if (commitNeeded)
			return "";
		else
			return biography;
	}

	public void setBiography (String biography) {
		this.biography = biography;
		commitNeeded = true;
	}

	public String getActualBiography () {
		return biography;
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

	public static String givenNamesValue() throws JspException {
		try {
			return currentInstance.getGivenNames();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function givenNamesValue()");
		}
	}

	public static String familyNameValue() throws JspException {
		try {
			return currentInstance.getFamilyName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function familyNameValue()");
		}
	}

	public static String creditNameValue() throws JspException {
		try {
			return currentInstance.getCreditName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function creditNameValue()");
		}
	}

	public static String biographyValue() throws JspException {
		try {
			return currentInstance.getBiography();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function biographyValue()");
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
		givenNames = null;
		familyName = null;
		creditName = null;
		biography = null;
		country = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ORCiDTagLibTagSupport>();

	}

}
