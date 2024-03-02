package edu.uiowa.slis.ORCiDTagLib.util;

public class MaterializationRequest {
	String tableName = null;
	String attributeList = null;
	String successorQueue = null;

	public MaterializationRequest(String tableName, String attributeList, String successorQueue) {
		this.tableName = tableName;
		this.attributeList = attributeList;
		this.successorQueue = successorQueue;
	}
	
	public String toString() {
		return tableName + ": " + attributeList + " : " + successorQueue;
	}
}
