package internal.dummy;

import java.io.File;

import com.ibm.edms.od.ODConstant;
import com.ibm.edms.od.ODFolder;
import com.ibm.edms.od.ODServer;

public class TestConnect {
	public static void main(String[] args) {
		try {
			ODServer server = new ODServer();
			connect(server);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void connect(ODServer server) throws Exception {
		server.setApplicationName("ODWEKAPIDemo");
		server.setConnectType(ODConstant.CONNECT_TYPE_TCPIP);
		server.setServer("localhost");
		server.setPort(1445);
		server.setUserId("admin");
		server.setPassword("passw0rd");
		server.initialize(new File("E:\\ars\\www")
				.getCanonicalPath(), "TestConnect.java");

		server.logon();
		try {
			ODFolder folder = server.openFolder("FCB-PB09-XML-3");
			System.out.println("Folder desc: " + folder.getName());
		} finally {
			server.logoff();
			server.terminate();
		}
	}
}
