package edu.uiowa.slis.ORCiDTagLib.fundingExternalId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingExternalIdID extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			if (!theFundingExternalId.commitNeeded) {
				pageContext.getOut().print(theFundingExternalId.getID());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			return theFundingExternalId.getID();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			FundingExternalId theFundingExternalId = (FundingExternalId)findAncestorWithClass(this, FundingExternalId.class);
			theFundingExternalId.setID(ID);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing FundingExternalId for ID tag ");
		}
	}

}
