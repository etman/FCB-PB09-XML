package com.steam.odwek;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.ibm.edms.od.ODCriteria;
import com.ibm.edms.od.ODException;
import com.ibm.edms.od.ODFolder;
import com.ibm.edms.od.ODServer;

public class OD7HelperImpl implements ODApi {

	public ODServer getODServer(HttpSession session) throws ODApiException {
		ServletContext ctx = session.getServletContext();
		ODServer odServer = (ODServer) session.getAttribute(KEY_OD_SERVER);

		if (odServer == null) {
			odServer = new ODServer();
			odServer.setApplicationName(ctx.getServletContextName());
			// odServer.setConnectType(ODConstant.CONNECT_TYPE_TCPIP);
			odServer.setServer(ctx.getInitParameter("odserver.host"));
			odServer.setPort(Integer.parseInt(ctx
					.getInitParameter("odserver.port")));
			odServer.setUserId(ctx.getInitParameter("odserver.userId"));
			odServer.setPassword(ctx.getInitParameter("odserver.password"));
			try {
				if (!odServer.isInitialized()) {
					System.out.println("[ODWEK INIT]initialize ODServer.....");
					odServer.initialize(new File(ctx
							.getInitParameter("odserver.config.dir"))
							.getCanonicalPath(), ctx.getServletContextName(),
							950);
					System.out.println("[ODWEK INIT]ODServer initialized.....");
				}
				odServer.logon();
			} catch (Exception e) {
				throw new ODApiException(e);
			}
			ctx.setAttribute(ODServer.class.getName(), odServer);
			return odServer;
		}
		return odServer;
	}

	public ODCriteria addCriteria(ODFolder folder, String indexedField,
			int operand, String searchVal) {
		ODCriteria c = folder.getCriteria(indexedField);
		int[] ops = c.getValidOperands();
		for (int i = 0; i < ops.length; i++)
			System.out.println("ops[" + i + "]=>" + ops[i]);
		try {
			c.setOperand(operand);
			c.setSearchValue(searchVal);
			return c;
		} catch (ODException e) {
			throw new ODApiException(e);
		}
	}

	public ODCriteria addCriteria(ODFolder folder, String indexedField,
			int operand, String searchVal1, String searchVal2) {
		ODCriteria c = folder.getCriteria(indexedField);
		int[] ops = c.getValidOperands();
		for (int i = 0; i < ops.length; i++)
			System.out.println("ops[" + i + "]=>" + ops[i]);
		try {
			c.setOperand(operand);
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
		return folder.getOrSearchCriteria();
	}

	public int getOperator(ODCriteria c) {
		return c.getOperand();
	}

}
