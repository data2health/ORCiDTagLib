package edu.uiowa.slis.ORCiDTagLib.affiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;

@SuppressWarnings("serial")
public class AffiliationDisambiguationSource extends ORCiDTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			if (!theAffiliation.commitNeeded) {
				pageContext.getOut().print(theAffiliation.getDisambiguationSource());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Affiliation for disambiguationSource tag ");
		}
		return SKIP_BODY;
	}

	public String getDisambiguationSource() throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			return theAffiliation.getDisambiguationSource();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Affiliation for disambiguationSource tag ");
		}
	}

	public void setDisambiguationSource(String disambiguationSource) throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			theAffiliation.setDisambiguationSource(disambiguationSource);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Affiliation for disambiguationSource tag ");
		}
	}

}
