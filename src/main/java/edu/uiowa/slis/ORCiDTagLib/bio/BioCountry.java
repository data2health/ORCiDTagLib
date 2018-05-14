package edu.uiowa.slis.ORCiDTagLib.bio;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class BioCountry extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			if (!theBio.commitNeeded) {
				pageContext.getOut().print(theBio.getCountry());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for country tag ");
		}
		return SKIP_BODY;
	}

	public String getCountry() throws JspTagException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			return theBio.getCountry();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for country tag ");
		}
	}

	public void setCountry(String country) throws JspTagException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			theBio.setCountry(country);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for country tag ");
		}
	}

}
