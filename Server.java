import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * NetKartのサーバです。
 * @author zenjiro
 */
public class Server {

	/**
	 * ステージの一覧
	 */
	final Map<String, Stage> stages;
	
	/**
	 * プレイヤの識別子とクライアントの対応表
	 */
	final Map<String, Client> clientsTable;

	/**
	 * コンストラクタです。
	 */
	public Server() {
		this.stages = new ConcurrentHashMap<String, Stage>();
		this.clientsTable = new ConcurrentHashMap<String, Client>();
	}

	/**
	 * メインメソッドです。
	 * @param args コマンドライン引数
	 * @throws FileNotFoundException 
	 */
	public static void main(final String[] args) throws FileNotFoundException {
		final Server server = new Server();
		for (final String arg : args) {
			server.stages.put(arg, (Stage) new XMLDecoder(new FileInputStream(new File(arg))).readObject());
		}

		System.out.println("Server started on " + Calendar.getInstance().getTime());
		final ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
		while (true) {
			try {
				final ServerSocket serverSocket = new ServerSocket(Const.Network.PORT);
				System.out.println("Created server socket on port " + Const.Network.PORT);
				while (true) {
					final Socket socket = serverSocket.accept();
					System.out.println("Accepted connection from " + socket.getInetAddress());
					final Client client = new Client(server, socket.getInputStream(), socket.getOutputStream());
					service.schedule(client, 0, TimeUnit.SECONDS);
				}
			} catch (final IOException exception) {
				exception.printStackTrace();
			}
		}
	}

}
