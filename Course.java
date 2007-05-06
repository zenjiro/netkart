import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Course {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Collection<Rail> rails = new ArrayList<Rail>();
		rails.add(new Rail(new CurveRail(), new Point2D.Double(200, 200), Math.PI / 2));
		final RailType currentType = new CurveRail();
		final Rail temporaryRail = new Rail(currentType, new Point2D.Double(), 0);

		final JFrame frame = new JFrame("コースエディタ");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setSize(800, 600);
		final JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(final Graphics g) {
				super.paintComponent(g);
				final Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.translate(0, this.getHeight());
				g2.scale(1, -1);
				g2.setColor(Color.BLACK);
				for (final Rail rail : rails) {
					final AffineTransform transform = new AffineTransform();
					transform.translate(rail.location.getX(), rail.location.getY());
					transform.rotate(rail.angle);
					g2.draw(transform.createTransformedShape(rail.type.getShape()));
				}
				g2.setColor(Color.RED);
				if (temporaryRail.location != null) {
					final AffineTransform transform = new AffineTransform();
					transform.translate(temporaryRail.location.getX(), temporaryRail.location.getY());
					transform.rotate(temporaryRail.angle);
					g2.draw(transform.createTransformedShape(temporaryRail.type.getShape()));
					final Point2D point2 = Util.getPoint2(temporaryRail);
					g2.fill(new Rectangle2D.Double(point2.getX() - 2, point2.getY() - 2, 4, 4));
				}
			}
		};
		panel.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(final MouseEvent e) {
			}

			public void mouseMoved(final MouseEvent e) {
				final Point2D mouseLocation = new Point2D.Double(e.getX(), panel.getHeight() - e.getY());

				double minDistance = 100;
				temporaryRail.location = null;
				for (final Rail rail : rails) {
					if (rail.location.distance(mouseLocation.getX(), mouseLocation.getY()) < minDistance) {
						final Rail rail1 = new Rail(temporaryRail.type, rail.location, rail.angle - Math.PI);
						final Point2D point2 = Util.getPoint2(rail1);
						final double angle = rail.angle - temporaryRail.type.getAngle();
						final Rail rail2 = new Rail(temporaryRail.type, new Point2D.Double(rail.location.getX()
								- Util.getPoint(temporaryRail.type, angle).getX(), rail.location.getY()
								- Util.getPoint(temporaryRail.type, angle).getY()), angle);
						if (mouseLocation.distance(point2) < mouseLocation.distance(rail2.location)) {
							temporaryRail.location = rail1.location;
							temporaryRail.angle = rail1.angle;
						} else {
							temporaryRail.location = rail2.location;
							temporaryRail.angle = rail2.angle;
						}
						minDistance = rail.location.distance(mouseLocation.getX(), mouseLocation.getY());
					}
					final Point2D point2 = Util.getPoint2(rail);
					if (point2.distance(mouseLocation.getX(), mouseLocation.getY()) < minDistance) {
						temporaryRail.location = point2;
						temporaryRail.angle = rail.angle + rail.type.getAngle();
						minDistance = point2.distance(mouseLocation.getX(), mouseLocation.getY());
					}
				}
				panel.repaint();
			}
		});
		panel.addMouseListener(new MouseListener() {

			public void mouseReleased(final MouseEvent e) {
			}

			public void mousePressed(final MouseEvent e) {
			}

			public void mouseExited(final MouseEvent e) {
			}

			public void mouseEntered(final MouseEvent e) {
			}

			public void mouseClicked(final MouseEvent e) {
				if (temporaryRail.location != null) {
					rails.add(new Rail(temporaryRail));
				}
				panel.repaint();
			}

		});
		frame.add(panel);

		frame.setVisible(true);
	}

}
