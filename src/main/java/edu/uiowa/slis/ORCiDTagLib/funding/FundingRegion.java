package edu.uiowa.slis.ORCiDTagLib.funding;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingRegion extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			if (!theFunding.commitNeeded) {
				pageContext.getOut().print(theFunding.getRegion());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for region tag ");
		}
		return SKIP_BODY;
	}

	public String getRegion() throws JspTagException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			return theFunding.getRegion();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for region tag ");
		}
	}

	public void setRegion(String region) throws JspTagException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			theFunding.setRegion(region);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for region tag ");
		}
	}

}
