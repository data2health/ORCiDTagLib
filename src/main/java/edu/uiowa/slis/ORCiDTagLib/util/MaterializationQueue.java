package edu.uiowa.slis.ORCiDTagLib.util;

import java.util.Vector;

public class MaterializationQueue {
	int capacity = Integer.MAX_VALUE;
	Vector<Vector<MaterializationRequest>> theQueue = new Vector<Vector<MaterializationRequest>>();
	boolean completed = false;

	public MaterializationQueue() {
		initialize("initial");
	}

	public synchronized boolean atCapacity() {
		return theQueue.size() >= capacity;
	}

	public synchronized void queue(Vector<MaterializationRequest> queueEntry) {
		theQueue.add(queueEntry);
		completed = false;
	}

	public void completed() {
		completed = true;
	}

	public synchronized boolean isCompleted() {
		return theQueue.size() == 0 && completed;
	}

	public synchronized Vector<MaterializationRequest> dequeue() {
		if (theQueue.size() == 0)
			return null;
		else
			return theQueue.remove(0);
	}

//person
//	other_name
//	biography
//	researcher_url
//	email
//	address
//	keyword
//	external_identifier
//	education
//	employment
//	funding
//		funding_external_id
//	work
//		work_external_id
//	history

	void initialize(String selection) {
		Vector<MaterializationRequest> theRequestList = null;
		
		// build chains of materialization dependencies - the initial group needs to be done first to maximize
		// parallelism in the subsequent groups

		switch (selection) {
		case "initial":
			theRequestList = new Vector<MaterializationRequest>();
			theRequestList.add(new MaterializationRequest("person", "id,orcid_id,given_names,family_name,credit_name", "person"));
			queue(theRequestList);

			break;
		case "person":
			theRequestList = new Vector<MaterializationRequest>();
			theRequestList.add(new MaterializationRequest("other_name", "id,orcid_id,seqnum,other_name", null));
			queue(theRequestList);

			theRequestList = new Vector<MaterializationRequest>();
			theRequestList.add(new MaterializationRequest("biography", "id,orcid_id,seqnum,biography", null));
			queue(theRequestList);

			theRequestList = new Vector<MaterializationRequest>();
			theRequestList.add(new MaterializationRequest("researcher_url", "id,orcid_id,seqnum,url,name", null));
			queue(theRequestList);

			theRequestList = new Vector<MaterializationRequest>();
			theRequestList.add(new MaterializationRequest("email", "id,orcid_id,seqnum,email", null));
			queue(theRequestList);

			theRequestList = new Vector<MaterializationRequest>();
			theRequestList.add(new MaterializationRequest("address", "id,orcid_id,seqnum,country", null));
			queue(theRequestList);

			theRequestList = new Vector<MaterializationRequest>();
			theRequestList.add(new MaterializationRequest("keyword", "id,orcid_id,seqnum,keyword", null));
			queue(theRequestList);

			theRequestList = new Vector<MaterializationRequest>();
			theRequestList.add(new MaterializationRequest("external_identifier", "id,orcid_id,seqnum,type,value,url,relationship", null));
			queue(theRequestList);

			theRequestList = new Vector<MaterializationRequest>();
			theRequestList.add(new MaterializationRequest("education", "id,orcid_id,seqnum,department,title,start_year,start_month,start_day,end_year,end_month,end_day,organization,city,region,country,org_id,id_source", null));
			queue(theRequestList);

			theRequestList = new Vector<MaterializationRequest>();
			theRequestList.add(new MaterializationRequest("employment", "id,orcid_id,seqnum,department,title,start_year,start_month,start_day,end_year,end_month,end_day,organization,city,region,country,org_id,id_source", null));
			queue(theRequestList);

			theRequestList = new Vector<MaterializationRequest>();
			theRequestList.add(new MaterializationRequest("funding", "id,orcid_id,seqnum,title,translated_title,type,start_year,start_month,start_day,end_year,end_month,end_day,organization,city,region,country,org_id,id_source", null));
			theRequestList.add(new MaterializationRequest("funding_external_id", "id,orcid_id,seqnum,seqnum2,type,value,relationship", null));
			queue(theRequestList);

			theRequestList = new Vector<MaterializationRequest>();
			theRequestList.add(new MaterializationRequest("work", "id,orcid_id,seqnum,title,type,pub_year,pub_month,pub_day", null));
			theRequestList.add(new MaterializationRequest("work_external_id", "id,orcid_id,seqnum,seqnum2,type,value,relationship", null));
			queue(theRequestList);

			theRequestList = new Vector<MaterializationRequest>();
			theRequestList.add(new MaterializationRequest("history", "id,orcid_id,creation_method,submission_date,last_modified,claimed,verified_email,verified_primary_email", null));
			queue(theRequestList);

			break;
		}

	}

}
