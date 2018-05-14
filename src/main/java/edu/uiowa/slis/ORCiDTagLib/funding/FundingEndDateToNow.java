package edu.uiowa.slis.ORCiDTagLib.funding;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.util.Date;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class FundingEndDateToNow extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			theFunding.setEndDateToNow( );
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for endDate tag ");
		}
		return SKIP_BODY;
	}

	public Date getEndDate() throws JspTagException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			return theFunding.getEndDate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for endDate tag ");
		}
	}

	public void setEndDate( ) throws JspTagException {
		try {
			Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
			theFunding.setEndDateToNow( );
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Funding for endDate tag ");
		}
	}

}
