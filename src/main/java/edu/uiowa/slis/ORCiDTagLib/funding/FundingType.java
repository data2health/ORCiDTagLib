package edu.uiowa.slis.ORCiDTagLib.funding;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingType extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			if (!theFunding.commitNeeded) {
				pageContext.getOut().print(theFunding.getType());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for type tag ");
		}
		return SKIP_BODY;
	}

	public String getType() throws JspTagException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			return theFunding.getType();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for type tag ");
		}
	}

	public void setType(String type) throws JspTagException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			theFunding.setType(type);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for type tag ");
		}
	}

}
