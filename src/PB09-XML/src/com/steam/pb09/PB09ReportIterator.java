package com.steam.pb09;

import java.io.BufferedReader;
import java.io.IOException;

import lib.utils.Utils;

import org.apache.commons.lang.StringUtils;

import com.steam.pb09.structure.PB09AcctLine;
import com.steam.pb09.structure.PB09HeaderLine;

public class PB09ReportIterator {
	private BufferedReader bufReader = null;
	private PB09Report report = null;

	private String lastAccount = null;

	public PB09ReportIterator(BufferedReader reader) {
		this.bufReader = reader;
	}

	public boolean hasNext() throws IOException {
		this.report = null;
		String textLine = null;
		try {
			while (bufReader.ready()) {
				bufReader.mark(20);
				textLine = bufReader.readLine();
				switch (textLine.charAt(0)) {
				case '1':
					if (report != null) {
						bufReader.reset();
						return true;
						// buildIndex(xmlFile, xmlOut.writeEnd(), indexOut);
					}
					PB09HeaderLine headerLine = nextHeader(bufReader);
					if (headerLine == null) {
						System.out.println("    [Invalid Header] " + textLine);
						continue;
					}
					this.report = new PB09Report(headerLine);
					continue;
				case '0':
					lastAccount = null;
				case '-':
					if (report != null)
						return true;
					System.out.println("    [Ignore] " + textLine);
					continue;
				default:
					if (report != null) {
						PB09AcctLine acctLine = new PB09AcctLine(textLine);
						String account = StringUtils.isNotBlank(acctLine
								.getAccount()) ? acctLine.getAccount()
								: lastAccount;
						this.lastAccount = account;
						acctLine.setAccount(lastAccount);
						report.add(acctLine);
						continue;
					}
					System.out.println("    [Ignore] " + textLine);
				}
			}
			return (report != null);
		} catch (Exception e) {
			throw new IllegalStateException(Utils.concat(
					"Error occured after line: ", textLine), e);
		}
	}

	public PB09Report next() {
		return report;
	}

	public static PB09HeaderLine nextHeader(BufferedReader bufReader) {
		String textLine = null;
		try {
			if (bufReader.ready()) {
				textLine = bufReader.readLine();
				if ("-  ".equals(textLine)) {
					PB09HeaderLine line = new PB09HeaderLine(
							(textLine = bufReader.readLine()));
					textLine = bufReader.readLine();
					return ("-  ".equals(textLine)) ? line : null;
				}
			}
			return null;
		} catch (Exception e) {
			throw new IllegalStateException(Utils.concat(
					"Error occured after line: ", textLine), e);
		}
	}

}
