package com.steam.pb09.structure;

import java.io.IOException;

import lib.structured.field.StructuredCharField;
import lib.structured.field.xml.Structure;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.xml.sax.SAXException;

public class PB09HeaderLine extends StructuredCharField {
	public PB09HeaderLine(String data) throws IOException, SAXException {
		this(data.toCharArray());
	}

	public PB09HeaderLine(char[] data) throws IOException, SAXException {
		super(Structure.createInstance(PB09HeaderLine.class
				.getResource("/pb09-header.xml")), data);
	}

	public String getUnitName() {
		return this.getFieldAsString("unitName");
	}

	public void setUnitName(String param) {
		setField("unitName", param);
	}

	public String getAccountTypeCode() {
		return this.getFieldAsString("accountTypeCode");
	}

	public void setAccountTypeCode(String param) {
		setField("accountTypeCode", param);
	}

	public String getReportStartYear() {
		return this.getFieldAsString("reportStartYear");
	}

	public void setReportStartYear(String param) {
		setField("reportStartYear", param);
	}

	public String getReportStartMon() {
		return this.getFieldAsString("reportStartMon");
	}

	public void setReportStartMon(String param) {
		setField("reportStartMon", param);
	}

	public String getReportStartDay() {
		return this.getFieldAsString("reportStartDay");
	}

	public void setReportStartDay(String param) {
		setField("reportStartDay", param);
	}

	public String getReportEndYear() {
		return this.getFieldAsString("reportEndYear");
	}

	public void setReportEndYear(String param) {
		setField("reportEndYear", param);
	}

	public String getReportEndMon() {
		return this.getFieldAsString("reportEndMon");
	}

	public void setReportEndMon(String param) {
		setField("reportEndMon", param);
	}

	public String getReportEndDay() {
		return this.getFieldAsString("reportEndDay");
	}

	public void setReportEndDay(String param) {
		setField("reportEndDay", param);
	}

	public String getPageNo() {
		return this.getFieldAsString("pageNo");
	}

	public void setPageNo(String param) {
		setField("pageNo", param);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o instanceof PB09HeaderLine) {
			PB09HeaderLine tmp = (PB09HeaderLine) o;
			return (StringUtils.equals(tmp.getUnitName(), this.getUnitName()) && StringUtils
					.equals(tmp.getAccountTypeCode(), this.getAccountTypeCode()));
		}
		return false;
	}

	public String toString() {
		return new ToStringBuilder(this)

		.append("unitName", getUnitName()).append("accountTypeCode",
				getAccountTypeCode()).append("reportStartYear",
				getReportStartYear()).append("reportStartMon",
				getReportStartMon()).append("reportStartDay",
				getReportStartDay())
				.append("reportEndYear", getReportEndYear()).append(
						"reportEndMon", getReportEndMon()).append(
						"reportEndDay", getReportEndDay()).append("pageNo",
						getPageNo())

				.toString();
	}
}