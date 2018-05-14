package edu.uiowa.slis.ORCiDTagLib.email;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class EmailVerified extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			if (!theEmail.commitNeeded) {
				pageContext.getOut().print(theEmail.getVerified());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for verified tag ");
		}
		return SKIP_BODY;
	}

	public boolean getVerified() throws JspTagException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			return theEmail.getVerified();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for verified tag ");
		}
	}

	public void setVerified(boolean verified) throws JspTagException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			theEmail.setVerified(verified);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for verified tag ");
		}
	}

}
