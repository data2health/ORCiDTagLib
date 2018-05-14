package edu.uiowa.slis.ORCiDTagLib.fundingContributor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.ORCiDTagLib.funding.Funding;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;
import edu.uiowa.slis.ORCiDTagLib.Sequence;

@SuppressWarnings("serial")

public class FundingContributor extends ORCiDTagLibTagSupport {

	static FundingContributor currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(FundingContributor.class);

	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();

	int ID = 0;
	int seqnum = 0;
	int seqnum2 = 0;
	String orcidId = null;
	String creditName = null;
	String email = null;
	String role = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			if (theFunding!= null)
				parentEntities.addElement(theFunding);

			if (theFunding == null) {
			} else {
				ID = theFunding.getID();
				seqnum = theFunding.getSeqnum();
			}

			FundingContributorIterator theFundingContributorIterator = (FundingContributorIterator)findAncestorWithClass(this, FundingContributorIterator.class);

			if (theFundingContributorIterator != null) {
				ID = theFundingContributorIterator.getID();
				seqnum = theFundingContributorIterator.getSeqnum();
				seqnum2 = theFundingContributorIterator.getSeqnum2();
			}

			if (theFundingContributorIterator == null && theFunding == null && seqnum2 == 0) {
				// no seqnum2 was provided - the default is to assume that it is a new FundingContributor and to generate a new seqnum2
				seqnum2 = Sequence.generateID();
				log.debug("generating new FundingContributor " + seqnum2);
				insertEntity();
			} else {
				// an iterator or seqnum2 was provided as an attribute - we need to load a FundingContributor from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select orcid_id,credit_name,email,role from orcid_dump.funding_contributor where id = ? and seqnum = ? and seqnum2 = ?");
				stmt.setInt(1,ID);
				stmt.setInt(2,seqnum);
				stmt.setInt(3,seqnum2);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (orcidId == null)
						orcidId = rs.getString(1);
					if (creditName == null)
						creditName = rs.getString(2);
					if (email == null)
						email = rs.getString(3);
					if (role == null)
						role = rs.getString(4);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving seqnum2 " + seqnum2);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update orcid_dump.funding_contributor set orcid_id = ?, credit_name = ?, email = ?, role = ? where id = ? and seqnum = ? and seqnum2 = ?");
				stmt.setString(1,orcidId);
				stmt.setString(2,creditName);
				stmt.setString(3,email);
				stmt.setString(4,role);
				stmt.setInt(5,ID);
				stmt.setInt(6,seqnum);
				stmt.setInt(7,seqnum2);
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
			if (seqnum2 == 0) {
				seqnum2 = Sequence.generateID();
				log.debug("generating new FundingContributor " + seqnum2);
			}

			if (orcidId == null)
				orcidId = "";
			if (creditName == null)
				creditName = "";
			if (email == null)
				email = "";
			if (role == null)
				role = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into orcid_dump.funding_contributor(id,seqnum,seqnum2,orcid_id,credit_name,email,role) values (?,?,?,?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setInt(2,seqnum);
			stmt.setInt(3,seqnum2);
			stmt.setString(4,orcidId);
			stmt.setString(5,creditName);
			stmt.setString(6,email);
			stmt.setString(7,role);
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

	public int getSeqnum2 () {
		return seqnum2;
	}

	public void setSeqnum2 (int seqnum2) {
		this.seqnum2 = seqnum2;
	}

	public int getActualSeqnum2 () {
		return seqnum2;
	}

	public String getOrcidId () {
		if (commitNeeded)
			return "";
		else
			return orcidId;
	}

	public void setOrcidId (String orcidId) {
		this.orcidId = orcidId;
		commitNeeded = true;
	}

	public String getActualOrcidId () {
		return orcidId;
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

	public String getEmail () {
		if (commitNeeded)
			return "";
		else
			return email;
	}

	public void setEmail (String email) {
		this.email = email;
		commitNeeded = true;
	}

	public String getActualEmail () {
		return email;
	}

	public String getRole () {
		if (commitNeeded)
			return "";
		else
			return role;
	}

	public void setRole (String role) {
		this.role = role;
		commitNeeded = true;
	}

	public String getActualRole () {
		return role;
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

	public static int seqnum2Value() throws JspException {
		try {
			return currentInstance.getSeqnum2();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function seqnum2Value()");
		}
	}

	public static String orcidIdValue() throws JspException {
		try {
			return currentInstance.getOrcidId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function orcidIdValue()");
		}
	}

	public static String creditNameValue() throws JspException {
		try {
			return currentInstance.getCreditName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function creditNameValue()");
		}
	}

	public static String emailValue() throws JspException {
		try {
			return currentInstance.getEmail();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function emailValue()");
		}
	}

	public static String roleValue() throws JspException {
		try {
			return currentInstance.getRole();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function roleValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		seqnum = 0;
		seqnum2 = 0;
		orcidId = null;
		creditName = null;
		email = null;
		role = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ORCiDTagLibTagSupport>();

	}

}
