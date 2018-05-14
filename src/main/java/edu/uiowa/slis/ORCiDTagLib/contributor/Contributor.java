package edu.uiowa.slis.ORCiDTagLib.contributor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.ORCiDTagLib.work.Work;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;
import edu.uiowa.slis.ORCiDTagLib.Sequence;

@SuppressWarnings("serial")

public class Contributor extends ORCiDTagLibTagSupport {

	static Contributor currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Contributor.class);

	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();

	int ID = 0;
	int seqnum = 0;
	int contnum = 0;
	String name = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			if (theWork!= null)
				parentEntities.addElement(theWork);

			if (theWork == null) {
			} else {
				ID = theWork.getID();
				seqnum = theWork.getSeqnum();
			}

			ContributorIterator theContributorIterator = (ContributorIterator)findAncestorWithClass(this, ContributorIterator.class);

			if (theContributorIterator != null) {
				ID = theContributorIterator.getID();
				seqnum = theContributorIterator.getSeqnum();
				contnum = theContributorIterator.getContnum();
			}

			if (theContributorIterator == null && theWork == null && contnum == 0) {
				// no contnum was provided - the default is to assume that it is a new Contributor and to generate a new contnum
				contnum = Sequence.generateID();
				log.debug("generating new Contributor " + contnum);
				insertEntity();
			} else {
				// an iterator or contnum was provided as an attribute - we need to load a Contributor from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select name from orcid_dump.contributor where id = ? and seqnum = ? and contnum = ?");
				stmt.setInt(1,ID);
				stmt.setInt(2,seqnum);
				stmt.setInt(3,contnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (name == null)
						name = rs.getString(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving contnum " + contnum);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update orcid_dump.contributor set name = ? where id = ? and seqnum = ? and contnum = ?");
				stmt.setString(1,name);
				stmt.setInt(2,ID);
				stmt.setInt(3,seqnum);
				stmt.setInt(4,contnum);
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
			if (contnum == 0) {
				contnum = Sequence.generateID();
				log.debug("generating new Contributor " + contnum);
			}

			if (name == null)
				name = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into orcid_dump.contributor(id,seqnum,contnum,name) values (?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setInt(2,seqnum);
			stmt.setInt(3,contnum);
			stmt.setString(4,name);
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

	public int getContnum () {
		return contnum;
	}

	public void setContnum (int contnum) {
		this.contnum = contnum;
	}

	public int getActualContnum () {
		return contnum;
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

	public static int contnumValue() throws JspException {
		try {
			return currentInstance.getContnum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function contnumValue()");
		}
	}

	public static String nameValue() throws JspException {
		try {
			return currentInstance.getName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function nameValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		seqnum = 0;
		contnum = 0;
		name = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ORCiDTagLibTagSupport>();

	}

}
