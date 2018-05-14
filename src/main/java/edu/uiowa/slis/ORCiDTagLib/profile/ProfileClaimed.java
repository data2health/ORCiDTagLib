package edu.uiowa.slis.ORCiDTagLib.profile;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ProfileClaimed extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			if (!theProfile.commitNeeded) {
				pageContext.getOut().print(theProfile.getClaimed());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for claimed tag ");
		}
		return SKIP_BODY;
	}

	public boolean getClaimed() throws JspTagException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			return theProfile.getClaimed();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for claimed tag ");
		}
	}

	public void setClaimed(boolean claimed) throws JspTagException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			theProfile.setClaimed(claimed);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for claimed tag ");
		}
	}

}
