package edu.uiowa.slis.ORCiDTagLib.url;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class UrlUrl extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Url theUrl = (Url)findAncestorWithClass(this, Url.class);
			if (!theUrl.commitNeeded) {
				pageContext.getOut().print(theUrl.getUrl());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Url for url tag ");
		}
		return SKIP_BODY;
	}

	public String getUrl() throws JspTagException {
		try {
			Url theUrl = (Url)findAncestorWithClass(this, Url.class);
			return theUrl.getUrl();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Url for url tag ");
		}
	}

	public void setUrl(String url) throws JspTagException {
		try {
			Url theUrl = (Url)findAncestorWithClass(this, Url.class);
			theUrl.setUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Url for url tag ");
		}
	}

}
