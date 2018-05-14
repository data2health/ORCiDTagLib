package edu.uiowa.slis.ORCiDTagLib.work;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkLanguage extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			if (!theWork.commitNeeded) {
				pageContext.getOut().print(theWork.getLanguage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for language tag ");
		}
		return SKIP_BODY;
	}

	public String getLanguage() throws JspTagException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			return theWork.getLanguage();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for language tag ");
		}
	}

	public void setLanguage(String language) throws JspTagException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			theWork.setLanguage(language);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for language tag ");
		}
	}

}
