import exporter.RpcExporter;
import importor.RpcImporter;
import service.EchoService;
import service.EchoServiceImpl;

import java.net.InetSocketAddress;

/**
 * User: huangqiang
 * Date: 18/7/13
 * Time: 下午11:13
 */
public class RpcTest {
	public static void main(String[] args) throws Exception {
	    new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					RpcExporter.exporter("localhost", 8088);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		RpcImporter<EchoService> importer = new RpcImporter<EchoService>();
		EchoService echo = importer.importer(EchoServiceImpl.class, new InetSocketAddress("localhost", 8088));
		System.out.println(echo.echo("Are u ok ?"));
	}
}
