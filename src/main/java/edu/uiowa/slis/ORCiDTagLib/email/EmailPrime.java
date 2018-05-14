package edu.uiowa.slis.ORCiDTagLib.email;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class EmailPrime extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			if (!theEmail.commitNeeded) {
				pageContext.getOut().print(theEmail.getPrime());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for prime tag ");
		}
		return SKIP_BODY;
	}

	public boolean getPrime() throws JspTagException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			return theEmail.getPrime();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for prime tag ");
		}
	}

	public void setPrime(boolean prime) throws JspTagException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			theEmail.setPrime(prime);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for prime tag ");
		}
	}

}
