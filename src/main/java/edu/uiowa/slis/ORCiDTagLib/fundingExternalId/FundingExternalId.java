package edu.uiowa.slis.ORCiDTagLib.fundingExternalId;

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

public class FundingExternalId extends ORCiDTagLibTagSupport {

	static FundingExternalId currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(FundingExternalId.class);

	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();

	int ID = 0;
	int seqnum = 0;
	int seqnum2 = 0;
	String type = null;
	String value = null;
	String url = null;

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

			FundingExternalIdIterator theFundingExternalIdIterator = (FundingExternalIdIterator)findAncestorWithClass(this, FundingExternalIdIterator.class);

			if (theFundingExternalIdIterator != null) {
				ID = theFundingExternalIdIterator.getID();
				seqnum = theFundingExternalIdIterator.getSeqnum();
				seqnum2 = theFundingExternalIdIterator.getSeqnum2();
			}

			if (theFundingExternalIdIterator == null && theFunding == null && seqnum2 == 0) {
				// no seqnum2 was provided - the default is to assume that it is a new FundingExternalId and to generate a new seqnum2
				seqnum2 = Sequence.generateID();
				log.debug("generating new FundingExternalId " + seqnum2);
				insertEntity();
			} else {
				// an iterator or seqnum2 was provided as an attribute - we need to load a FundingExternalId from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select type,value,url from orcid_dump.funding_external_id where id = ? and seqnum = ? and seqnum2 = ?");
				stmt.setInt(1,ID);
				stmt.setInt(2,seqnum);
				stmt.setInt(3,seqnum2);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (type == null)
						type = rs.getString(1);
					if (value == null)
						value = rs.getString(2);
					if (url == null)
						url = rs.getString(3);
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
				PreparedStatement stmt = getConnection().prepareStatement("update orcid_dump.funding_external_id set type = ?, value = ?, url = ? where id = ? and seqnum = ? and seqnum2 = ?");
				stmt.setString(1,type);
				stmt.setString(2,value);
				stmt.setString(3,url);
				stmt.setInt(4,ID);
				stmt.setInt(5,seqnum);
				stmt.setInt(6,seqnum2);
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
				log.debug("generating new FundingExternalId " + seqnum2);
			}

			if (type == null)
				type = "";
			if (value == null)
				value = "";
			if (url == null)
				url = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into orcid_dump.funding_external_id(id,seqnum,seqnum2,type,value,url) values (?,?,?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setInt(2,seqnum);
			stmt.setInt(3,seqnum2);
			stmt.setString(4,type);
			stmt.setString(5,value);
			stmt.setString(6,url);
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

	public String getValue () {
		if (commitNeeded)
			return "";
		else
			return value;
	}

	public void setValue (String value) {
		this.value = value;
		commitNeeded = true;
	}

	public String getActualValue () {
		return value;
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

	public static String typeValue() throws JspException {
		try {
			return currentInstance.getType();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function typeValue()");
		}
	}

	public static String valueValue() throws JspException {
		try {
			return currentInstance.getValue();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function valueValue()");
		}
	}

	public static String urlValue() throws JspException {
		try {
			return currentInstance.getUrl();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function urlValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		seqnum = 0;
		seqnum2 = 0;
		type = null;
		value = null;
		url = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ORCiDTagLibTagSupport>();

	}

}
