package com.steam.odwek;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.ibm.edms.od.ODConfig;
import com.ibm.edms.od.ODConstant;
import com.ibm.edms.od.ODCriteria;
import com.ibm.edms.od.ODException;
import com.ibm.edms.od.ODFolder;
import com.ibm.edms.od.ODServer;

class OD8HelperImpl implements ODApi {

	public ODServer getODServer(HttpSession session) throws ODApiException {
		ODServer odServer = (ODServer) session.getAttribute(KEY_OD_SERVER);
		try {
			if (odServer == null) {
				ServletContext ctx = session.getServletContext();
				ODConfig cfg = new ODConfig(ODConstant.PLUGIN, // AfpViewer
						ODConstant.APPLET, // LineViewer
						null, // MetaViewer
						500, // MaxHits
						"c:\\applets", // AppletDir
						"CHT", // Language
						"c:\\temp", // TempDir
						"c:\\temp\\trace", // TraceDir
						1); // TraceLevel
				odServer = new ODServer(cfg);
				odServer.setPort(Integer.parseInt(ctx
						.getInitParameter("odserver.port")));
				if (!odServer.isInitialized()) {
					System.out.println("[ODWEK INIT]initialize ODServer.....");
					odServer.initialize(ctx.getServletContextName());
					System.out.println("[ODWEK INIT]ODServer initialized.....");
				}
				odServer.logon(ctx.getInitParameter("odserver.host"), ctx
						.getInitParameter("odserver.userId"), ctx
						.getInitParameter("odserver.password"));
				session.setAttribute(ODServer.class.getName(), odServer);
				return odServer;
			}
		} catch (Exception e) {
			throw new ODApiException(e);
		}
		return odServer;
	}

	public ODCriteria addCriteria(ODFolder folder, String indexedField,
			int operand, String searchVal) {
		ODCriteria c = folder.getCriteria(indexedField);
		int[] ops = c.getValidOperators();
		for (int i = 0; i < ops.length; i++)
			System.out.println("ops[" + i + "]=>" + ops[i]);
		try {
			c.setOperator(operand);
			c.setSearchValue(searchVal);
			return c;
		} catch (ODException e) {
			throw new ODApiException(e);
		}
	}

	public ODCriteria addCriteria(ODFolder folder, String indexedField,
			int operand, String searchVal1, String searchVal2) {
		ODCriteria c = folder.getCriteria(indexedField);
		int[] ops = c.getValidOperators();
		for (int i = 0; i < ops.length; i++)
			System.out.println("ops[" + i + "]=>" + ops[i]);
		try {
			c.setOperator(operand);
			c.setSearchValues(searchVal1, searchVal2);
			return c;
		} catch (ODException e) {
			throw new ODApiException(e);
		}
	}

	public void release(HttpSession session) {
		try {
			synchronized (session) {
				ODServer odServer = (ODServer) getODServer(session);
				if (odServer != null) {
					try {
						odServer.logoff();
						session.removeAttribute(KEY_OD_SERVER);
					} finally {
						odServer.terminate();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.removeAttribute(KEY_OD_SERVER);
		}
	}

	public boolean isOrSearchCriteria(ODFolder folder) {
		return folder.isOrSearchCriteria();
	}

	public int getOperator(ODCriteria c) {
		return c.getOperator();
	}

}
