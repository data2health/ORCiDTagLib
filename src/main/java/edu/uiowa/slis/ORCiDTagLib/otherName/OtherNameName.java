package edu.uiowa.slis.ORCiDTagLib.otherName;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class OtherNameName extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			OtherName theOtherName = (OtherName)findAncestorWithClass(this, OtherName.class);
			if (!theOtherName.commitNeeded) {
				pageContext.getOut().print(theOtherName.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherName for name tag ");
		}
		return SKIP_BODY;
	}

	public String getName() throws JspTagException {
		try {
			OtherName theOtherName = (OtherName)findAncestorWithClass(this, OtherName.class);
			return theOtherName.getName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherName for name tag ");
		}
	}

	public void setName(String name) throws JspTagException {
		try {
			OtherName theOtherName = (OtherName)findAncestorWithClass(this, OtherName.class);
			theOtherName.setName(name);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherName for name tag ");
		}
	}

}
