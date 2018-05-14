package edu.uiowa.slis.ORCiDTagLib.profile;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ProfileLocale extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			if (!theProfile.commitNeeded) {
				pageContext.getOut().print(theProfile.getLocale());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for locale tag ");
		}
		return SKIP_BODY;
	}

	public String getLocale() throws JspTagException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			return theProfile.getLocale();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for locale tag ");
		}
	}

	public void setLocale(String locale) throws JspTagException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			theProfile.setLocale(locale);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for locale tag ");
		}
	}

}
