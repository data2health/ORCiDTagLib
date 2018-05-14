package edu.uiowa.slis.ORCiDTagLib.fundingExternalId;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;
import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibBodyTagSupport;
import edu.uiowa.slis.ORCiDTagLib.funding.Funding;

@SuppressWarnings("serial")

public class FundingExternalIdDeleter extends ORCiDTagLibBodyTagSupport {
    int ID = 0;
    int seqnum = 0;
    int seqnum2 = 0;
    String type = null;
    String value = null;
    String url = null;
	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Funding theFunding = (Funding)findAncestorWithClass(this, Funding.class);
		if (theFunding!= null)
			parentEntities.addElement(theFunding);

		if (theFunding == null) {
		} else {
			ID = theFunding.getID();
			seqnum = theFunding.getSeqnum();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from orcid_dump.funding_external_id where 1=1"
                                                        + (ID == 0 ? "" : " and id = ?")
                                                        + (seqnum == 0 ? "" : " and seqnum = ?")
                                                        + (seqnum2 == 0 ? "" : " and seqnum2 = ?")
                                                        );
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
            if (seqnum2 != 0) stat.setInt(webapp_keySeq++, seqnum2);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating FundingExternalId deleter");
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
        seqnum2 = 0;
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

	public int getSeqnum2 () {
		return seqnum2;
	}

	public void setSeqnum2 (int seqnum2) {
		this.seqnum2 = seqnum2;
	}

	public int getActualSeqnum2 () {
		return seqnum2;
	}
}
