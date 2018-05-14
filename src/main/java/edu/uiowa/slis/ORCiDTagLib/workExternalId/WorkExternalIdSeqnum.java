package edu.uiowa.slis.ORCiDTagLib.workExternalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkExternalIdSeqnum extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			if (!theWorkExternalId.commitNeeded) {
				pageContext.getOut().print(theWorkExternalId.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			return theWorkExternalId.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			theWorkExternalId.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for seqnum tag ");
		}
	}

}
