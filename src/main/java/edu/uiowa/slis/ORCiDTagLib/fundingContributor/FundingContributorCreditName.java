package edu.uiowa.slis.ORCiDTagLib.fundingContributor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingContributorCreditName extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			if (!theFundingContributor.commitNeeded) {
				pageContext.getOut().print(theFundingContributor.getCreditName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for creditName tag ");
		}
		return SKIP_BODY;
	}

	public String getCreditName() throws JspTagException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			return theFundingContributor.getCreditName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for creditName tag ");
		}
	}

	public void setCreditName(String creditName) throws JspTagException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			theFundingContributor.setCreditName(creditName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for creditName tag ");
		}
	}

}
