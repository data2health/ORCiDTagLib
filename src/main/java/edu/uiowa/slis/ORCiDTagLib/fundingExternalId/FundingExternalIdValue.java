package edu.uiowa.slis.ORCiDTagLib.fundingExternalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingExternalIdValue extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			if (!theFundingExternalId.commitNeeded) {
				pageContext.getOut().print(theFundingExternalId.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for value tag ");
		}
		return SKIP_BODY;
	}

	public String getValue() throws JspTagException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			return theFundingExternalId.getValue();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for value tag ");
		}
	}

	public void setValue(String value) throws JspTagException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			theFundingExternalId.setValue(value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for value tag ");
		}
	}

}
