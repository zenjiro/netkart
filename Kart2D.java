import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * NetKartクライアントの2D実装です。
 * @author zenjiro
 */
public class Kart2D {

	/**
	 * メインメソッドです。
	 * @param args コマンドライン引数
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 * @throws InterruptedException 
	 */
	public static void main(final String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException, FileNotFoundException, InterruptedException {
		final KeyHandler handler = new KeyHandler();
		final Player player = new Player();
		final Stage stage = new Stage();
		final XMLDecoder decoder = new XMLDecoder(new FileInputStream(args[0]));
		stage.setStage((Stage) decoder.readObject());
		decoder.close();
		final CheckPoint startLine = stage.getCheckPoints().get(0);
		player.x = startLine.location.getX();
		player.y = startLine.location.getY();
		player.direction = startLine.angle;

		final JFrame frame = new JFrame("Kart 2D");
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		frame.setLocationByPlatform(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setSize(800, 600);
		frame.addKeyListener(handler);
		final JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(final Graphics g) {
				final double x = player.x;
				final double y = player.y;
				final double direction = player.direction;
				final Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.WHITE);
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.translate(0, this.getHeight());
				g2.scale(1, -1);
				g2.translate(getWidth() / 2, getHeight() / 4);
				g2.scale(2, 2);
				g2.rotate(-direction + Math.PI / 2);
				g2.translate(-x, -y);
				draw(player, stage, g2, x, y, direction);
				g2.translate(x, y);
				g2.rotate(direction - Math.PI / 2);
				g2.scale(.5, .5);
				g2.translate(-getWidth() / 2, -getHeight() / 4);
				g2.translate(0, getHeight() * 3 / 4);
				g2.scale(.25, .25);
				draw(player, stage, g2, x, y, direction);
				g2.scale(4, 4);
				g2.translate(0, -getHeight() * 3 / 4);
				g2.setColor(Color.RED);
				g2.translate(100, 20);
				g2.rotate(Math.PI - player.speed / 2.4 * Math.PI);
				g2.fillPolygon(new int[] { 0, 80, 80, 0 }, new int[] { -4, -2, 2, 4 }, 4);
			}

			private void draw(final Player player, final Stage stage, final Graphics2D g2, final double x, final double y, final double direction) {
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
		}
	}

}
