package edu.uiowa.slis.ORCiDTagLib.url;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class UrlSeqnum extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Url theUrl = (Url)findAncestorWithClass(this, Url.class);
			if (!theUrl.commitNeeded) {
				pageContext.getOut().print(theUrl.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Url for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			Url theUrl = (Url)findAncestorWithClass(this, Url.class);
			return theUrl.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Url for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			Url theUrl = (Url)findAncestorWithClass(this, Url.class);
			theUrl.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Url for seqnum tag ");
		}
	}

}
