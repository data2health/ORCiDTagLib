package edu.uiowa.slis.ORCiDTagLib.contributor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ContributorName extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Contributor theContributor = (Contributor)findAncestorWithClass(this, Contributor.class);
			if (!theContributor.commitNeeded) {
				pageContext.getOut().print(theContributor.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Contributor for name tag ");
		}
		return SKIP_BODY;
	}

	public String getName() throws JspTagException {
		try {
			Contributor theContributor = (Contributor)findAncestorWithClass(this, Contributor.class);
			return theContributor.getName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Contributor for name tag ");
		}
	}

	public void setName(String name) throws JspTagException {
		try {
			Contributor theContributor = (Contributor)findAncestorWithClass(this, Contributor.class);
			theContributor.setName(name);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Contributor for name tag ");
		}
	}

}
