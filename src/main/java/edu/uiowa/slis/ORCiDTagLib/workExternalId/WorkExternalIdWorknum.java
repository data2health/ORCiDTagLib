package edu.uiowa.slis.ORCiDTagLib.workExternalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkExternalIdWorknum extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			if (!theWorkExternalId.commitNeeded) {
				pageContext.getOut().print(theWorkExternalId.getWorknum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for worknum tag ");
		}
		return SKIP_BODY;
	}

	public int getWorknum() throws JspTagException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			return theWorkExternalId.getWorknum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for worknum tag ");
		}
	}

	public void setWorknum(int worknum) throws JspTagException {
		try {
			WorkExternalId theWorkExternalId = (WorkExternalId)findAncestorWithClass(this, WorkExternalId.class);
			theWorkExternalId.setWorknum(worknum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing WorkExternalId for worknum tag ");
		}
	}

}
