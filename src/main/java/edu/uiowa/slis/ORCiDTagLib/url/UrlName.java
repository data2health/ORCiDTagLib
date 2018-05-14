package edu.uiowa.slis.ORCiDTagLib.url;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class UrlName extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Url theUrl = (Url)findAncestorWithClass(this, Url.class);
			if (!theUrl.commitNeeded) {
				pageContext.getOut().print(theUrl.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Url for name tag ");
		}
		return SKIP_BODY;
	}

	public String getName() throws JspTagException {
		try {
			Url theUrl = (Url)findAncestorWithClass(this, Url.class);
			return theUrl.getName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Url for name tag ");
		}
	}

	public void setName(String name) throws JspTagException {
		try {
			Url theUrl = (Url)findAncestorWithClass(this, Url.class);
			theUrl.setName(name);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Url for name tag ");
		}
	}

}
