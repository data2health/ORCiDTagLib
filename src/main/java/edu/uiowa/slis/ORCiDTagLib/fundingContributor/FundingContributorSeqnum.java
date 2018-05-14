package edu.uiowa.slis.ORCiDTagLib.fundingContributor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingContributorSeqnum extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			if (!theFundingContributor.commitNeeded) {
				pageContext.getOut().print(theFundingContributor.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			return theFundingContributor.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			FundingContributor theFundingContributor = (FundingContributor)findAncestorWithClass(this, FundingContributor.class);
			theFundingContributor.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingContributor for seqnum tag ");
		}
	}

}
