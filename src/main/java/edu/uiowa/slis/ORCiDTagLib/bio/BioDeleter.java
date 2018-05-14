package edu.uiowa.slis.ORCiDTagLib.bio;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;
import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibBodyTagSupport;
import edu.uiowa.slis.ORCiDTagLib.profile.Profile;

@SuppressWarnings("serial")

public class BioDeleter extends ORCiDTagLibBodyTagSupport {
    int ID = 0;
    String givenNames = null;
    String familyName = null;
    String creditName = null;
    String biography = null;
    String country = null;
	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Profile theProfile = (Profile)findAncestorWithClass(this, Profile.class);
		if (theProfile!= null)
			parentEntities.addElement(theProfile);

		if (theProfile == null) {
		} else {
			ID = theProfile.getID();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from orcid_dump.bio where 1=1"
                                                        + (ID == 0 ? "" : " and id = ?")
                                                        );
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Bio deleter");
        } finally {
            freeConnection();
        }

        return SKIP_BODY;
    }

	public int doEndTag() throws JspException {
		clearServiceState();
		return super.doEndTag();
	}

    private void clearServiceState() {
        ID = 0;
        parentEntities = new Vector<ORCiDTagLibTagSupport>();

        this.rs = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }



	public int getID () {
		return ID;
	}

	public void setID (int ID) {
		this.ID = ID;
	}

	public int getActualID () {
		return ID;
	}
}
