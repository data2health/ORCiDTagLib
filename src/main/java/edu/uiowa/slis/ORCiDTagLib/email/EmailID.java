package edu.uiowa.slis.ORCiDTagLib.email;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class EmailID extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			if (!theEmail.commitNeeded) {
				pageContext.getOut().print(theEmail.getID());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			return theEmail.getID();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			theEmail.setID(ID);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for ID tag ");
		}
	}

}
