import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
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
	private final PrintWriter out;

	/**
	 * 識別子
	 */
	private String id;

	/**
	 * ステージの識別子
	 */
	private String stageID;

	/**
	 * x座標
	 */
	private double x;

	/**
	 * y座標
	 */
	private double y;

	/**
	 * 向き
	 */
	private double direction;

	/**
	 * コンストラクタです。
	 * @param server サーバ
	 * @param in 入力ストリーム
	 * @param out 出力ストリーム
	 */
	public Client(final Server server, final InputStream in, final OutputStream out) {
		this.server = server;
		this.in = in;
		this.out = new PrintWriter(out);
	}

	public void run() {
		final Scanner scanner = new Scanner(this.in);
		while (scanner.hasNextLine()) {
			final String line = scanner.nextLine();
			System.out.println("Got " + line);
			if (line.equals(Const.Network.PUT_PLAYER_NAME)) {
				this.id = scanner.nextLine();
				this.server.clientsTable.put(this.id, this);
				System.out.println("Set id " + this.id);
			} else if (line.equals(Const.Network.GET_PLAYER_NAME)) {
				this.out.println(this.id);
			} else if (line.equals(Const.Network.GET_PLAYER_LIST)) {
				for (final Client client : this.server.clientsTable.values()) {
					this.out.print(client.id + "\t");
				}
				this.out.println();
			} else if (line.equals(Const.Network.GET_STAGE_LIST)) {
				for (final String stageName : this.server.stages.keySet()) {
					this.out.print(stageName + "\t");
				}
				this.out.println();
			} else if (line.equals(Const.Network.PUT_STAGE_ID)) {
				this.stageID = scanner.nextLine();
				System.out.println("Set stage id " + this.stageID);
			} else if (line.equals(Const.Network.GET_STAGE_ID)) {
				this.out.println(this.stageID);
			} else if (line.equals(Const.Network.PUT_LOCATION)) {
				this.x = Integer.parseInt(scanner.nextLine());
				this.y = Integer.parseInt(scanner.nextLine());
				this.direction = Integer.parseInt(scanner.nextLine()) / 360.0;
				System.out.println("Got location " + this.x + ", " + this.y + ", " + this.direction);
			} else if (line.equals(Const.Network.GET_LOCATION)) {
				final Client client = this.server.clientsTable.get(scanner.nextLine());
				this.out.println((int) client.x);
				this.out.println((int) client.y);
				this.out.println((int) (client.direction * 360));
				this.out.flush();
			}
			this.out.flush();
		}
		System.out.println(this.id + " disconnected on " + Calendar.getInstance().getTime());
		this.server.clientsTable.remove(this.id);
	}

}
