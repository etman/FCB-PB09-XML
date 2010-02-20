<%@ page pageEncoding="big5" contentType="text/html;charset=big5"%>
<%@ page
	import="com.ibm.edms.od.*,com.steam.odwek.*,java.nio.charset.*,java.io.*,javax.xml.transform.*,javax.xml.transform.stream.*,java.text.*,java.util.*"%>
<%!private int[] operands = new int[] {};

	private static boolean isValid(String type, String val) {
		return (type != null && !"".equals(type.trim()) && val != null && !""
				.equals(val.trim()));
	}%>
<%
	request.setCharacterEncoding("big5");
	//String rNameOp = request.getParameter("R_NAME_AssignOP");
	//sString bIdOp = request.getParameter("B_ID_AssignOP");
	String rIdOp = request.getParameter("R_ID_AssignOP");
	String acctOp = request.getParameter("ACCOUNT_AssignOP");
	String acctNumberOp = request
			.getParameter("ACCOUNT_NUMBER_AssignOP");

	//String rNameVal = request.getParameter("R_NAME_Value");
	//String bIdVal = request.getParameter("B_ID_Value");
	String rIdVal = request.getParameter("R_ID_Value");
	String acctVal = request.getParameter("ACCOUNT_Value");
	String acctNumberVal = request.getParameter("ACCOUNT_NUMBER_Value");

	//String[] types = new String[] { "報表代號", "單位名稱", "報表名稱", "會計科目",
	//		"帳號" };
	//String[] ops = new String[] { rNameOp, bIdOp, rIdOp, acctOp,
	//		acctNumberOp };
	//String[] values = new String[] { rNameVal, bIdVal, rIdVal, acctVal,
	//		acctNumberVal };
	String[] types = new String[] { "報表代號", "會計科目", "帳號" };
	String[] ops = new String[] { rIdOp, acctOp, acctNumberOp };
	String[] values = new String[] { rIdVal, acctVal, acctNumberVal };
	String startDate = request.getParameter("start_date");
	String endDate = request.getParameter("end_date");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5" />
<script language="javascript">
		function createDynaField(val){
			var rangeBlock = document.getElementById("criteriaRangeBlock");
			var valBlock = document.getElementById("criteriaValueBlock");
			if('S_DATE' == val || 'E_DATE' == val){
			  rangeBlock.style.display = 'inline';
			  valBlock.style.display = 'none';
			} else {
			  rangeBlock.style.display = 'none';
			  valBlock.style.display = 'inline';
			}
		}
		
		function openXmlViewer(docId){
			window.open('xmlViewer.jsp?docId='+docId);
		}
	</script>
</head>
<body>
<form method="post" action="index.jsp">查詢條件:
<table>
	<tr>
		<td>報表代號</td>
		<td><select name="R_ID_AssignOP">
			<option value="<%=ODConstant.OPEqual%>"
				<%=(String.valueOf(ODConstant.OPEqual).equals(rIdOp) ? " selected"
									: "")%>>等於</option>
			<option value="<%=ODConstant.OPLike%>"
				<%=(String.valueOf(ODConstant.OPLike).equals(rIdOp) ? " selected"
									: "")%>>相似</option>
		</select> <input type="text" name="R_ID_Value"
			value="<%=(rIdVal != null) ? rIdVal : ""%>"></td>
	</tr>
	<tr>
		<td>會計科目代號</td>
		<td><select name="ACCOUNT_AssignOP">
			<option value="<%=ODConstant.OPEqual%>"
				<%=(String.valueOf(ODConstant.OPEqual)
									.equals(acctOp) ? " selected" : "")%>>等於</option>
			<option value="<%=ODConstant.OPLike%>"
				<%=(String.valueOf(ODConstant.OPLike).equals(acctOp) ? " selected"
									: "")%>>相似</option>
		</select> <input type="text" name="ACCOUNT_Value"
			value="<%=(acctVal != null) ? acctVal : ""%>"></td>
	</tr>
	<tr>
		<td>帳號</td>
		<td><select name="ACCOUNT_NUMBER_AssignOP">
			<option value="<%=ODConstant.OPEqual%>"
				<%=(String.valueOf(ODConstant.OPEqual).equals(
									acctNumberOp) ? " selected" : "")%>>等於</option>
			<option value="<%=ODConstant.OPLike%>"
				<%=(String.valueOf(ODConstant.OPLike).equals(
									acctNumberOp) ? " selected" : "")%>>相似</option>
		</select> <input type="text" name="ACCOUNT_NUMBER_Value"
			value="<%=(acctNumberVal != null) ? acctNumberVal : ""%>"></td>
	</tr>
	<tr>
		<td>報表起迄日</td>
		<td><input type="text" name="start_date"
			value="<%=(startDate != null) ? startDate : ""%>"> 介於 <input
			type="text" name="end_date"
			value="<%=(endDate != null) ? endDate : ""%>">(ex:yyyyMMdd)</td>
	</tr>
	<tr>
		<td>準則邏輯</td>
		<td><input type="radio" name="logicGate" value="and"
			<%=(!"or".equals(request.getParameter("logicGate"))) ? "checked"
							: ""%> />和<input
			type="radio" name="logicGate" value="or"
			<%=("or".equals(request.getParameter("logicGate"))) ? "checked"
							: ""%> />或</td>
	</tr>
