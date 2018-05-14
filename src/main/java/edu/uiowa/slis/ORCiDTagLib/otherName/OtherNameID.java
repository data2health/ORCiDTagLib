package edu.uiowa.slis.ORCiDTagLib.otherName;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class OtherNameID extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			OtherName theOtherName = (OtherName)findAncestorWithClass(this, OtherName.class);
			if (!theOtherName.commitNeeded) {
				pageContext.getOut().print(theOtherName.getID());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherName for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			OtherName theOtherName = (OtherName)findAncestorWithClass(this, OtherName.class);
			return theOtherName.getID();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherName for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			OtherName theOtherName = (OtherName)findAncestorWithClass(this, OtherName.class);
			theOtherName.setID(ID);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherName for ID tag ");
		}
	}

}
