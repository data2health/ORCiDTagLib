package edu.uiowa.slis.ORCiDTagLib.funding;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingCountry extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			if (!theFunding.commitNeeded) {
				pageContext.getOut().print(theFunding.getCountry());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for country tag ");
		}
		return SKIP_BODY;
	}

	public String getCountry() throws JspTagException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			return theFunding.getCountry();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for country tag ");
		}
	}

	public void setCountry(String country) throws JspTagException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			theFunding.setCountry(country);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for country tag ");
		}
	}

}
