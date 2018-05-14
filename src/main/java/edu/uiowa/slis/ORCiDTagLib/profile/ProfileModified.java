package edu.uiowa.slis.ORCiDTagLib.profile;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ProfileModified extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			if (!theProfile.commitNeeded) {
				pageContext.getOut().print(theProfile.getModified());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for modified tag ");
		}
		return SKIP_BODY;
	}

	public String getModified() throws JspTagException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			return theProfile.getModified();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for modified tag ");
		}
	}

	public void setModified(String modified) throws JspTagException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			theProfile.setModified(modified);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for modified tag ");
		}
	}

}
