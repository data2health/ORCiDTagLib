package edu.uiowa.slis.ORCiDTagLib.externalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ExternalIdName extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			if (!theExternalId.commitNeeded) {
				pageContext.getOut().print(theExternalId.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for name tag ");
		}
		return SKIP_BODY;
	}

	public String getName() throws JspTagException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			return theExternalId.getName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for name tag ");
		}
	}

	public void setName(String name) throws JspTagException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			theExternalId.setName(name);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for name tag ");
		}
	}

}
