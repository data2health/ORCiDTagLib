package edu.uiowa.slis.ORCiDTagLib.profile;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ProfileMethod extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			if (!theProfile.commitNeeded) {
				pageContext.getOut().print(theProfile.getMethod());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for method tag ");
		}
		return SKIP_BODY;
	}

	public String getMethod() throws JspTagException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			return theProfile.getMethod();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for method tag ");
		}
	}

	public void setMethod(String method) throws JspTagException {
		try {
			Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
			theProfile.setMethod(method);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Profile for method tag ");
		}
	}

}
