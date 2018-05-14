package edu.uiowa.slis.ORCiDTagLib.bio;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class BioBiography extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			if (!theBio.commitNeeded) {
				pageContext.getOut().print(theBio.getBiography());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for biography tag ");
		}
		return SKIP_BODY;
	}

	public String getBiography() throws JspTagException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			return theBio.getBiography();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for biography tag ");
		}
	}

	public void setBiography(String biography) throws JspTagException {
		try {
			Bio theBio = (Bio)findAncestorWithClass(this, Bio.class);
			theBio.setBiography(biography);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Bio for biography tag ");
		}
	}

}
