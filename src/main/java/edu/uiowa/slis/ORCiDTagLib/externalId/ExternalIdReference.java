package edu.uiowa.slis.ORCiDTagLib.externalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ExternalIdReference extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			if (!theExternalId.commitNeeded) {
				pageContext.getOut().print(theExternalId.getReference());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for reference tag ");
		}
		return SKIP_BODY;
	}

	public String getReference() throws JspTagException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			return theExternalId.getReference();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for reference tag ");
		}
	}

	public void setReference(String reference) throws JspTagException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			theExternalId.setReference(reference);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for reference tag ");
		}
	}

}
