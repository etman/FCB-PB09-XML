package com.steam.pb09;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lib.utils.Utils;

import org.apache.commons.lang.StringUtils;

import com.steam.pb09.structure.PB09AcctLine;
import com.steam.pb09.structure.PB09HeaderLine;
import com.steam.pb09.utils.ConvertUtils;

public class PB09Report {
	private PB09HeaderLine header = null;
	private List<PB09AcctLine> acctLines = null;

	public PB09Report(PB09HeaderLine header) {
		this.header = header;
		this.acctLines = new ArrayList<PB09AcctLine>();
	}

	public void add(PB09AcctLine acctLine) {
		this.acctLines.add(acctLine);
	}

	public PB09HeaderLine getHeader() {
		return header;
	}

	public List<PB09AcctLine> getAcctLines() {
		return acctLines;
	}

	public Iterator<PB09AcctLine> iterator() {
		return acctLines.iterator();
	}

	public String toString() {
		return Utils.concat(header.toString(), System
				.getProperty("line.separator"), "Acct Lines: ", acctLines
				.size());
	}

	public void writeXMLBegin(PrintWriter out) {
		out.println("<?xml version=\"1.0\" encoding=\"Big5\"?>");
		out.println("<存款明細分類帳>");
		out.println(Utils.concat("<單位名稱>", header.getUnitName(), "</單位名稱>"));
		out.println(Utils.concat("<科目代號>", header.getAccountTypeCode(),
				"</科目代號>"));
		out.println(Utils.concat("<報表起始日期>", ConvertUtils.fmtDate(header
				.getReportStartYear(), header.getReportStartMon(), header
				.getReportStartDay()), "</報表起始日期>"));
		out.println(Utils.concat("<報表結束日期>", ConvertUtils.fmtDate(header
				.getReportEndYear(), header.getReportEndMon(), header
				.getReportEndDay()), "</報表結束日期>"));
	}

	public void writeXMLEnd(PrintWriter out) {
		out.println("</存款明細分類帳>");
	}

	public void writeAcctLine(PrintWriter out, PB09AcctLine acctLine) {
		out.println("<交易資料>");
		out.println(Utils.concat("<帳號>", acctLine.getAccount(), "</帳號>"));
		out.println(Utils.concat("<交易日期>", ConvertUtils
				.roc2dcDashStyle(acctLine.getTrxDate()), "</交易日期>"));
		out.println(Utils
				.concat("<存戶性質>", acctLine.getDepositType(), "</存戶性質>"));
		out.println(Utils.concat("<往來狀態>", acctLine.getBetweenState(),
				"</往來狀態>"));
		if (StringUtils.isNotBlank(acctLine.getAppBank())) {
			out.println("<交易序號>");
			out
					.println(Utils.concat("	<受理行>", acctLine.getAppBank(),
							"</受理行>"));
			out.println(Utils.concat("	<機號>", acctLine.getMachineCode(),
					"</機號>"));
			out.println(Utils.concat("	<編號>", acctLine.getTransNo(), "</編號>"));
			out.println("</交易序號>");
		}
		if (StringUtils.isNotBlank(acctLine.getReversalMark()))
			out.println(Utils.concat("<被沖正記號>", acctLine.getReversalMark(),
					"</被沖正記號>"));
		if (StringUtils.isNotBlank(acctLine.getAccountingDateDelay()))
			out.println(Utils.concat("<摘要或延時營業記帳日>", acctLine
					.getAccountingDateDelay(), "</摘要或延時營業記帳日>"));
		if (StringUtils.isNotBlank(acctLine.getCreditCash()))
			out.println(Utils.concat("<借方金額>", ConvertUtils.fmtDollar(acctLine
					.getCreditCash()), "</借方金額>"));
		if (StringUtils.isNotBlank(acctLine.getDebitCash()))
			out.println(Utils.concat("<貸方金額>", ConvertUtils.fmtDollar(acctLine
					.getDebitCash()), "</貸方金額>"));
		if (StringUtils.isNotBlank(acctLine.getDepositBalance()))
			out.println(Utils.concat("<存款餘額>", ConvertUtils.fmtDollar(acctLine
					.getDepositBalance()), "</存款餘額>"));
		if (StringUtils.isNotBlank(acctLine.getUnadjProdNumber()))
			out.println(Utils.concat("<參考用未調整積數 單位=\"100\">", acctLine
					.getUnadjProdNumber(), "</參考用未調整積數>"));
		if (StringUtils.isNotBlank(acctLine.getAdjProdNumberInLimit()))
			out.println(Utils.concat("<調整後限額內積數 單位=\"100\">", acctLine
					.getAdjProdNumberInLimit(), "</調整後限額內積數>"));
		if (StringUtils.isNotBlank(acctLine.getAdjProdNumberOutLimit()))
			out.println(Utils.concat("<調整後限額外積數 單位=\"100\">", acctLine
					.getAdjProdNumberOutLimit(), "</調整後限額外積數>"));
		if (StringUtils.isNotBlank(acctLine.getTransAccntSrc()))
			out.println(Utils.concat("<轉入帳號>", acctLine.getTransAccntSrc(),
					"</轉入帳號>"));

		out.println("</交易資料>");
	}

	public void writeIndex(PrintWriter out, String xmlFileName, long offset,
			long length, String acctNumber) {
		out.println("GROUP_FIELD_NAME:R_ID");
		out.println("GROUP_FIELD_VALUE:PB09");
//		out.println("GROUP_FIELD_NAME:R_NAME");
//		out.println("GROUP_FIELD_VALUE:存款明細分類帳");
//		out.println("GROUP_FIELD_NAME:B_ID");
//		out.println(Utils.concat("GROUP_FIELD_VALUE:", StringUtils.replace(
//				header.getUnitName(), " ", "")));
		out.println("GROUP_FIELD_NAME:S_DATE");
		out.println(Utils.concat("GROUP_FIELD_VALUE:", ConvertUtils.fmtDate(
				header.getReportStartYear(), header.getReportStartMon(), header
						.getReportStartDay())));
		out.println("GROUP_FIELD_NAME:E_DATE");
		out.println(Utils.concat("GROUP_FIELD_VALUE:", ConvertUtils.fmtDate(
				header.getReportEndYear(), header.getReportEndMon(), header
						.getReportEndDay())));
		out.println("GROUP_FIELD_NAME:ACCOUNT");
		out.println(Utils.concat("GROUP_FIELD_VALUE:", header
				.getAccountTypeCode()));
		out.println("GROUP_FIELD_NAME:ACCOUNT_NUMBER");
		out.println(Utils.concat("GROUP_FIELD_VALUE:", acctNumber));
		out.println(Utils.concat("GROUP_OFFSET:", offset));
		out.println(Utils.concat("GROUP_LENGTH:", length));
		out.println(Utils.concat("GROUP_FILENAME:", xmlFileName));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PB09Report other = (PB09Report) obj;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		return true;
	}
}
