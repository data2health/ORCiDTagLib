package edu.uiowa.slis.ORCiDTagLib.workExternalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkExternalIdExternalId extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			if (!theWorkExternalId.commitNeeded) {
				pageContext.getOut().print(theWorkExternalId.getExternalId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for externalId tag ");
		}
		return SKIP_BODY;
	}

	public String getExternalId() throws JspTagException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			return theWorkExternalId.getExternalId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for externalId tag ");
		}
	}

	public void setExternalId(String externalId) throws JspTagException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			theWorkExternalId.setExternalId(externalId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for externalId tag ");
		}
	}

}
