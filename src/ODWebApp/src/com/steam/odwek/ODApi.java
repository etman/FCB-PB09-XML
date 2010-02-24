package com.steam.odwek;

import javax.servlet.http.HttpSession;

import com.ibm.edms.od.ODCriteria;
import com.ibm.edms.od.ODFolder;
import com.ibm.edms.od.ODServer;

public interface ODApi {
	public final static String KEY_OD_SERVER = ODServer.class.getName();

	ODServer getODServer(HttpSession session) throws ODApiException;

	ODCriteria addCriteria(ODFolder folder, String indexedField, int operand,
			String searchVal) throws ODApiException;

	ODCriteria addCriteria(ODFolder folder, String indexedField, int operand,
			String searchVal1, String searchVal2) throws ODApiException;

	void release(HttpSession session) throws ODApiException;

	boolean isOrSearchCriteria(ODFolder folder) throws ODApiException;

	int getOperator(ODCriteria c) throws ODApiException;

}
