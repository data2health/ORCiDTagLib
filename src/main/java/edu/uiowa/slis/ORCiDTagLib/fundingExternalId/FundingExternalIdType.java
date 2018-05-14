package edu.uiowa.slis.ORCiDTagLib.fundingExternalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingExternalIdType extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			if (!theFundingExternalId.commitNeeded) {
				pageContext.getOut().print(theFundingExternalId.getType());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for type tag ");
		}
		return SKIP_BODY;
	}

	public String getType() throws JspTagException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			return theFundingExternalId.getType();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for type tag ");
		}
	}

	public void setType(String type) throws JspTagException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			theFundingExternalId.setType(type);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for type tag ");
		}
	}

}
