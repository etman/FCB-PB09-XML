package com.steam.odwek;

import java.io.File;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.ibm.edms.od.ODConstant;
import com.ibm.edms.od.ODCriteria;
import com.ibm.edms.od.ODException;
import com.ibm.edms.od.ODFolder;
import com.ibm.edms.od.ODServer;

public class ODHelper {
	private final static String KEY_OD_SERVER = ODServer.class.getName();

	public static void transform(byte[] xmlBytes, String xslPath, Writer out)
			throws UnsupportedEncodingException, TransformerException {
		transform(new String(xmlBytes, "MS950"), xslPath, out);
	}

	public static void transform(String xml, String xslPath, Writer out)
			throws UnsupportedEncodingException, TransformerException {
		Source xmlSource = new StreamSource(new StringReader(xml));
		Source xsltSource = new StreamSource(new File(xslPath));
		Result result = new StreamResult(out);

		// create an instance of TransformerFactory
		TransformerFactory transFact = TransformerFactory.newInstance();
		Transformer trans = transFact.newTransformer(xsltSource);
		trans.transform(xmlSource, result);
	}

	public synchronized static ODServer getODServer(HttpSession session)
			throws Exception {
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
			if (!odServer.isInitialized()) {
				System.out.println("[ODWEK INIT]initialize ODServer.....");
				odServer.initialize(new File(ctx
						.getInitParameter("odserver.config.dir"))
						.getCanonicalPath(), ctx.getServletContextName(), 950);
				System.out.println("[ODWEK INIT]ODServer initialized.....");
			}
			odServer.logon();
			ctx.setAttribute(ODServer.class.getName(), odServer);
			return odServer;
		}
		return odServer;
	}

	public static ODFolder getFolder(HttpSession session) {
		for (int i = 0; i < 3; i++) {
			try {
				synchronized (session) {
					ServletContext ctx = session.getServletContext();
					return getODServer(session).openFolder(
							ctx.getInitParameter("working.odfolder"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				release(session);
			}
		}
		throw new IllegalStateException(
				"Could not get folder...check stdout log.");
	}

	public static ODCriteria addCriteria(ODFolder folder, String indexedField,
			int operand, String searchVal) throws ODException {
		ODCriteria c = folder.getCriteria(indexedField);
		int[] ops = c.getValidOperands();
		for (int i = 0; i < ops.length; i++)
			System.out.println("ops[" + i + "]=>" + ops[i]);
		c.setOperand(operand);
		c.setSearchValue(searchVal);
		return c;
	}

	public static ODCriteria addCriteria(ODFolder folder, String indexedField,
			int operand, String searchVal1, String searchVal2)
			throws ODException {
		ODCriteria c = folder.getCriteria(indexedField);
		int[] ops = c.getValidOperands();
		for (int i = 0; i < ops.length; i++)
			System.out.println("ops[" + i + "]=>" + ops[i]);
		c.setOperand(operand);
		c.setSearchValues(searchVal1, searchVal2);
		return c;
	}

	public static void addDateRange(ODFolder folder, String criteriaName,
			String startDate, String endDate, String legalPattern)
			throws ODException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			Date d1 = null;
			if (startDate != null && !startDate.trim().equals("")) {
				d1 = sdf.parse(startDate);
			} else {
				d1 = sdf.parse("19700101");
			}
			Date d2 = null;
			if (endDate != null && !endDate.trim().equals("")) {
				d2 = sdf.parse(endDate);
			} else {
				d2 = sdf.parse("20590917");
			}
			sdf.applyPattern(legalPattern);
			addCriteria(folder, criteriaName, ODConstant.OPBetween, sdf
					.format(d1), sdf.format(d2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void release(HttpSession session) {
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

	public static String convOP2Symbol(int op) {
		switch (op) {
		case ODConstant.OPEqual:
			return "=";
		case ODConstant.OPBetween:
			return "between";
		case ODConstant.OPGreaterThan:
			return ">";
		case ODConstant.OPGreaterThanEqual:
			return ">=";
		case ODConstant.OPLessThan:
			return "<";
		case ODConstant.OPLessThanEqual:
			return "<=";
		case ODConstant.OPLike:
			return "like";
		default:
			return String.valueOf(op);
		}
	}
}
