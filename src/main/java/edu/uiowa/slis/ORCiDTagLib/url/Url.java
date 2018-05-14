package edu.uiowa.slis.ORCiDTagLib.url;

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

public class Url extends ORCiDTagLibTagSupport {

	static Url currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Url.class);

	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();

	int ID = 0;
	int seqnum = 0;
	String name = null;
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

			UrlIterator theUrlIterator = (UrlIterator)findAncestorWithClass(this, UrlIterator.class);

			if (theUrlIterator != null) {
				ID = theUrlIterator.getID();
				seqnum = theUrlIterator.getSeqnum();
			}

			if (theUrlIterator == null && theBio == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new Url and to generate a new seqnum
				seqnum = Sequence.generateID();
				log.debug("generating new Url " + seqnum);
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a Url from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select name,url from orcid_dump.url where id = ? and seqnum = ?");
				stmt.setInt(1,ID);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (name == null)
						name = rs.getString(1);
					if (url == null)
						url = rs.getString(2);
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
				PreparedStatement stmt = getConnection().prepareStatement("update orcid_dump.url set name = ?, url = ? where id = ? and seqnum = ?");
				stmt.setString(1,name);
				stmt.setString(2,url);
				stmt.setInt(3,ID);
				stmt.setInt(4,seqnum);
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
				log.debug("generating new Url " + seqnum);
			}

			if (name == null)
				name = "";
			if (url == null)
				url = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into orcid_dump.url(id,seqnum,name,url) values (?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setInt(2,seqnum);
			stmt.setString(3,name);
			stmt.setString(4,url);
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

	public static String nameValue() throws JspException {
		try {
			return currentInstance.getName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function nameValue()");
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
		name = null;
		url = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ORCiDTagLibTagSupport>();

	}

}
