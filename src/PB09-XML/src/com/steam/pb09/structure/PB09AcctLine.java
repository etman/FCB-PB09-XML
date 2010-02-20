package com.steam.pb09.structure;

import java.io.IOException;

import lib.structured.field.StructuredCharField;
import lib.structured.field.xml.Structure;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.xml.sax.SAXException;

public class PB09AcctLine extends StructuredCharField {
	public PB09AcctLine(String data) throws IOException, SAXException {
		this(data.toCharArray());
	}

	public PB09AcctLine(char[] data) throws IOException, SAXException {
		super(Structure.createInstance(PB09AcctLine.class
				.getResource("/pb09-acct-line.xml")), data);
	}

	public String getAccount() {
		return this.getFieldAsString("account");
	}

	public void setAccount(String param) {
		setField("account", param);
	}

	public String getTrxDate() {
		return this.getFieldAsString("trxDate");
	}

	public void setTrxDate(String param) {
		setField("trxDate", param);
	}

	public String getDepositType() {
		return this.getFieldAsString("depositType");
	}

	public void setDepositType(String param) {
		setField("depositType", param);
	}

	public String getBetweenState() {
		return this.getFieldAsString("betweenState");
	}

	public void setBetweenState(String param) {
		setField("betweenState", param);
	}

	public String getAppBank() {
		return this.getFieldAsString("appBank");
	}

	public void setAppBank(String param) {
		setField("appBank", param);
	}

	public String getMachineCode() {
		return this.getFieldAsString("machineCode");
	}

	public void setMachineCode(String param) {
		setField("machineCode", param);
	}

	public String getTransNo() {
		return this.getFieldAsString("transNo");
	}

	public void setTransNo(String param) {
		setField("transNo", param);
	}

	public String getReversalMark() {
		return this.getFieldAsString("reversalMark");
	}

	public void setReversalMark(String param) {
		setField("reversalMark", param);
	}

	public String getAccountingDateDelay() {
		return this.getFieldAsString("accountingDateDelay");
	}

	public void setAccountingDateDelay(String param) {
		setField("accountingDateDelay", param);
	}

	public String getCreditCash() {
		return this.getFieldAsString("creditCash");
	}

	public void setCreditCash(String param) {
		setField("creditCash", param);
	}

	public String getDebitCash() {
		return this.getFieldAsString("debitCash");
	}

	public void setDebitCash(String param) {
		setField("debitCash", param);
	}

	public String getDepositBalance() {
		return this.getFieldAsString("depositBalance");
	}

	public void setDepositBalance(String param) {
		setField("depositBalance", param);
	}

	public String getUnadjProdNumber() {
		return this.getFieldAsString("unadjProdNumber");
	}

	public void setUnadjProdNumber(String param) {
		setField("unadjProdNumber", param);
	}

	public String getAdjProdNumberInLimit() {
		return this.getFieldAsString("adjProdNumberInLimit");
	}

	public void setAdjProdNumberInLimit(String param) {
		setField("adjProdNumberInLimit", param);
	}

	public String getAdjProdNumberOutLimit() {
		return this.getFieldAsString("adjProdNumberOutLimit");
	}

	public void setAdjProdNumberOutLimit(String param) {
		setField("adjProdNumberOutLimit", param);
	}

	public String getTransAccntSrc() {
		return this.getFieldAsString("transAccntSrc");
	}

	public void setTransAccntSrc(String param) {
		setField("transAccntSrc", param);
	}

	public String toString() {
		return new ToStringBuilder(this)

		.append("account", getAccount()).append("trxDate", getTrxDate())
				.append("depositType", getDepositType()).append("betweenState",
						getBetweenState()).append("appBank", getAppBank())
				.append("machineCode", getMachineCode()).append("transNo",
						getTransNo()).append("reversalMark", getReversalMark())
				.append("accountingDateDelay", getAccountingDateDelay())
				.append("creditCash", getCreditCash()).append("debitCash",
						getDebitCash()).append("depositBalance",
						getDepositBalance()).append("unadjProdNumber",
						getUnadjProdNumber()).append("adjProdNumberInLimit",
						getAdjProdNumberInLimit()).append(
						"adjProdNumberOutLimit", getAdjProdNumberOutLimit())
				.append("transAccntSrc", getTransAccntSrc())

				.toString();
	}
}