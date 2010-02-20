package internal.dummy;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.servlet.jsp.JspWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class RetrieveDoc {
	public static void main(String[] args) throws IOException,
			InterruptedException {
		int rtCode = 0;// retrieve();
		System.out.println("exit with return code: " + rtCode);
		if (rtCode == 0) {
			InputStreamReader in = new InputStreamReader(new FileInputStream(
					new File("pb09.4")), "ms950");
			char[] buf = new char[1024];
			StringBuffer strBuf = new StringBuffer();
			for (int lenRead = 0; (lenRead = in.read(buf)) > 0;) {
				strBuf.append(buf, 0, lenRead);
			}
			transform(strBuf.toString(), new OutputStreamWriter(System.out,
					"ms950"));
		}

	}

	// where b_id='535�sæ�&�'

	private static int retrieve(String qStr, String saveDir,
			String fileNamePrefix, JspWriter out) throws IOException,
			InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		Process proc = null;
		try {
			String cmd = new StringBuffer(
					"arsdoc get -h localhost -u admin -p password -f FCB-PB09-XML -i \"")
					.append(qStr).append("\" -d \"").append(saveDir).append(
							"\" -o ").append(fileNamePrefix).append(" -n")
					.toString();

			if ("AIX".equals(System.getProperty("os.name"))) {
				File shell = new File(saveDir, fileNamePrefix);
				writeToShell(shell, cmd);
				proc = runtime.exec("sh " + shell.getAbsolutePath());
			} else {
				proc = runtime.exec(cmd);
			}

			int rtCode = proc.waitFor();
			if (rtCode != 0) {
				InputStream in = proc.getErrorStream();
				ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				bytesOut
						.write(("[COMMAND ERROR] return code[" + rtCode + "]: " + cmd)
								.getBytes());
				bytesOut.write(System.getProperty("line.separator").getBytes());
				for (int i = 0; (i = in.read(buf)) > 0;) {
					bytesOut.write(buf, 0, i);
				}
				out.println(bytesOut.toString(Charset.defaultCharset().name()));
			}
			return rtCode;
		} finally {
			if (proc != null)
				proc.destroy();
		}
	}

	private static void writeToShell(File shell, String cmd) throws IOException {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(shell));
			out.write(cmd);
		} finally {
			if (out != null)
				out.close();
		}
	}

	private static void transform(String xml, Writer out)
			throws UnsupportedEncodingException {
		try {
			Source xmlSource = new StreamSource(new StringReader(xml));
			Source xsltSource = new StreamSource(new File("pb09-disp.xsl"));
			Result result = new StreamResult(out);

			// create an instance of TransformerFactory
			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer trans = transFact.newTransformer(xsltSource);
			trans.transform(xmlSource, result);
		} catch (TransformerException e) {
			throw new IllegalStateException(xml, e);
		}
	}
}
