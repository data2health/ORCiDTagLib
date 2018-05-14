package edu.uiowa.slis.ORCiDTagLib.externalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ExternalIdID extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			if (!theExternalId.commitNeeded) {
				pageContext.getOut().print(theExternalId.getID());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			return theExternalId.getID();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			ExternalId theExternalId = (ExternalId)findAncestorWithClass(this, ExternalId.class);
			theExternalId.setID(ID);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ExternalId for ID tag ");
		}
	}

}
