package internal.dummy;

import com.ibm.edms.od.ODConfig;
import com.ibm.edms.od.ODConstant;
import com.ibm.edms.od.ODFolder;
import com.ibm.edms.od.ODServer;

public class TestConnect {
	public static void main(String[] args) throws Exception {
		ODServer odServer = null;
		try {
			ODConfig cfg = new ODConfig(ODConstant.PLUGIN, // AfpViewer
					ODConstant.APPLET, // LineViewer
					null, // MetaViewer
					500, // MaxHits
					"c:\\applets", // AppletDir
					"CHT", // Language
					"c:\\temp", // TempDir
					"c:\\temp\\trace", // TraceDir
					1); // TraceLevel
			odServer = new ODServer(cfg);
			odServer.setPort(1445);
			if (!odServer.isInitialized()) {
				System.out.println("[ODWEK INIT]initialize ODServer.....");
				odServer.initialize("TestConnect");
				System.out.println("[ODWEK INIT]ODServer initialized.....");
			}
			odServer.logon("192.168.130.134", "admin", "passw0rd");

			ODFolder folder = odServer.openFolder("FCB-PB09-XML-3");
			System.out.println("Folder desc: " + folder.getName());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (odServer != null) {
				odServer.logoff();
				odServer.terminate();
			}
		}
	}

	public static void connect(ODServer server) throws Exception {
		try {
			ODFolder folder = server.openFolder("FCB-PB09-XML-3");
			System.out.println("Folder desc: " + folder.getName());
		} finally {
			server.logoff();
			server.terminate();
		}
	}
}
