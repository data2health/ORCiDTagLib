package edu.uiowa.slis.ORCiDTagLib.keyword;

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

public class Keyword extends ORCiDTagLibTagSupport {

	static Keyword currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Keyword.class);

	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();

	int ID = 0;
	int seqnum = 0;
	String keyword = null;

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

			KeywordIterator theKeywordIterator = (KeywordIterator)findAncestorWithClass(this, KeywordIterator.class);

			if (theKeywordIterator != null) {
				ID = theKeywordIterator.getID();
				seqnum = theKeywordIterator.getSeqnum();
			}

			if (theKeywordIterator == null && theBio == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new Keyword and to generate a new seqnum
				seqnum = Sequence.generateID();
				log.debug("generating new Keyword " + seqnum);
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a Keyword from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select keyword from orcid_dump.keyword where id = ? and seqnum = ?");
				stmt.setInt(1,ID);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (keyword == null)
						keyword = rs.getString(1);
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
				PreparedStatement stmt = getConnection().prepareStatement("update orcid_dump.keyword set keyword = ? where id = ? and seqnum = ?");
				stmt.setString(1,keyword);
				stmt.setInt(2,ID);
				stmt.setInt(3,seqnum);
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
				log.debug("generating new Keyword " + seqnum);
			}

			if (keyword == null)
				keyword = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into orcid_dump.keyword(id,seqnum,keyword) values (?,?,?)");
			stmt.setInt(1,ID);
			stmt.setInt(2,seqnum);
			stmt.setString(3,keyword);
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

	public String getKeyword () {
		if (commitNeeded)
			return "";
		else
			return keyword;
	}

	public void setKeyword (String keyword) {
		this.keyword = keyword;
		commitNeeded = true;
	}

	public String getActualKeyword () {
		return keyword;
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

	public static String keywordValue() throws JspException {
		try {
			return currentInstance.getKeyword();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function keywordValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		seqnum = 0;
		keyword = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ORCiDTagLibTagSupport>();

	}

}
