package edu.uiowa.slis.ORCiDTagLib.externalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ExternalIdOrcidPath extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			if (!theExternalId.commitNeeded) {
				pageContext.getOut().print(theExternalId.getOrcidPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for orcidPath tag ");
		}
		return SKIP_BODY;
	}

	public String getOrcidPath() throws JspTagException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			return theExternalId.getOrcidPath();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for orcidPath tag ");
		}
	}

	public void setOrcidPath(String orcidPath) throws JspTagException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			theExternalId.setOrcidPath(orcidPath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for orcidPath tag ");
		}
	}

}
