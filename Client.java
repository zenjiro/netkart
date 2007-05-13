import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Map;
import java.util.Scanner;

/**
 * クライアントを表すクラスです。
 * @author zenjiro
 */
public class Client implements Runnable {

	/**
	 * サーバ
	 */
	private final Server server;

	/**
	 * 入力ストリーム
	 */
	private final InputStream in;

	/**
	 * 出力ストリーム
	 */
	private final OutputStream out;

	/**
	 * 識別子
	 */
	private String id;

	/**
	 * ステージの識別子
	 */
	private String stageID;

	/**
	 * コンストラクタです。
	 * @param server サーバ
	 * @param in 入力ストリーム
	 * @param out 出力ストリーム
	 */
	public Client(final Server server, final InputStream in, final OutputStream out) {
		this.server = server;
		this.in = in;
		this.out = out;
	}

	public void run() {
		final Scanner scanner = new Scanner(this.in);
		final PrintWriter writer = new PrintWriter(this.out);
		while (scanner.hasNextLine()) {
			try {
				final String line = scanner.nextLine();
				System.out.println("Got " + line);
				if (line.equals(Const.Network.PUT_PLAYER_NAME)) {
					this.id = scanner.nextLine();
					System.out.println("Set id " + this.id);
				} else if (line.equals(Const.Network.GET_STAGE_LIST)) {
					for (final Map.Entry<String, Stage> entry : this.server.getStages().entrySet()) {
						writer.println(Const.Network.PUT_STAGE_ID);
						writer.println(entry.getKey());
					}
				} else if (line.equals(Const.Network.PUT_STAGE_ID)) {
					this.stageID = scanner.nextLine();
					System.out.println("Set stage id " + this.stageID);
				} else if (line.equals(Const.Network.GET_STAGE)) {
					writer.println(Const.Network.PUT_STAGE_START);
					this.server.sendStage(this.stageID, writer);
					writer.println(Const.Network.PUT_STAGE_END);
				}
				writer.flush();
			} catch (final IOException exception) {
				exception.printStackTrace();
			}
		}
		System.out.println(this.id + " disconnected on " + Calendar.getInstance().getTime());
	}

}
