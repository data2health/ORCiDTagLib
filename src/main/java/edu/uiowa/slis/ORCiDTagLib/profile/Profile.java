package edu.uiowa.slis.ORCiDTagLib.profile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;
import edu.uiowa.slis.ORCiDTagLib.Sequence;

@SuppressWarnings("serial")

public class Profile extends ORCiDTagLibTagSupport {

	static Profile currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Profile.class);

	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();

	int ID = 0;
	String uri = null;
	String path = null;
	String host = null;
	String locale = null;
	String method = null;
	String completion = null;
	String submission = null;
	String modified = null;
	boolean claimed = false;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			ProfileIterator theProfileIterator = (ProfileIterator)findAncestorWithClass(this, ProfileIterator.class);

			if (theProfileIterator != null) {
				ID = theProfileIterator.getID();
			}

			if (theProfileIterator == null && ID == 0) {
				// no ID was provided - the default is to assume that it is a new Profile and to generate a new ID
				ID = Sequence.generateID();
				log.debug("generating new Profile " + ID);
				insertEntity();
			} else {
				// an iterator or ID was provided as an attribute - we need to load a Profile from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select uri,path,host,locale,method,completion,submission,modified,claimed from orcid_dump.profile where id = ?");
				stmt.setInt(1,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (uri == null)
						uri = rs.getString(1);
					if (path == null)
						path = rs.getString(2);
					if (host == null)
						host = rs.getString(3);
					if (locale == null)
						locale = rs.getString(4);
					if (method == null)
						method = rs.getString(5);
					if (completion == null)
						completion = rs.getString(6);
					if (submission == null)
						submission = rs.getString(7);
					if (modified == null)
						modified = rs.getString(8);
					if (claimed == false)
						claimed = rs.getBoolean(9);
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
				PreparedStatement stmt = getConnection().prepareStatement("update orcid_dump.profile set uri = ?, path = ?, host = ?, locale = ?, method = ?, completion = ?, submission = ?, modified = ?, claimed = ? where id = ?");
				stmt.setString(1,uri);
				stmt.setString(2,path);
				stmt.setString(3,host);
				stmt.setString(4,locale);
				stmt.setString(5,method);
				stmt.setString(6,completion);
				stmt.setString(7,submission);
				stmt.setString(8,modified);
				stmt.setBoolean(9,claimed);
				stmt.setInt(10,ID);
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
			if (ID == 0) {
				ID = Sequence.generateID();
				log.debug("generating new Profile " + ID);
			}

			if (uri == null)
				uri = "";
			if (path == null)
				path = "";
			if (host == null)
				host = "";
			if (locale == null)
				locale = "";
			if (method == null)
				method = "";
			if (completion == null)
				completion = "";
			if (submission == null)
				submission = "";
			if (modified == null)
				modified = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into orcid_dump.profile(id,uri,path,host,locale,method,completion,submission,modified,claimed) values (?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setString(2,uri);
			stmt.setString(3,path);
			stmt.setString(4,host);
			stmt.setString(5,locale);
			stmt.setString(6,method);
			stmt.setString(7,completion);
			stmt.setString(8,submission);
			stmt.setString(9,modified);
			stmt.setBoolean(10,claimed);
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

	public String getUri () {
		if (commitNeeded)
			return "";
		else
			return uri;
	}

	public void setUri (String uri) {
		this.uri = uri;
		commitNeeded = true;
	}

	public String getActualUri () {
		return uri;
	}

	public String getPath () {
		if (commitNeeded)
			return "";
		else
			return path;
	}

	public void setPath (String path) {
		this.path = path;
		commitNeeded = true;
	}

	public String getActualPath () {
		return path;
	}

	public String getHost () {
		if (commitNeeded)
			return "";
		else
			return host;
	}

	public void setHost (String host) {
		this.host = host;
		commitNeeded = true;
	}

	public String getActualHost () {
		return host;
	}

	public String getLocale () {
		if (commitNeeded)
			return "";
		else
			return locale;
	}

	public void setLocale (String locale) {
		this.locale = locale;
		commitNeeded = true;
	}

	public String getActualLocale () {
		return locale;
	}

	public String getMethod () {
		if (commitNeeded)
			return "";
		else
			return method;
	}

	public void setMethod (String method) {
		this.method = method;
		commitNeeded = true;
	}

	public String getActualMethod () {
		return method;
	}

	public String getCompletion () {
		if (commitNeeded)
			return "";
		else
			return completion;
	}

	public void setCompletion (String completion) {
		this.completion = completion;
		commitNeeded = true;
	}

	public String getActualCompletion () {
		return completion;
	}

	public String getSubmission () {
		if (commitNeeded)
			return "";
		else
			return submission;
	}

	public void setSubmission (String submission) {
		this.submission = submission;
		commitNeeded = true;
	}

	public String getActualSubmission () {
		return submission;
	}

	public String getModified () {
		if (commitNeeded)
			return "";
		else
			return modified;
	}

	public void setModified (String modified) {
		this.modified = modified;
		commitNeeded = true;
	}

	public String getActualModified () {
		return modified;
	}

	public boolean getClaimed () {
		return claimed;
	}

	public void setClaimed (boolean claimed) {
		this.claimed = claimed;
		commitNeeded = true;
	}

	public boolean getActualClaimed () {
		return claimed;
	}

	public static int IDValue() throws JspException {
		try {
			return currentInstance.getID();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function IDValue()");
		}
	}

	public static String uriValue() throws JspException {
		try {
			return currentInstance.getUri();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function uriValue()");
		}
	}

	public static String pathValue() throws JspException {
		try {
			return currentInstance.getPath();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pathValue()");
		}
	}

	public static String hostValue() throws JspException {
		try {
			return currentInstance.getHost();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function hostValue()");
		}
	}

	public static String localeValue() throws JspException {
		try {
			return currentInstance.getLocale();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function localeValue()");
		}
	}

	public static String methodValue() throws JspException {
		try {
			return currentInstance.getMethod();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function methodValue()");
		}
	}

	public static String completionValue() throws JspException {
		try {
			return currentInstance.getCompletion();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function completionValue()");
		}
	}

	public static String submissionValue() throws JspException {
		try {
			return currentInstance.getSubmission();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function submissionValue()");
		}
	}

	public static String modifiedValue() throws JspException {
		try {
			return currentInstance.getModified();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function modifiedValue()");
		}
	}

	public static boolean claimedValue() throws JspException {
		try {
			return currentInstance.getClaimed();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function claimedValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		uri = null;
		path = null;
		host = null;
		locale = null;
		method = null;
		completion = null;
		submission = null;
		modified = null;
		claimed = false;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ORCiDTagLibTagSupport>();

	}

}
