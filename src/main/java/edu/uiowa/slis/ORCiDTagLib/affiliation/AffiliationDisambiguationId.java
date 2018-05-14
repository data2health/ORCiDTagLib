package edu.uiowa.slis.ORCiDTagLib.affiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class AffiliationDisambiguationId extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			if (!theAffiliation.commitNeeded) {
				pageContext.getOut().print(theAffiliation.getDisambiguationId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Affiliation for disambiguationId tag ");
		}
		return SKIP_BODY;
	}

	public String getDisambiguationId() throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			return theAffiliation.getDisambiguationId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Affiliation for disambiguationId tag ");
		}
	}

	public void setDisambiguationId(String disambiguationId) throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			theAffiliation.setDisambiguationId(disambiguationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Affiliation for disambiguationId tag ");
		}
	}

}
