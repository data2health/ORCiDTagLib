package edu.uiowa.slis.ORCiDTagLib.workExternalId;

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

public class WorkExternalId extends ORCiDTagLibTagSupport {

	static WorkExternalId currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(WorkExternalId.class);

	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();

	int ID = 0;
	int seqnum = 0;
	int worknum = 0;
	String externalType = null;
	String externalId = null;

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

			WorkExternalIdIterator theWorkExternalIdIterator = (WorkExternalIdIterator)findAncestorWithClass(this, WorkExternalIdIterator.class);

			if (theWorkExternalIdIterator != null) {
				ID = theWorkExternalIdIterator.getID();
				seqnum = theWorkExternalIdIterator.getSeqnum();
				worknum = theWorkExternalIdIterator.getWorknum();
			}

			if (theWorkExternalIdIterator == null && theWork == null && worknum == 0) {
				// no worknum was provided - the default is to assume that it is a new WorkExternalId and to generate a new worknum
				worknum = Sequence.generateID();
				log.debug("generating new WorkExternalId " + worknum);
				insertEntity();
			} else {
				// an iterator or worknum was provided as an attribute - we need to load a WorkExternalId from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select external_type,external_id from orcid_dump.work_external_id where id = ? and seqnum = ? and worknum = ?");
				stmt.setInt(1,ID);
				stmt.setInt(2,seqnum);
				stmt.setInt(3,worknum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (externalType == null)
						externalType = rs.getString(1);
					if (externalId == null)
						externalId = rs.getString(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving worknum " + worknum);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update orcid_dump.work_external_id set external_type = ?, external_id = ? where id = ? and seqnum = ? and worknum = ?");
				stmt.setString(1,externalType);
				stmt.setString(2,externalId);
				stmt.setInt(3,ID);
				stmt.setInt(4,seqnum);
				stmt.setInt(5,worknum);
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
			if (worknum == 0) {
				worknum = Sequence.generateID();
				log.debug("generating new WorkExternalId " + worknum);
			}

			if (externalType == null)
				externalType = "";
			if (externalId == null)
				externalId = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into orcid_dump.work_external_id(id,seqnum,worknum,external_type,external_id) values (?,?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setInt(2,seqnum);
			stmt.setInt(3,worknum);
			stmt.setString(4,externalType);
			stmt.setString(5,externalId);
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

	public int getWorknum () {
		return worknum;
	}

	public void setWorknum (int worknum) {
		this.worknum = worknum;
	}

	public int getActualWorknum () {
		return worknum;
	}

	public String getExternalType () {
		if (commitNeeded)
			return "";
		else
			return externalType;
	}

	public void setExternalType (String externalType) {
		this.externalType = externalType;
		commitNeeded = true;
	}

	public String getActualExternalType () {
		return externalType;
	}

	public String getExternalId () {
		if (commitNeeded)
			return "";
		else
			return externalId;
	}

	public void setExternalId (String externalId) {
		this.externalId = externalId;
		commitNeeded = true;
	}

	public String getActualExternalId () {
		return externalId;
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

	public static int worknumValue() throws JspException {
		try {
			return currentInstance.getWorknum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function worknumValue()");
		}
	}

	public static String externalTypeValue() throws JspException {
		try {
			return currentInstance.getExternalType();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function externalTypeValue()");
		}
	}

	public static String externalIdValue() throws JspException {
		try {
			return currentInstance.getExternalId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function externalIdValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		seqnum = 0;
		worknum = 0;
		externalType = null;
		externalId = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ORCiDTagLibTagSupport>();

	}

}
