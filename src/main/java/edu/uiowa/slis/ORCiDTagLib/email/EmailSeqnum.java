package edu.uiowa.slis.ORCiDTagLib.email;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class EmailSeqnum extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			if (!theEmail.commitNeeded) {
				pageContext.getOut().print(theEmail.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			return theEmail.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			Email theEmail = (Email)findAncestorWithClass(this, Email.class);
			theEmail.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Email for seqnum tag ");
		}
	}

}
