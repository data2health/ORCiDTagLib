package edu.uiowa.slis.ORCiDTagLib.profile;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ProfilePath extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			if (!theProfile.commitNeeded) {
				pageContext.getOut().print(theProfile.getPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for path tag ");
		}
		return SKIP_BODY;
	}

	public String getPath() throws JspTagException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			return theProfile.getPath();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for path tag ");
		}
	}

	public void setPath(String path) throws JspTagException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			theProfile.setPath(path);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for path tag ");
		}
	}

}
