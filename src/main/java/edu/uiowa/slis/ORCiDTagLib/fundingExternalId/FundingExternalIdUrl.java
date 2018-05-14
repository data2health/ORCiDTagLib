package edu.uiowa.slis.ORCiDTagLib.fundingExternalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingExternalIdUrl extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			if (!theFundingExternalId.commitNeeded) {
				pageContext.getOut().print(theFundingExternalId.getUrl());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for url tag ");
		}
		return SKIP_BODY;
	}

	public String getUrl() throws JspTagException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			return theFundingExternalId.getUrl();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for url tag ");
		}
	}

	public void setUrl(String url) throws JspTagException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			theFundingExternalId.setUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for url tag ");
		}
	}

}
