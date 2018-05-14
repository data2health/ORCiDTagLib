package edu.uiowa.slis.ORCiDTagLib.externalId;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.ORCiDTagLib.bio.Bio;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;
import edu.uiowa.slis.ORCiDTagLib.Sequence;

@SuppressWarnings("serial")

public class ExternalId extends ORCiDTagLibTagSupport {

	static ExternalId currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(ExternalId.class);

	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();

	int ID = 0;
	int seqnum = 0;
	String orcidUri = null;
	String orcidPath = null;
	String orcidHost = null;
	String name = null;
	String reference = null;
	String url = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			if (theBio!= null)
				parentEntities.addElement(theBio);

			if (theBio == null) {
			} else {
				ID = theBio.getID();
			}

			ExternalIdIterator theExternalIdIterator = (ExternalIdIterator)findAncestorWithClass(this, ExternalIdIterator.class);

			if (theExternalIdIterator != null) {
				ID = theExternalIdIterator.getID();
				seqnum = theExternalIdIterator.getSeqnum();
			}

			if (theExternalIdIterator == null && theBio == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new ExternalId and to generate a new seqnum
				seqnum = Sequence.generateID();
				log.debug("generating new ExternalId " + seqnum);
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a ExternalId from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select orcid_uri,orcid_path,orcid_host,name,reference,url from orcid_dump.external_id where id = ? and seqnum = ?");
				stmt.setInt(1,ID);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (orcidUri == null)
						orcidUri = rs.getString(1);
					if (orcidPath == null)
						orcidPath = rs.getString(2);
					if (orcidHost == null)
						orcidHost = rs.getString(3);
					if (name == null)
						name = rs.getString(4);
					if (reference == null)
						reference = rs.getString(5);
					if (url == null)
						url = rs.getString(6);
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
				PreparedStatement stmt = getConnection().prepareStatement("update orcid_dump.external_id set orcid_uri = ?, orcid_path = ?, orcid_host = ?, name = ?, reference = ?, url = ? where id = ? and seqnum = ?");
				stmt.setString(1,orcidUri);
				stmt.setString(2,orcidPath);
				stmt.setString(3,orcidHost);
				stmt.setString(4,name);
				stmt.setString(5,reference);
				stmt.setString(6,url);
				stmt.setInt(7,ID);
				stmt.setInt(8,seqnum);
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
				log.debug("generating new ExternalId " + seqnum);
			}

			if (orcidUri == null)
				orcidUri = "";
			if (orcidPath == null)
				orcidPath = "";
			if (orcidHost == null)
				orcidHost = "";
			if (name == null)
				name = "";
			if (reference == null)
				reference = "";
			if (url == null)
				url = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into orcid_dump.external_id(id,seqnum,orcid_uri,orcid_path,orcid_host,name,reference,url) values (?,?,?,?,?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setInt(2,seqnum);
			stmt.setString(3,orcidUri);
			stmt.setString(4,orcidPath);
			stmt.setString(5,orcidHost);
			stmt.setString(6,name);
			stmt.setString(7,reference);
			stmt.setString(8,url);
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

	public String getOrcidUri () {
		if (commitNeeded)
			return "";
		else
			return orcidUri;
	}

	public void setOrcidUri (String orcidUri) {
		this.orcidUri = orcidUri;
		commitNeeded = true;
	}

	public String getActualOrcidUri () {
		return orcidUri;
	}

	public String getOrcidPath () {
		if (commitNeeded)
			return "";
		else
			return orcidPath;
	}

	public void setOrcidPath (String orcidPath) {
		this.orcidPath = orcidPath;
		commitNeeded = true;
	}

	public String getActualOrcidPath () {
		return orcidPath;
	}

	public String getOrcidHost () {
		if (commitNeeded)
			return "";
		else
			return orcidHost;
	}

	public void setOrcidHost (String orcidHost) {
		this.orcidHost = orcidHost;
		commitNeeded = true;
	}

	public String getActualOrcidHost () {
		return orcidHost;
	}

	public String getName () {
		if (commitNeeded)
			return "";
		else
			return name;
	}

	public void setName (String name) {
		this.name = name;
		commitNeeded = true;
	}

	public String getActualName () {
		return name;
	}

	public String getReference () {
		if (commitNeeded)
			return "";
		else
			return reference;
	}

	public void setReference (String reference) {
		this.reference = reference;
		commitNeeded = true;
	}

	public String getActualReference () {
		return reference;
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

	public static String orcidUriValue() throws JspException {
		try {
			return currentInstance.getOrcidUri();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function orcidUriValue()");
		}
	}

	public static String orcidPathValue() throws JspException {
		try {
			return currentInstance.getOrcidPath();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function orcidPathValue()");
		}
	}

	public static String orcidHostValue() throws JspException {
		try {
			return currentInstance.getOrcidHost();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function orcidHostValue()");
		}
	}

	public static String nameValue() throws JspException {
		try {
			return currentInstance.getName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function nameValue()");
		}
	}

	public static String referenceValue() throws JspException {
		try {
			return currentInstance.getReference();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function referenceValue()");
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
		orcidUri = null;
		orcidPath = null;
		orcidHost = null;
		name = null;
		reference = null;
		url = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ORCiDTagLibTagSupport>();

	}

}
