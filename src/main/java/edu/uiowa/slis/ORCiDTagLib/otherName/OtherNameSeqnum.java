package edu.uiowa.slis.ORCiDTagLib.otherName;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class OtherNameSeqnum extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			OtherName theOtherName = (OtherName)findAncestorWithClass(this, OtherName.class);
			if (!theOtherName.commitNeeded) {
				pageContext.getOut().print(theOtherName.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherName for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			OtherName theOtherName = (OtherName)findAncestorWithClass(this, OtherName.class);
			return theOtherName.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherName for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			OtherName theOtherName = (OtherName)findAncestorWithClass(this, OtherName.class);
			theOtherName.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherName for seqnum tag ");
		}
	}

}
