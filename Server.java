import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * NetKartのサーバです。
 * @author zenjiro
 */
public class Server {

	/**
	 * ステージの一覧
	 */
	private final Map<String, Stage> stages;

	/**
	 * クライアントの一覧
	 */
	private final List<Client> clients;
	
	/**
	 * ステージを追加します。
	 * @param id ステージの識別子
	 * @param stage ステージ
	 */
	public void putStage(final String id, final Stage stage) {
		this.stages.put(id, stage);
	}

	/**
	 * ステージを送信します。
	 * TODO どんなファイルでも見られてしまうセキュリティホールがあります。
	 * @param id ステージの識別子
	 * @param writer 出力ストリーム
	 * @throws FileNotFoundException ファイル未検出例外
	 */
	public void sendStage(final String id, final PrintWriter writer) throws FileNotFoundException {
		final Scanner scanner = new Scanner(new File(id));
		while (scanner.hasNextLine()) {
			writer.println(scanner.nextLine());
		}
		scanner.close();
	}
	
	/**
	 * クライアントを追加します。
	 * @param client クライアント
	 */
	public void addPlayer(final Client client) {
		this.clients.add(client);
	}
	
	/**
	 * コンストラクタです。
	 */
	public Server() {
		this.stages = new HashMap<String, Stage>();
		this.clients = new ArrayList<Client>();
	}

	/**
	 * メインメソッドです。
	 * @param args コマンドライン引数
	 * @throws FileNotFoundException 
	 */
	public static void main(final String[] args) throws FileNotFoundException {
		final Server server = new Server();
		for (final String arg : args) {
			server.putStage(arg, (Stage) new XMLDecoder(new FileInputStream(new File(arg))).readObject());
		}
		
		System.out.println("Server started on " + Calendar.getInstance().getTime());
		while (true) {
			try {
				final ServerSocket serverSocket = new ServerSocket(Const.Network.PORT);
				System.out.println("Created server socket on port " + Const.Network.PORT);
				while (true) {
					final Socket socket = serverSocket.accept();
					System.out.println("Accepted connection from " + socket.getInetAddress());
					final Client client = new Client(server, socket.getInputStream(), socket.getOutputStream());
					server.addPlayer(client);
					new Thread(client).start();
				}
			} catch (final IOException exception) {
				exception.printStackTrace();
			}
		}
	}

	/**
	 * @return ステージの一覧
	 */
	public Map<String, Stage> getStages() {
		return this.stages;
	}

}
