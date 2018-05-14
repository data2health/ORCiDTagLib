package edu.uiowa.slis.ORCiDTagLib.funding;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingID extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			if (!theFunding.commitNeeded) {
				pageContext.getOut().print(theFunding.getID());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			return theFunding.getID();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			theFunding.setID(ID);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for ID tag ");
		}
	}

}
