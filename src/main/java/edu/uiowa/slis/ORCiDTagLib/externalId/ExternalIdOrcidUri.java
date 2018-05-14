package edu.uiowa.slis.ORCiDTagLib.externalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ExternalIdOrcidUri extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			if (!theExternalId.commitNeeded) {
				pageContext.getOut().print(theExternalId.getOrcidUri());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for orcidUri tag ");
		}
		return SKIP_BODY;
	}

	public String getOrcidUri() throws JspTagException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			return theExternalId.getOrcidUri();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for orcidUri tag ");
		}
	}

	public void setOrcidUri(String orcidUri) throws JspTagException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			theExternalId.setOrcidUri(orcidUri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for orcidUri tag ");
		}
	}

}
