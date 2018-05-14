package edu.uiowa.slis.ORCiDTagLib.email;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class EmailCurrent extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			if (!theEmail.commitNeeded) {
				pageContext.getOut().print(theEmail.getCurrent());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for current tag ");
		}
		return SKIP_BODY;
	}

	public boolean getCurrent() throws JspTagException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			return theEmail.getCurrent();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for current tag ");
		}
	}

	public void setCurrent(boolean current) throws JspTagException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			theEmail.setCurrent(current);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for current tag ");
		}
	}

}
