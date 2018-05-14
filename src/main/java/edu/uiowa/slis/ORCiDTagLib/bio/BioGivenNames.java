package edu.uiowa.slis.ORCiDTagLib.bio;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class BioGivenNames extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			if (!theBio.commitNeeded) {
				pageContext.getOut().print(theBio.getGivenNames());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for givenNames tag ");
		}
		return SKIP_BODY;
	}

	public String getGivenNames() throws JspTagException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			return theBio.getGivenNames();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for givenNames tag ");
		}
	}

	public void setGivenNames(String givenNames) throws JspTagException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			theBio.setGivenNames(givenNames);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for givenNames tag ");
		}
	}

}
