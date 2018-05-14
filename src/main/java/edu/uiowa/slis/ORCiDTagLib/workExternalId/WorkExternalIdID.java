package edu.uiowa.slis.ORCiDTagLib.workExternalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkExternalIdID extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			if (!theWorkExternalId.commitNeeded) {
				pageContext.getOut().print(theWorkExternalId.getID());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			return theWorkExternalId.getID();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			theWorkExternalId.setID(ID);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for ID tag ");
		}
	}

}
