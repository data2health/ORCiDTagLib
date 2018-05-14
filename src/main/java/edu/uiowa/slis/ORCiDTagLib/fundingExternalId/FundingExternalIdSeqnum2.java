package edu.uiowa.slis.ORCiDTagLib.fundingExternalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingExternalIdSeqnum2 extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			if (!theFundingExternalId.commitNeeded) {
				pageContext.getOut().print(theFundingExternalId.getSeqnum2());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for seqnum2 tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum2() throws JspTagException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			return theFundingExternalId.getSeqnum2();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for seqnum2 tag ");
		}
	}

	public void setSeqnum2(int seqnum2) throws JspTagException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			theFundingExternalId.setSeqnum2(seqnum2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for seqnum2 tag ");
		}
	}

}
