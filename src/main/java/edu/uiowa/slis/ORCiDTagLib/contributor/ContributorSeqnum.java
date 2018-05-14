package edu.uiowa.slis.ORCiDTagLib.contributor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class ContributorSeqnum extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Contributor theContributor = (Contributor)findAncestorWithClass(this, Contributor.class);
			if (!theContributor.commitNeeded) {
				pageContext.getOut().print(theContributor.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Contributor for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			Contributor theContributor = (Contributor)findAncestorWithClass(this, Contributor.class);
			return theContributor.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Contributor for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			Contributor theContributor = (Contributor)findAncestorWithClass(this, Contributor.class);
			theContributor.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Contributor for seqnum tag ");
		}
	}

}
