package edu.uiowa.slis.ORCiDTagLib.fundingContributor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingContributorSeqnum2 extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			if (!theFundingContributor.commitNeeded) {
				pageContext.getOut().print(theFundingContributor.getSeqnum2());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for seqnum2 tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum2() throws JspTagException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			return theFundingContributor.getSeqnum2();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for seqnum2 tag ");
		}
	}

	public void setSeqnum2(int seqnum2) throws JspTagException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			theFundingContributor.setSeqnum2(seqnum2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for seqnum2 tag ");
		}
	}

}
