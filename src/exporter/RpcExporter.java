package exporter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * User: huangqiang
 * Date: 18/7/12
 * Time: 下午11:11
 */
public class RpcExporter {
	static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	public static void exporter(String hostname, int port) throws Exception {
		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(hostname, port));
		try {
			for(;;) {
				executor.execute(new ExporterTask(server.accept()));
			}
		} finally {
			server.close();
		}
	}

	private static class ExporterTask implements Runnable {
		Socket client = null;
		public ExporterTask(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			ObjectInputStream input = null;
			ObjectOutputStream output = null;
			try {
				input = new ObjectInputStream(client.getInputStream());
				String interfaceName = input.readUTF(); // read 1
				Class<?> service = Class.forName(interfaceName);
				String methodName = input.readUTF(); // read 2
				Class<?>[] parameterTypes = (Class<?>[]) input.readObject(); // read 3
				Object[] arguments = (Object[]) input.readObject();
				Method method = service.getMethod(methodName, parameterTypes);
				Object result = method.invoke(service.newInstance(), arguments);
				output = new ObjectOutputStream(client.getOutputStream());
				output.writeObject(result);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (output != null) {
						output.close();
					}

					if (input != null) {
						input.close();
					}

					if (client != null) {
						client.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
