package edu.uiowa.slis.ORCiDTagLib.work;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkCitation extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			if (!theWork.commitNeeded) {
				pageContext.getOut().print(theWork.getCitation());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for citation tag ");
		}
		return SKIP_BODY;
	}

	public String getCitation() throws JspTagException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			return theWork.getCitation();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for citation tag ");
		}
	}

	public void setCitation(String citation) throws JspTagException {
		try {
			Work theWork = (Work)findAncestorWithClass(this, Work.class);
			theWork.setCitation(citation);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Work for citation tag ");
		}
	}

}
