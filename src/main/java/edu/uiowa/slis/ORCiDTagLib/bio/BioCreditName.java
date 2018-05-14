package edu.uiowa.slis.ORCiDTagLib.bio;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class BioCreditName extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			if (!theBio.commitNeeded) {
				pageContext.getOut().print(theBio.getCreditName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for creditName tag ");
		}
		return SKIP_BODY;
	}

	public String getCreditName() throws JspTagException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			return theBio.getCreditName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for creditName tag ");
		}
	}

	public void setCreditName(String creditName) throws JspTagException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			theBio.setCreditName(creditName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for creditName tag ");
		}
	}

}
