package edu.uiowa.slis.ORCiDTagLib.contributor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ContributorID extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Contributor theContributor = (Contributor)findAncestorWithClass(this, Contributor.class);
			if (!theContributor.commitNeeded) {
				pageContext.getOut().print(theContributor.getID());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Contributor for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			Contributor theContributor = (Contributor)findAncestorWithClass(this, Contributor.class);
			return theContributor.getID();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Contributor for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			Contributor theContributor = (Contributor)findAncestorWithClass(this, Contributor.class);
			theContributor.setID(ID);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Contributor for ID tag ");
		}
	}

}
