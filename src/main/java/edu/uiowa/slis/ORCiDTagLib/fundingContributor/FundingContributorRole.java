package edu.uiowa.slis.ORCiDTagLib.fundingContributor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingContributorRole extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			if (!theFundingContributor.commitNeeded) {
				pageContext.getOut().print(theFundingContributor.getRole());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for role tag ");
		}
		return SKIP_BODY;
	}

	public String getRole() throws JspTagException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			return theFundingContributor.getRole();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for role tag ");
		}
	}

	public void setRole(String role) throws JspTagException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			theFundingContributor.setRole(role);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for role tag ");
		}
	}

}
