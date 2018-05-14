package edu.uiowa.slis.ORCiDTagLib.fundingContributor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingContributorOrcidId extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			if (!theFundingContributor.commitNeeded) {
				pageContext.getOut().print(theFundingContributor.getOrcidId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for orcidId tag ");
		}
		return SKIP_BODY;
	}

	public String getOrcidId() throws JspTagException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			return theFundingContributor.getOrcidId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for orcidId tag ");
		}
	}

	public void setOrcidId(String orcidId) throws JspTagException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			theFundingContributor.setOrcidId(orcidId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for orcidId tag ");
		}
	}

}
