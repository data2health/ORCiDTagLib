package edu.uiowa.slis.ORCiDTagLib.work;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkType extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			if (!theWork.commitNeeded) {
				pageContext.getOut().print(theWork.getType());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for type tag ");
		}
		return SKIP_BODY;
	}

	public String getType() throws JspTagException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			return theWork.getType();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for type tag ");
		}
	}

	public void setType(String type) throws JspTagException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			theWork.setType(type);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for type tag ");
		}
	}

}
