package edu.uiowa.slis.ORCiDTagLib.bio;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class BioFamilyName extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			if (!theBio.commitNeeded) {
				pageContext.getOut().print(theBio.getFamilyName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for familyName tag ");
		}
		return SKIP_BODY;
	}

	public String getFamilyName() throws JspTagException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			return theBio.getFamilyName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for familyName tag ");
		}
	}

	public void setFamilyName(String familyName) throws JspTagException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			theBio.setFamilyName(familyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for familyName tag ");
		}
	}

}
