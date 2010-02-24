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
	private static volatile ODApi odApi = null;

	protected ODHelper() {

	}

	public static boolean isOrSearchCriteria(ODFolder folder) {
		return odApi.isOrSearchCriteria(folder);
	}

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

	public static ODServer getODServer(HttpSession session) throws Exception {
		ServletContext ctx = session.getServletContext();
		if (odApi == null) {
			synchronized (ODHelper.class) {
				if (odApi == null) {
					String ver = ctx.getInitParameter("odwek.version");
					int version = 0;
					try {
						version = Integer.parseInt(ver);
					} catch (Exception e) {
					}
					switch (version) {
					default:
					case 8:
						try {
							Class.forName("com.ibm.edms.od.ODConfig");
							odApi = new OD8HelperImpl();
							break;
						} catch (Exception e) {
						}
					case 7:
						odApi = new OD7HelperImpl();
						break;
					}
				}
			}
		}
		synchronized (session) {
			return odApi.getODServer(session);
		}
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
		return odApi.addCriteria(folder, indexedField, operand, searchVal);
	}

	public static ODCriteria addCriteria(ODFolder folder, String indexedField,
			int operand, String searchVal1, String searchVal2)
			throws ODException {
		return odApi.addCriteria(folder, indexedField, operand, searchVal1,
				searchVal2);
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
		odApi.release(session);
	}

	public static String convOP2Symbol(ODCriteria c) {
		int op = odApi.getOperator(c);
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
