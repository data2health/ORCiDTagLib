package edu.uiowa.slis.ORCiDTagLib.contributor;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibTagSupport;
import edu.uiowa.slis.ORCiDTagLib.ORCiDTagLibBodyTagSupport;
import edu.uiowa.slis.ORCiDTagLib.work.Work;

@SuppressWarnings("serial")

public class ContributorDeleter extends ORCiDTagLibBodyTagSupport {
    int ID = 0;
    int seqnum = 0;
    int contnum = 0;
    String name = null;
	Vector<ORCiDTagLibTagSupport> parentEntities = new Vector<ORCiDTagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Work theWork = (Work)findAncestorWithClass(this, Work.class);
		if (theWork!= null)
			parentEntities.addElement(theWork);

		if (theWork == null) {
		} else {
			ID = theWork.getID();
			seqnum = theWork.getSeqnum();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from orcid_dump.contributor where 1=1"
                                                        + (ID == 0 ? "" : " and id = ?")
                                                        + (seqnum == 0 ? "" : " and seqnum = ?")
                                                        + (contnum == 0 ? "" : " and contnum = ?")
                                                        );
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
            if (contnum != 0) stat.setInt(webapp_keySeq++, contnum);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Contributor deleter");
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
        contnum = 0;
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

	public int getContnum () {
		return contnum;
	}

	public void setContnum (int contnum) {
		this.contnum = contnum;
	}

	public int getActualContnum () {
		return contnum;
	}
}
