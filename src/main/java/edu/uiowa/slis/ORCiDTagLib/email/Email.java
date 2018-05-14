package edu.uiowa.slis.ORCiDTagLib.email;

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

public class Email extends ORCiDTagLibTagSupport {

	static Email currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Email.class);

	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();

	int ID = 0;
	int seqnum = 0;
	String email = null;
	boolean prime = false;
	boolean current = false;
	boolean verified = false;

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

			EmailIterator theEmailIterator = (EmailIterator)findAncestorWithClass(this, EmailIterator.class);

			if (theEmailIterator != null) {
				ID = theEmailIterator.getID();
				seqnum = theEmailIterator.getSeqnum();
			}

			if (theEmailIterator == null && theBio == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new Email and to generate a new seqnum
				seqnum = Sequence.generateID();
				log.debug("generating new Email " + seqnum);
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a Email from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select email,prime,current,verified from orcid_dump.email where id = ? and seqnum = ?");
				stmt.setInt(1,ID);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (email == null)
						email = rs.getString(1);
					if (prime == false)
						prime = rs.getBoolean(2);
					if (current == false)
						current = rs.getBoolean(3);
					if (verified == false)
						verified = rs.getBoolean(4);
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
				PreparedStatement stmt = getConnection().prepareStatement("update orcid_dump.email set email = ?, prime = ?, current = ?, verified = ? where id = ? and seqnum = ?");
				stmt.setString(1,email);
				stmt.setBoolean(2,prime);
				stmt.setBoolean(3,current);
				stmt.setBoolean(4,verified);
				stmt.setInt(5,ID);
				stmt.setInt(6,seqnum);
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
				log.debug("generating new Email " + seqnum);
			}

			if (email == null)
				email = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into orcid_dump.email(id,seqnum,email,prime,current,verified) values (?,?,?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setInt(2,seqnum);
			stmt.setString(3,email);
			stmt.setBoolean(4,prime);
			stmt.setBoolean(5,current);
			stmt.setBoolean(6,verified);
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

	public boolean getPrime () {
		return prime;
	}

	public void setPrime (boolean prime) {
		this.prime = prime;
		commitNeeded = true;
	}

	public boolean getActualPrime () {
		return prime;
	}

	public boolean getCurrent () {
		return current;
	}

	public void setCurrent (boolean current) {
		this.current = current;
		commitNeeded = true;
	}

	public boolean getActualCurrent () {
		return current;
	}

	public boolean getVerified () {
		return verified;
	}

	public void setVerified (boolean verified) {
		this.verified = verified;
		commitNeeded = true;
	}

	public boolean getActualVerified () {
		return verified;
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

	public static String emailValue() throws JspException {
		try {
			return currentInstance.getEmail();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function emailValue()");
		}
	}

	public static boolean primeValue() throws JspException {
		try {
			return currentInstance.getPrime();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function primeValue()");
		}
	}

	public static boolean currentValue() throws JspException {
		try {
			return currentInstance.getCurrent();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function currentValue()");
		}
	}

	public static boolean verifiedValue() throws JspException {
		try {
			return currentInstance.getVerified();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function verifiedValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		seqnum = 0;
		email = null;
		prime = false;
		current = false;
		verified = false;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ORCiDTagLibTagSupport>();

	}

}
