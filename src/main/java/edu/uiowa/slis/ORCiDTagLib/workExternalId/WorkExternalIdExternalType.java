package edu.uiowa.slis.ORCiDTagLib.workExternalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkExternalIdExternalType extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			if (!theWorkExternalId.commitNeeded) {
				pageContext.getOut().print(theWorkExternalId.getExternalType());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for externalType tag ");
		}
		return SKIP_BODY;
	}

	public String getExternalType() throws JspTagException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			return theWorkExternalId.getExternalType();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for externalType tag ");
		}
	}

	public void setExternalType(String externalType) throws JspTagException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			theWorkExternalId.setExternalType(externalType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for externalType tag ");
		}
	}

}
