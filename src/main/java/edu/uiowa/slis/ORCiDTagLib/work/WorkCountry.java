package edu.uiowa.slis.ORCiDTagLib.work;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkCountry extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			if (!theWork.commitNeeded) {
				pageContext.getOut().print(theWork.getCountry());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for country tag ");
		}
		return SKIP_BODY;
	}

	public String getCountry() throws JspTagException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			return theWork.getCountry();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for country tag ");
		}
	}

	public void setCountry(String country) throws JspTagException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			theWork.setCountry(country);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for country tag ");
		}
	}

}
