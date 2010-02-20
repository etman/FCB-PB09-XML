<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ page contentType="text/html;charset=utf-8"
	import="com.ibm.edms.od.*,com.steam.odwek.ODHelper,java.io.*,javax.xml.transform.*,javax.xml.transform.stream.*,java.text.*,java.util.*"%>

<%
	String docId = request.getParameter("docId");

	byte[] xml = null;
	synchronized (ODHelper.class) {
		ODFolder folder = null;
		try {
			folder = ODHelper.getFolder(session);
			ODHit hit = folder.recreateHit(docId);
			if (hit == null) {
				//error handling
			}
			xml = hit.retrieve(null);
		} finally {
			if (folder != null)
				folder.close();
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<%
	if (xml != null && xml.length > 0) {
		ODHelper.transform(xml, application
				.getRealPath("pb09-disp.xsl"), out);
	}
%>

</body>
</html>