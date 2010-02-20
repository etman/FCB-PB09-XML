package com.steam.pb09;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import lib.utils.Utils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.steam.pb09.structure.PB09AcctLine;
import com.steam.pb09.structure.PB09HeaderLine;

public class PB09Converter {
	public static void main(String[] args) throws IOException {
		Options options = buildCLIOptions();
		PB09Args cmdArgs = PB09Args.createInstance(options, args);
		if (cmdArgs == null) {
			return;
		}
		if (!cmdArgs.isValid()) {
			printUsage(options);
			return;
		}

		File reportFile = cmdArgs.getReportFile();
		String reportName = reportFile.getName();
		int hasDot = reportName.lastIndexOf(".");
		String xmlName = (hasDot != -1) ? reportName.substring(0, hasDot)
				: reportName;
		File xmlFile = new File(cmdArgs.getStoreFolder(), Utils.concat(xmlName,
				".xml"));
		generate(reportFile, cmdArgs.getReportEncoding(), xmlFile);
	}

	private static void generate(File reportFile, Charset encoding, File xmlFile)
			throws IOException {
		PrintWriter indexOut = null;
		PrintWriter xmlOut = null;
		CountBytesOutputStream byteCnt = null;
		BufferedReader bufReader = null;
		try {
			File indexFile = new File(Utils.concat(xmlFile.getCanonicalPath(),
					".ind"));
			bufReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(reportFile), encoding));
			xmlOut = new PrintWriter(new OutputStreamWriter(
					(byteCnt = new CountBytesOutputStream(
							new BufferedOutputStream(new FileOutputStream(
									xmlFile)))), "ms950"), true);
			indexOut = new PrintWriter(new OutputStreamWriter(
					new BufferedOutputStream(new FileOutputStream(indexFile)),
					"ms950"), true);
			indexOut.println("CODEPAGE:950");

			PB09ReportIterator itr = new PB09ReportIterator(bufReader);
			PB09Report lastReport = null;
			String lastAccount = null;
			long lastOffset = 0;
			while (itr.hasNext()) {
				lastReport = itr.next();
				for (PB09AcctLine acctLine : lastReport.getAcctLines()) {
					if (!acctLine.getAccount().equals(lastAccount)) {
						if (lastAccount != null) {
							lastReport.writeXMLEnd(xmlOut);
							long length = byteCnt.getBytesWritten();
							lastReport.writeIndex(indexOut, xmlFile.getName(),
									lastOffset, length - lastOffset,
									lastAccount);
						}

						lastOffset = byteCnt.getBytesWritten();
						lastAccount = acctLine.getAccount();
						PB09HeaderLine header = lastReport.getHeader();
						System.out.println(Utils.concat("Processing: Unit[",
								header.getUnitName(), "], Account[", header
										.getAccountTypeCode(), "]"));
						lastReport.writeXMLBegin(xmlOut);
					}
					lastReport.writeAcctLine(xmlOut, acctLine);
					// acctWriter.writeAcctLine(report, acctLine);
				}
			}
			if (lastAccount != null) {
				lastReport.writeXMLEnd(xmlOut);
				long length = byteCnt.getBytesWritten();
				lastReport.writeIndex(indexOut, xmlFile.getName(), lastOffset,
						length - lastOffset, lastAccount);
			}
		} finally {
			if (bufReader != null)
				bufReader.close();
			if (xmlOut != null)
				xmlOut.close();
			if (indexOut != null)
				indexOut.close();
		}
	}

	private static Options buildCLIOptions() {
		Option reportOption = new Option(
				"r",
				true,
				"Specify the location of report file and file encoding(optional) for XML/Index generation. The default encoding is MS950.");
		reportOption.setArgs(2);
		reportOption.setArgName("file [encoding]");

		Option destOption = new Option(
				"d",
				true,
				"Specify the directory to store XML files and generic Index files produced. The default save folder is current(working) folder.");
		destOption.setArgs(1);
		destOption.setArgName("[folder]");

		Options options = new Options();
		options.addOption(reportOption);
		options.addOption(destOption);
		return options;
	}

	private static void printUsage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("pb09.bat", options);
	}

	private static class PB09Args {
		private CommandLine cmd = null;

		private PB09Args(CommandLine cmd) {
			this.cmd = cmd;
		}

		public boolean isValid() {
			if (getReportFile() == null) {
				System.out
						.println("Argument error: The location of report file must be specified.");
				return false;
			}
			if (getStoreFolder() == null) {
				System.out
						.println("Argument error: The save folder specified does not exist.");
				return false;
			}
			return true;
		}

		public File getReportFile() {
			if (!cmd.hasOption('r')) {
				return null;
			}
			String[] reportArgs = cmd.getOptionValues('r');
			if (reportArgs.length < 1)
				return null;
			File f = new File(reportArgs[0]);
			return f.exists() ? f : null;
		}

		public Charset getReportEncoding() {
			if (!cmd.hasOption('r')) {
				return null;
			}
			String[] reportArgs = cmd.getOptionValues('r');
			if (reportArgs.length < 2)
				return Charset.forName("ms950");
			return Charset.forName(reportArgs[1]);
		}

		public File getStoreFolder() {
			if (!cmd.hasOption('d')) {
				return new File("./");
			}
			File f = new File(cmd.getOptionValue('d'));
			if (!f.exists()) {
				return null;
			}
			return f;
		}

		public static PB09Args createInstance(Options options, String[] args) {
			CommandLineParser parser = new PosixParser();
			CommandLine cmd;
			try {
				cmd = parser.parse(options, args);
				return new PB09Args(cmd);
			} catch (ParseException e) {
				System.out.println("Argument error: " + e.getMessage());
			}
			return null;
		}

	}

}
