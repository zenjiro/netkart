import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * NetKartクライアントの2D実装です。
 * @author zenjiro
 */
public class NetKart2D {

	/**
	 * メインメソッドです。
	 * @param args コマンドライン引数
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(final String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException, InterruptedException, UnknownHostException, IOException {
		final KeyHandler handler = new KeyHandler();
		final Player player = new Player();
		final Stage stage = new Stage();
		final Map<String, Player> players = new ConcurrentHashMap<String, Player>();
		
		final Socket socket = new Socket(Const.Network.SERVER, Const.Network.PORT);
		final InputStream in = socket.getInputStream();
		final Scanner scanner = new Scanner(in);
		final PrintWriter out = new PrintWriter(socket.getOutputStream());
		out.println(Const.Network.GET_STAGE_LIST);
		out.flush();
		final List<String> stageNames = Arrays.asList(scanner.nextLine().split("\t"));
		System.out.println(stageNames);
		out.println(Const.Network.PUT_STAGE_ID);
		out.println(stageNames.get(1));
		out.println(Const.Network.GET_STAGE_ID);
		out.flush();
		final String stageName = scanner.nextLine();
		System.out.println("Stage name is " + stageName);
		stage.setStage((Stage) new XMLDecoder(NetKart2D.class.getResourceAsStream(stageName)).readObject());
		out.println(Const.Network.PUT_PLAYER_NAME);
		out.println("test" + new Random().nextInt(100));
		out.println(Const.Network.GET_PLAYER_NAME);
		out.flush();
		final String myName = scanner.nextLine();
		System.out.println("My name is " + myName);
		out.println(Const.Network.GET_PLAYER_LIST);
		out.flush();
		for (final String playerName : scanner.nextLine().split("\t")) {
			players.put(playerName, new Player());
		}
		System.out.println("players = " + players);
		final CheckPoint startLine = stage.getCheckPoints().get(0);
		player.x = startLine.location.getX();
		player.y = startLine.location.getY();
		player.direction = startLine.angle;
		final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		service.scheduleAtFixedRate(new Runnable() {
			public void run() {
				out.println(Const.Network.PUT_LOCATION);
				out.println((int) player.x);
				out.println((int) player.y);
				out.println((int) (player.direction * 360));
				out.flush();
				for (final Map.Entry<String, Player> entry : players.entrySet()) {
					final Player player = entry.getValue();
					out.println(Const.Network.GET_LOCATION);
					out.println(entry.getKey());
					out.flush();
					player.x = Integer.parseInt(scanner.nextLine());
					player.y = Integer.parseInt(scanner.nextLine());
					player.direction = Integer.parseInt(scanner.nextLine()) / 360.0;
				}
			}
		}, 0, 1000, TimeUnit.MILLISECONDS);

		final JFrame frame = new JFrame("Kart 2D");
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		frame.setLocationByPlatform(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setSize(640, 480);
		frame.addKeyListener(handler);
		frame.addWindowListener(new WindowListener() {
		
			public void windowOpened(final WindowEvent e) {
			}
		
			public void windowIconified(final WindowEvent e) {
			}
		
			public void windowDeiconified(final WindowEvent e) {
			}
		
			public void windowDeactivated(final WindowEvent e) {
			}
		
			public void windowClosing(final WindowEvent e) {
			}
		
			public void windowClosed(final WindowEvent e) {
				service.shutdown();
			}
		
			public void windowActivated(final WindowEvent e) {
			}
		
		});
		final JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(final Graphics g) {
				final double x = player.x;
				final double y = player.y;
				final double direction = player.direction;
				final Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.WHITE);
				g2.fillRect(0, 0, this.getWidth(), this.getHeight());
				g2.translate(0, this.getHeight());
				g2.scale(1, -1);
				g2.translate(this.getWidth() / 2, this.getHeight() / 4);
				g2.scale(2, 2);
				g2.rotate(-direction + Math.PI / 2);
				g2.translate(-x, -y);
				this.draw(player, stage, g2, x, y, direction);
				g2.translate(x, y);
				g2.rotate(direction - Math.PI / 2);
				g2.scale(.5, .5);
				g2.translate(-this.getWidth() / 2, -this.getHeight() / 4);
				g2.translate(0, this.getHeight() * 3 / 4);
				g2.scale(.25, .25);
				this.draw(player, stage, g2, x, y, direction);
				g2.scale(4, 4);
				g2.translate(0, -this.getHeight() * 3 / 4);
				g2.setColor(Color.RED);
				g2.translate(100, 20);
				g2.rotate(Math.PI - player.speed / 2.4 * Math.PI);
				g2.fillPolygon(new int[] { 0, 80, 80, 0 }, new int[] { -4, -2, 2, 4 }, 4);
			}

			private void draw(final Player player, final Stage stage, final Graphics2D g2, final double x,
					final double y, final double direction) {
				g2.setColor(new Color(240, 240, 240));
				for (final Rail rail : stage.getRails()) {
					g2.fill(Util.getShape(rail, true));
				}
				g2.setColor(Color.BLACK);
				for (final Rail rail : stage.getRails()) {
					g2.draw(Util.getShape(rail, false));
				}
				final GeneralPath playerShape = new GeneralPath();
				playerShape.moveTo(0, 0);
				playerShape.lineTo(-24, -10);
				playerShape.lineTo(-24, 10);
				playerShape.closePath();
				for (final Player enemy : players.values()) {
					final AffineTransform playerTransform = new AffineTransform();
					playerTransform.translate(enemy.x, enemy.y);
					playerTransform.rotate(enemy.direction);
					g2.setColor(Color.YELLOW);
					g2.fill(playerTransform.createTransformedShape(playerShape));
					g2.setColor(Color.BLACK);
					g2.draw(playerTransform.createTransformedShape(playerShape));
				}
				final AffineTransform playerTransform = new AffineTransform();
				playerTransform.translate(x, y);
				playerTransform.rotate(direction);
				g2.setColor(Color.RED);
				g2.fill(playerTransform.createTransformedShape(playerShape));
				g2.setColor(Color.BLACK);
				g2.draw(playerTransform.createTransformedShape(playerShape));
			}
		};
		panel.setFocusable(false);
		frame.add(panel);
		
		frame.setVisible(true);

		int wait = 1;
		long last = System.currentTimeMillis();
		while (frame.isShowing()) {
			// キー入力を処理する。
			if (handler.isPressed(KeyEvent.VK_BACK_SLASH)) {
				player.speed += 0.05;
			}
			if (handler.isPressed(KeyEvent.VK_LEFT)) {
				player.handle += .002;
			}
			if (handler.isPressed(KeyEvent.VK_RIGHT)) {
				player.handle -= .002;
			}
			player.speed *= .98;
			player.handle *= .9;
			player.speed *= (1 - Math.abs(player.handle) / 2);
			// test
			player.direction += 0.001;

			// プレイヤの位置を変更する。
			player.direction += player.handle;
			player.x += player.speed * Math.cos(player.direction);
			player.y += player.speed * Math.sin(player.direction);
			
			// 再描画する。
			panel.repaint();

			// 待つ。
			final long now = System.currentTimeMillis();
			if (now - last < 10) {
				wait++;
			} else if (now - last > 10) {
				wait = Math.max(0, wait - 1);
			}
			last = now;
			Thread.sleep(wait);
			System.out.print("Waiting " + wait + "ms at " + now + " \r");
		}
	}

}
