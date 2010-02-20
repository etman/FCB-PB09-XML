<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ page contentType="text/html;charset=utf-8" import="java.io.*, javax.xml.transform.*, javax.xml.transform.stream.*, java.util.*"%>
<%!
	private static void transform(String workingDir, File xml, Writer out)
			throws UnsupportedEncodingException {
		try {
			Source xmlSource = new StreamSource(xml);
			Source xsltSource = new StreamSource(new File(workingDir, "pb09-disp.xsl"));
			Result result = new StreamResult(out);
			
			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer trans = transFact.newTransformer(xsltSource);
			trans.transform(xmlSource, result);
		} catch (TransformerException e) {
			throw new IllegalStateException(xml.getPath(), e);
		}
	}
%>
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  </head>
  <body>
<%
	int pageNo = 0;
	try{
	  pageNo = Integer.parseInt(request.getParameter("pageNo"));
	  out.println("目前頁次: "+(pageNo+1));
	}catch(Exception e){}
	 
	String rPath = session.getServletContext().getRealPath("");
	List fileList = (List) session.getAttribute("fileList");
	if(fileList!=null && ! fileList.isEmpty()){
	%>
		&nbsp;&nbsp;&nbsp;&nbsp;頁數: 
		<% for(int i = 0 ;i < fileList.size();i++){ %>
		      <a href="result.jsp?pageNo=<%=i%>"><%=i+1%></a>
		<% } %>
	<%
		transform(rPath, (File)fileList.get(pageNo), out);
	}
%>
  </body>
</html>