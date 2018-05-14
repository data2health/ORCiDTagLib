package edu.uiowa.slis.ORCiDTagLib.funding;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;
import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibBodyTagSupport;
import edu.uiowa.slis.ORCiDTagLib.profile.Profile;

@SuppressWarnings("serial")

public class FundingDeleter extends ORCiDTagLibBodyTagSupport {
    int ID = 0;
    int seqnum = 0;
    String type = null;
    String orgType = null;
    String title = null;
    String translatedTitle = null;
    String shortDescription = null;
    String amount = null;
    String currency = null;
    String url = null;
    Date startDate = null;
    Date endDate = null;
    String organization = null;
    String city = null;
    String region = null;
    String country = null;
    String disambiguationId = null;
    String disambiguationSource = null;
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
            stat = getConnection().prepareStatement("DELETE from orcid_dump.funding where 1=1"
                                                        + (ID == 0 ? "" : " and id = ?")
                                                        + (seqnum == 0 ? "" : " and seqnum = ?")
                                                        );
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Funding deleter");
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
        seqnum = 0;
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

	public int getSeqnum () {
		return seqnum;
	}

	public void setSeqnum (int seqnum) {
		this.seqnum = seqnum;
	}

	public int getActualSeqnum () {
		return seqnum;
	}
}
