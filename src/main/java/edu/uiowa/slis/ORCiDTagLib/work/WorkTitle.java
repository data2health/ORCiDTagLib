package edu.uiowa.slis.ORCiDTagLib.work;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkTitle extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			if (!theWork.commitNeeded) {
				pageContext.getOut().print(theWork.getTitle());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for title tag ");
		}
		return SKIP_BODY;
	}

	public String getTitle() throws JspTagException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			return theWork.getTitle();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for title tag ");
		}
	}

	public void setTitle(String title) throws JspTagException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			theWork.setTitle(title);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for title tag ");
		}
	}

}
