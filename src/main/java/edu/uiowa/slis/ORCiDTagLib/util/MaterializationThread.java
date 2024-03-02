package edu.uiowa.slis.ORCiDTagLib.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Vector;

import org.apache.log4j.Logger;

public class MaterializationThread implements Runnable {
	static Logger logger = Logger.getLogger(MaterializationThread.class);
	static DecimalFormat formatter = new DecimalFormat("00");

	Connection conn = null;
	MaterializationQueue matQueue = null;
	int threadID = 0;

	public MaterializationThread(int threadID, MaterializationQueue matQueue, Connection conn) throws Exception {
		this.matQueue = matQueue;
		this.threadID = threadID;
		this.conn = conn;
	}

	@Override
	public void run() {
		while (!matQueue.isCompleted()) {
			Vector<MaterializationRequest> theRequestVector = matQueue.dequeue();
			if (theRequestVector == null) {
				return;
			} else {
				for (MaterializationRequest theRequest : theRequestVector) {
					logger.info("[" + formatter.format(threadID) + "] rematerializing " + theRequest.tableName + "...");
					try {
						PreparedStatement stmt = conn.prepareStatement("insert into orcid." + theRequest.tableName + " select " + theRequest.attributeList
								+ " from orcid_staging.staging_" + theRequest.tableName
								+ " natural join orcid_staging.queue");
						int count = stmt.executeUpdate();
						stmt.close();
						logger.info("[" + formatter.format(threadID) + "]\tcount: " + count);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

					if (theRequest.successorQueue != null) {
						matQueue.initialize(theRequest.successorQueue);
						try {
							Loader.respawn();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
