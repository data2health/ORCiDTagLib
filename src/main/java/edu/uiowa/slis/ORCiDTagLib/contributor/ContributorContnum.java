package edu.uiowa.slis.ORCiDTagLib.contributor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ContributorContnum extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Contributor theContributor = (Contributor)findAncestorWithClass(this, Contributor.class);
			if (!theContributor.commitNeeded) {
				pageContext.getOut().print(theContributor.getContnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Contributor for contnum tag ");
		}
		return SKIP_BODY;
	}

	public int getContnum() throws JspTagException {
		try {
			Contributor theContributor = (Contributor)findAncestorWithClass(this, Contributor.class);
			return theContributor.getContnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Contributor for contnum tag ");
		}
	}

	public void setContnum(int contnum) throws JspTagException {
		try {
			Contributor theContributor = (Contributor)findAncestorWithClass(this, Contributor.class);
			theContributor.setContnum(contnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Contributor for contnum tag ");
		}
	}

}