</table>

<input type="submit" value="搜尋" /></form>

<%
	String hitsPage = request.getParameter("hitsPage");
	ODHitsPager hitsPager = (ODHitsPager) session
			.getAttribute(ODHitsPager.class.getName());
	boolean noQueryNeeded = false;
	if (hitsPager != null) {
		noQueryNeeded = hitsPager.setCurrentPage(hitsPage);
	}
	String[] displayOrder = null;
	synchronized (session) {
		ODFolder folder = null;
		try {
			folder = ODHelper.getFolder(session);
			displayOrder = folder.getDisplayOrder();

			if (!noQueryNeeded) {
				folder.setOrSearchCriteria("or".equals(request
						.getParameter("logicGate")));
				boolean hasValueSet = false;
				for (int i = 0; i < types.length; i++) {
					if (isValid(ops[i], values[i])) {
						if (hasValueSet)
							out
									.print((folder
											.getOrSearchCriteria() ? " 或 "
											: " 和 "));
						hasValueSet = true;

						ODCriteria c = ODHelper.addCriteria(folder,
								types[i], Integer.parseInt(ops[i]),
								values[i]);
						out.println(c.getName()
								+ " "
								+ ODHelper
										.convOP2Symbol(c.getOperand())
								+ " " + c.getSearchValues()[0]);
					}
				}
				if (startDate != null && !startDate.trim().equals("")
						&& endDate != null
						&& !endDate.trim().equals("")) {
					hasValueSet = true;
					ODHelper.addDateRange(folder, "開始日期", startDate,
							endDate, "MM/dd/yy");
					ODHelper.addDateRange(folder, "結束日期", startDate,
							endDate, "MM/dd/yy");
%>
<br />
報表起迄:<%=startDate%>~<%=endDate%>
<%
	}
				if (hasValueSet)
					folder.search();

				hitsPager = new ODHitsPager((Vector) folder.getHits()
						.clone());
				session.setAttribute(ODHitsPager.class.getName(),
						hitsPager);
			}
		} finally {
			if (folder != null)
				folder.close();
		}
	}
%>
<br />
<strong>筆數</strong>
:
<%=hitsPager.getHitsTotal()%>,
<strong>頁數</strong>
:<%=hitsPager.getPageTotal()%>
<table border="1">
	<tr>
		<%
			for (int i = 0; i < displayOrder.length; i++) {
		%>
		<td><%=displayOrder[i]%></td>
		<%
			}
		%>
	</tr>
	<%
		while (hitsPager.hasNext()) {
			ODHit hit = hitsPager.nextHit();
	%>
	<tr>
		<%
			for (int i = 0; i < displayOrder.length; i++) {
		%>
		<td><a
			href="javascript:onclick=openXmlViewer('<%=hit.getDocId()%>')"> <%=hit.getDisplayValue(displayOrder[i])%></a></td>
		<%
			}
		%>
	</tr>
	<%
		}
	%>
</table>
<br />
<strong>目前頁次</strong>
:<%=hitsPager.getCurrentPage()%>
<strong>頁次</strong>
:
<%
	for (int i = 1; i <= hitsPager.getPageTotal(); i++) {
		if (i > 1)
			out.print(",");
%>
<a href="index2.jsp?hitsPage=<%=i%>"><%=i%></a>
<%
	}
%>
</body>
</html>