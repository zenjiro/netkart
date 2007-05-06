import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * ステージエディタです。
 * @author zenjiro
 *
 * - スタート位置を作りたい。
 * - ステージの大きさを固定にしたい。
 * - 表示倍率を変更できるようにしたい。
 */
public class Editor {
	/**
	 * モードの列挙型です。
	 * @author zenjiro
	 */
	public static enum Mode {
		/**
		 * レールを追加するモード
		 */
		ADD_RAIL,
		/**
		 * レールを削除するモード
		 */
		DELETE_RAIL,
		/**
		 * チェックポイントを追加するモード
		 */
		ADD_CHECK_POINT,
	}

	/**
	 * モード
	 */
	private static Mode mode;

	/**
	 * 削除するレール
	 */
	private static Rail deleteRail;

	/**
	 * 編集中のファイル
	 */
	private static File file;

	/**
	 * 変更されたかどうか
	 */
	private static boolean isModified;

	/**
	 * メインメソッドです。
	 * @param args
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(final String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		final Stage stage = new Stage();
		final Rail temporaryRail = new Rail();
		final CheckPoint temporaryCheckPoint = new CheckPoint();
		mode = Mode.ADD_RAIL;

		final JFrame frame = new JFrame(Util.getTitle(file, isModified));
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		frame.setLocationByPlatform(true);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLayout(new BorderLayout());

		final JPanel panel = new JPanel() {

			@Override
			protected void paintComponent(final Graphics g) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());

				final Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.translate(0, this.getHeight());
				g2.scale(1, -1);
				g2.setFont(Const.EDITOR_FONT);
				g2.setStroke(new BasicStroke(1.2f));
				for (final Rail rail : stage.getRails()) {
					final AffineTransform transform = new AffineTransform();
					transform.translate(rail.location.getX(), rail.location.getY());
					transform.rotate(rail.angle);
					if (rail.isReverse) {
						transform.scale(1, -1);
					}
					g2.setColor(new Color(240, 240, 240));
					g2.fill(transform.createTransformedShape(rail.type.getFill()));
					g2.setColor(Color.BLACK);
					g2.draw(transform.createTransformedShape(rail.type.getShape()));
				}
				{
					int i = 0;
					for (final CheckPoint checkPoint : stage.getCheckPoints()) {
						final AffineTransform transform = new AffineTransform();
						transform.translate(checkPoint.location.getX(), checkPoint.location.getY());
						transform.rotate(checkPoint.angle);
						final Shape rectangle = transform.createTransformedShape(new Rectangle2D.Double(-10,
								-Const.RAIL_WIDTH / 2, 20, Const.RAIL_WIDTH));
						g2.setColor(new Color(250, 250, 250));
						g2.fill(rectangle);
						g2.setColor(Color.BLACK);
						g2.draw(rectangle);
						if (i == 0) {
							final GeneralPath arrow = new GeneralPath();
							arrow.moveTo(-20, 0);
							arrow.lineTo(-40, -20);
							arrow.lineTo(-40, -10);
							arrow.lineTo(-60, -10);
							arrow.lineTo(-60, 10);
							arrow.lineTo(-40, 10);
							arrow.lineTo(-40, 20);
							arrow.closePath();
							g2.fill(transform.createTransformedShape(arrow));
						}
						final Shape string = g2.getFont().createGlyphVector(g2.getFontRenderContext(),
								String.valueOf(i)).getOutline();
						transform.rotate(-checkPoint.angle);
						transform
								.translate(-string.getBounds2D().getWidth() / 2, -string.getBounds2D().getHeight() / 2);
						transform.scale(1, -1);
						g2.fill(transform.createTransformedShape(string));
						i++;
					}
				}
				switch (mode) {
				case ADD_RAIL:
					g2.setStroke(new BasicStroke(1.6f));
					if (temporaryRail.location != null) {
						final AffineTransform transform = new AffineTransform();
						transform.translate(temporaryRail.location.getX(), temporaryRail.location.getY());
						transform.rotate(temporaryRail.angle);
						if (temporaryRail.isReverse) {
							transform.scale(1, -1);
						}
						g2.setColor(Const.COLOR_ADD_FILL);
						g2.fill(transform.createTransformedShape(temporaryRail.type.getFill()));
						g2.setColor(Const.COLOR_ADD_DRAW);
						g2.draw(transform.createTransformedShape(temporaryRail.type.getShape()));
					}
					break;
				case DELETE_RAIL:
					g2.setStroke(new BasicStroke(2));
					if (deleteRail != null) {
						final AffineTransform transform = new AffineTransform();
						transform.translate(deleteRail.location.getX(), deleteRail.location.getY());
						transform.rotate(deleteRail.angle);
						if (deleteRail.isReverse) {
							transform.scale(1, -1);
						}
						g2.setColor(Const.COLOR_DELETE_FILL);
						g2.fill(transform.createTransformedShape(deleteRail.type.getFill()));
						g2.setColor(Const.COLOR_DELETE_DRAW);
						g2.draw(transform.createTransformedShape(deleteRail.type.getShape()));
					}
					break;
				case ADD_CHECK_POINT:
					g2.setStroke(new BasicStroke(1));
					if (temporaryCheckPoint.location != null) {
						final AffineTransform transform = new AffineTransform();
						transform.translate(temporaryCheckPoint.location.getX(), temporaryCheckPoint.location.getY());
						transform.rotate(temporaryCheckPoint.angle);
						final Shape rectangle = transform.createTransformedShape(new Rectangle2D.Double(-10,
								-Const.RAIL_WIDTH / 2, 20, Const.RAIL_WIDTH));
						g2.setColor(Const.COLOR_ADD_FILL);
						g2.fill(rectangle);
						g2.setColor(Const.COLOR_ADD_DRAW);
						g2.draw(rectangle);
						if (stage.getCheckPoints().isEmpty()) {
							final GeneralPath arrow = new GeneralPath();
							arrow.moveTo(-20, 0);
							arrow.lineTo(-40, -20);
							arrow.lineTo(-40, -10);
							arrow.lineTo(-60, -10);
							arrow.lineTo(-60, 10);
							arrow.lineTo(-40, 10);
							arrow.lineTo(-40, 20);
							arrow.closePath();
							g2.fill(transform.createTransformedShape(arrow));
						}
						final Shape string = g2.getFont().createGlyphVector(g2.getFontRenderContext(),
								String.valueOf(stage.getCheckPoints().size())).getOutline();
						transform.rotate(-temporaryCheckPoint.angle);
						transform
								.translate(-string.getBounds2D().getWidth() / 2, -string.getBounds2D().getHeight() / 2);
						transform.scale(1, -1);
						g2.fill(transform.createTransformedShape(string));
					}
					break;
				}
				g2.setStroke(new BasicStroke(1));
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .1f));
				g2.setColor(Color.BLACK);
				for (int x = Const.EDITOR_GRID / 2; x < getWidth(); x += Const.EDITOR_GRID) {
					g2.drawLine(x, 0, x, getHeight());
				}
				for (int y = Const.EDITOR_GRID / 2; y < getHeight(); y += Const.EDITOR_GRID) {
					g2.drawLine(0, y, getWidth(), y);
				}
			}
		};
		panel.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(final MouseEvent e) {
			}

			public void mouseMoved(final MouseEvent e) {
				final Point mouseLocation = new Point(e.getX(), panel.getHeight() - e.getY());

				double minDistance;
				switch (mode) {
				case ADD_RAIL:
					temporaryRail.location = null;
					if (stage.getRails().isEmpty()) {
						final Point point = new Point(((int) ((mouseLocation.getX()) / Const.EDITOR_GRID))
								* Const.EDITOR_GRID,
								((int) ((mouseLocation.getY() + Const.EDITOR_GRID / 2) / Const.EDITOR_GRID))
										* Const.EDITOR_GRID);
						temporaryRail.location = point;
					} else {
						minDistance = Const.EDITOR_GRID;
						for (final Rail rail : stage.getRails()) {
							if (rail.location.distance(mouseLocation.getX(), mouseLocation.getY()) < minDistance) {
								final Rail rail1 = new Rail(temporaryRail.type, rail.location, rail.angle - Math.PI,
										false);
								final Point point2 = Util.getPoint2(rail1);
								final Rail rail2 = new Rail(temporaryRail.type, rail.location, rail.angle - Math.PI,
										true);
								if (mouseLocation.distance(point2) < mouseLocation.distance(Util.getPoint2(rail2))) {
									temporaryRail.location = rail1.location;
									temporaryRail.angle = rail1.angle;
									temporaryRail.isReverse = rail1.isReverse;
								} else {
									temporaryRail.location = rail2.location;
									temporaryRail.angle = rail2.angle;
									temporaryRail.isReverse = rail2.isReverse;
								}
								minDistance = rail.location.distance(mouseLocation.getX(), mouseLocation.getY());
							}
							final Point point2 = Util.getPoint2(rail);
							if (point2.distance(mouseLocation.getX(), mouseLocation.getY()) < minDistance) {
								final Rail rail1 = new Rail(temporaryRail.type, point2, rail.angle
										+ (rail.isReverse ? -rail.type.getAngle() : rail.type.getAngle()), false);
								final Point point3 = Util.getPoint2(rail1);
								final Rail rail2 = new Rail(temporaryRail.type, point2, rail.angle
										+ (rail.isReverse ? -rail.type.getAngle() : rail.type.getAngle()), true);
								if (mouseLocation.distance(point3) < mouseLocation.distance(Util.getPoint2(rail2))) {
									temporaryRail.location = rail1.location;
									temporaryRail.angle = rail1.angle;
									temporaryRail.isReverse = rail1.isReverse;
								} else {
									temporaryRail.location = rail2.location;
									temporaryRail.angle = rail2.angle;
									temporaryRail.isReverse = rail2.isReverse;
								}
								minDistance = point2.distance(mouseLocation.getX(), mouseLocation.getY());
							}
						}
					}
					break;
				case DELETE_RAIL:
					deleteRail = null;
					for (final Rail rail : stage.getRails()) {
						if (Util.getFill(rail).contains(mouseLocation.toPoint2D())) {
							deleteRail = rail;
							break;
						}
					}
					break;
				case ADD_CHECK_POINT:
					minDistance = Const.EDITOR_GRID;
					temporaryCheckPoint.location = null;
					for (final Rail rail : stage.getRails()) {
						final Point point1 = rail.location;
						if (point1.distance(mouseLocation) < minDistance) {
							temporaryCheckPoint.location = point1;
							temporaryCheckPoint.angle = rail.angle
									+ (Util.getFill(rail).contains(mouseLocation.toPoint2D()) ? Math.PI : 0);
							minDistance = point1.distance(mouseLocation);
						}
						final Point point2 = Util.getPoint2(rail);
						if (point2.distance(mouseLocation) < minDistance) {
							temporaryCheckPoint.location = point2;
							temporaryCheckPoint.angle = rail.angle
									+ (rail.isReverse ? -rail.type.getAngle() : rail.type.getAngle())
									+ (Util.getFill(rail).contains(mouseLocation.toPoint2D()) ? 0 : Math.PI);
							minDistance = point2.distance(mouseLocation);
						}
					}
					break;
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
				switch (mode) {
				case ADD_RAIL:
					if (e.getButton() == MouseEvent.BUTTON1) {
						if (temporaryRail.location != null) {
							stage.getRails().add(new Rail(temporaryRail));
							temporaryRail.location = null;
							isModified = true;
							frame.setTitle(Util.getTitle(file, isModified));
						}
					} else {
						if (stage.getRails().size() > 0) {
							stage.getRails().remove(stage.getRails().size() - 1);
							temporaryRail.location = null;
							if (stage.getRails().isEmpty()) {
								temporaryRail.angle = 0;
							}
							isModified = true;
							frame.setTitle(Util.getTitle(file, isModified));
						}
					}
					break;
				case DELETE_RAIL:
					if (deleteRail != null) {
						stage.getRails().remove(deleteRail);
						deleteRail = null;
						isModified = true;
						frame.setTitle(Util.getTitle(file, isModified));
					}
					break;
				case ADD_CHECK_POINT:
					if (e.getButton() == MouseEvent.BUTTON1) {
						if (temporaryCheckPoint.location != null) {
							stage.getCheckPoints().add(new CheckPoint(temporaryCheckPoint));
							temporaryCheckPoint.location = null;
							isModified = true;
							frame.setTitle(Util.getTitle(file, isModified));
						}
					} else {
						if (stage.getCheckPoints().size() > 0) {
							stage.getCheckPoints().remove(stage.getCheckPoints().size() - 1);
							temporaryCheckPoint.location = null;
							isModified = true;
							frame.setTitle(Util.getTitle(file, isModified));
						}
					}
					break;
				}
				panel.repaint();
			}

		});
		frame.add(panel, BorderLayout.CENTER);

		final JMenuBar menuBar = new JMenuBar();
		final JMenu fileMenu = new JMenu("ファイル(F)");
		fileMenu.setMnemonic('F');
		final JMenuItem newItem = new JMenuItem("新規(N)");
		newItem.setMnemonic('N');
		newItem.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
		fileMenu.add(newItem);
		final JMenuItem openItem = new JMenuItem("開く(O)...");
		openItem.setMnemonic('O');
		openItem.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
		fileMenu.add(openItem);
		fileMenu.addSeparator();
		final JMenuItem saveItem = new JMenuItem("保存(S)");
		saveItem.setMnemonic('S');
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
		fileMenu.add(saveItem);
		final JMenuItem saveAsItem = new JMenuItem("別名で保存(A)...");
		saveAsItem.setMnemonic('A');
		fileMenu.add(saveAsItem);
		fileMenu.addSeparator();
		final JMenuItem exitItem = new JMenuItem("終了(X)");
		exitItem.setMnemonic('X');
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				frame.dispose();
			}
		});
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);
		final JMenu toolMenu = new JMenu("ツール(T)");
		toolMenu.setMnemonic('T');
		menuBar.add(toolMenu);
		final ButtonGroup menuItemGroup = new ButtonGroup();
		final JRadioButtonMenuItem straightItem = new JRadioButtonMenuItem("直線(1)", true);
		straightItem.setMnemonic('1');
		straightItem.setAccelerator(KeyStroke.getKeyStroke('1'));
		toolMenu.add(straightItem);
		menuItemGroup.add(straightItem);
		final JRadioButtonMenuItem straightItem2 = new JRadioButtonMenuItem("半直線(2)");
		straightItem2.setMnemonic('2');
		straightItem2.setAccelerator(KeyStroke.getKeyStroke('2'));
		toolMenu.add(straightItem2);
		menuItemGroup.add(straightItem2);
		final JRadioButtonMenuItem straightItem3 = new JRadioButtonMenuItem("倍直線(3)");
		straightItem3.setMnemonic('3');
		straightItem3.setAccelerator(KeyStroke.getKeyStroke('3'));
		toolMenu.add(straightItem3);
		menuItemGroup.add(straightItem3);
		final JRadioButtonMenuItem curveItem = new JRadioButtonMenuItem("曲線(4)");
		curveItem.setMnemonic('4');
		curveItem.setAccelerator(KeyStroke.getKeyStroke('4'));
		toolMenu.add(curveItem);
		menuItemGroup.add(curveItem);
		final JRadioButtonMenuItem curveItem2 = new JRadioButtonMenuItem("半曲線(5)");
		curveItem2.setMnemonic('5');
		curveItem2.setAccelerator(KeyStroke.getKeyStroke('5'));
		toolMenu.add(curveItem2);
		menuItemGroup.add(curveItem2);
		final JRadioButtonMenuItem curveItem3 = new JRadioButtonMenuItem("倍曲線(6)");
		curveItem3.setMnemonic('6');
		curveItem3.setAccelerator(KeyStroke.getKeyStroke('6'));
		toolMenu.add(curveItem3);
		menuItemGroup.add(curveItem3);
		final JRadioButtonMenuItem sCurveItem = new JRadioButtonMenuItem("S字曲線(7)");
		sCurveItem.setMnemonic('7');
		sCurveItem.setAccelerator(KeyStroke.getKeyStroke('7'));
		toolMenu.add(sCurveItem);
		menuItemGroup.add(sCurveItem);
		final JRadioButtonMenuItem sCurveItem2 = new JRadioButtonMenuItem("半S字曲線(8)");
		sCurveItem2.setMnemonic('8');
		sCurveItem2.setAccelerator(KeyStroke.getKeyStroke('8'));
		toolMenu.add(sCurveItem2);
		menuItemGroup.add(sCurveItem2);
		final JRadioButtonMenuItem sCurveItem3 = new JRadioButtonMenuItem("倍S字曲線(9)");
		sCurveItem3.setMnemonic('9');
		sCurveItem3.setAccelerator(KeyStroke.getKeyStroke('9'));
		toolMenu.add(sCurveItem3);
		menuItemGroup.add(sCurveItem3);
		toolMenu.addSeparator();
		final JRadioButtonMenuItem checkPointItem = new JRadioButtonMenuItem("通過点(C)");
		checkPointItem.setMnemonic('C');
		checkPointItem.setAccelerator(KeyStroke.getKeyStroke('c'));
		toolMenu.add(checkPointItem);
		menuItemGroup.add(checkPointItem);
		toolMenu.addSeparator();
		final JRadioButtonMenuItem deleteItem = new JRadioButtonMenuItem("削除(D)");
		deleteItem.setMnemonic('D');
		deleteItem.setAccelerator(KeyStroke.getKeyStroke('d'));
		toolMenu.add(deleteItem);
		menuItemGroup.add(deleteItem);
		frame.add(menuBar, BorderLayout.NORTH);

		final JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
		final ButtonGroup toolBarGroup = new ButtonGroup();
		final JRadioButton straightRadio = new JRadioButton("直線(1)", true);
		straightRadio.setMnemonic('1');
		straightRadio.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				straightItem.doClick();
			}
		});
		toolBar.add(straightRadio);
		toolBarGroup.add(straightRadio);
		final JRadioButton straightRadio2 = new JRadioButton("半直線(2)");
		straightRadio2.setMnemonic('2');
		straightRadio2.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				straightItem2.doClick();
			}
		});
		toolBar.add(straightRadio2);
		toolBarGroup.add(straightRadio2);
		final JRadioButton straightRadio3 = new JRadioButton("倍直線(3)");
		straightRadio3.setMnemonic('3');
		straightRadio3.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				straightItem3.doClick();
			}
		});
		toolBar.add(straightRadio3);
		toolBarGroup.add(straightRadio3);
		final JRadioButton curveRadio = new JRadioButton("曲線(4)");
		curveRadio.setMnemonic('4');
		curveRadio.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				curveItem.doClick();
			}
		});
		toolBar.add(curveRadio);
		toolBarGroup.add(curveRadio);
		final JRadioButton curveRadio2 = new JRadioButton("半曲線(5)");
		curveRadio2.setMnemonic('5');
		curveRadio2.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				curveItem2.doClick();
			}
		});
		toolBar.add(curveRadio2);
		toolBarGroup.add(curveRadio2);
		final JRadioButton curveRadio3 = new JRadioButton("倍曲線(6)");
		curveRadio3.setMnemonic('6');
		curveRadio3.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				curveItem3.doClick();
			}
		});
		toolBar.add(curveRadio3);
		toolBarGroup.add(curveRadio3);
		final JRadioButton sCurveRadio = new JRadioButton("S字曲線(7)");
		sCurveRadio.setMnemonic('7');
		sCurveRadio.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				sCurveItem.doClick();
			}
		});
		toolBar.add(sCurveRadio);
		toolBarGroup.add(sCurveRadio);
		final JRadioButton sCurveRadio2 = new JRadioButton("半S字曲線(8)");
		sCurveRadio2.setMnemonic('8');
		sCurveRadio2.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				sCurveItem2.doClick();
			}
		});
		toolBar.add(sCurveRadio2);
		toolBarGroup.add(sCurveRadio2);
		final JRadioButton sCurveRadio3 = new JRadioButton("倍S字曲線(9)");
		sCurveRadio3.setMnemonic('9');
		sCurveRadio3.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				sCurveItem3.doClick();
			}
		});
		toolBar.add(sCurveRadio3);
		toolBarGroup.add(sCurveRadio3);
		toolBar.addSeparator();
		final JRadioButton checkPointRadio = new JRadioButton("通過点(C)");
		checkPointRadio.setMnemonic('C');
		checkPointRadio.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				checkPointItem.doClick();
			}
		});
		toolBar.add(checkPointRadio);
		toolBarGroup.add(checkPointRadio);
		toolBar.addSeparator();
		final JRadioButton deleteRadio = new JRadioButton("削除(D)");
		deleteRadio.setMnemonic('D');
		deleteRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteItem.doClick();
			}
		});
		toolBar.add(deleteRadio);
		toolBarGroup.add(deleteRadio);
		toolBar.addSeparator();
		final JLabel titleLabel = new JLabel("タイトル(I)");
		titleLabel.setDisplayedMnemonic('I');
		toolBar.add(titleLabel);
		final JTextField titleTextField = new JTextField("", 10);
		titleLabel.setLabelFor(titleTextField);
		titleTextField.addFocusListener(new FocusListener() {
			public void focusLost(final FocusEvent e) {
				titleTextField.select(0, 0);
			}

			public void focusGained(final FocusEvent e) {
				titleTextField.selectAll();
			}
		});
		toolBar.add(titleTextField);
		final JLabel authorLabel = new JLabel("作者(A)");
		authorLabel.setDisplayedMnemonic('A');
		toolBar.add(authorLabel);
		final JTextField authorTextField = new JTextField("", 10);
		authorLabel.setLabelFor(authorTextField);
		authorTextField.addFocusListener(new FocusListener() {
			public void focusLost(final FocusEvent e) {
				authorTextField.select(0, 0);
			}

			public void focusGained(final FocusEvent e) {
				authorTextField.selectAll();
			}
		});
		toolBar.add(authorTextField);
		frame.add(toolBar, BorderLayout.WEST);

		newItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (askIfSave(frame, saveItem, "新規")) {
					file = null;
					stage.getRails().clear();
					stage.getCheckPoints().clear();
					stage.setTitle("");
					stage.setAuthor("");
					titleTextField.setText(stage.getTitle());
					authorTextField.setText(stage.getAuthor());
					isModified = false;
					frame.setTitle(Util.getTitle(file, isModified));
					panel.repaint();
				}
			}
		});
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					if (askIfSave(frame, saveItem, "開く")) {
						final JFileChooser chooser = new JFileChooser(file);
						chooser.addChoosableFileFilter(new NetKartFileFilter());
						if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION
								&& chooser.getSelectedFile() != null) {
							final File openFile = chooser.getSelectedFile().getName().endsWith(".netkart") ? chooser
									.getSelectedFile() : new File(chooser.getSelectedFile().getAbsolutePath()
									+ ".netkart");
							if (openFile.exists()) {
								if (openFile.canRead()) {
									file = openFile;
									final XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));
									stage.setStage((Stage) decoder.readObject());
									decoder.close();
									titleTextField.setText(stage.getTitle());
									authorTextField.setText(stage.getAuthor());
									isModified = false;
									frame.setTitle(Util.getTitle(file, isModified));
								} else {
									JOptionPane.showMessageDialog(frame, "\"" + openFile + "\"を読み込むことができませんでした。", "開く",
											JOptionPane.ERROR_MESSAGE);
								}
							} else {
								JOptionPane.showMessageDialog(frame, "\"" + openFile + "\"が見つかりませんでした。", "開く",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				} catch (final FileNotFoundException exception) {
					exception.printStackTrace();
				}
				panel.repaint();
			}
		});
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (file == null) {
					saveAsItem.doClick();
				} else {
					stage.setTitle(titleTextField.getText());
					stage.setAuthor(authorTextField.getText());
					try {
						final XMLEncoder encoder = new XMLEncoder(new FileOutputStream(file));
						encoder.writeObject(stage);
						encoder.close();
						isModified = false;
						frame.setTitle(Util.getTitle(file, isModified));
					} catch (final IOException exception) {
						exception.printStackTrace();
					}
				}
			}
		});
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser chooser = new JFileChooser(file);
				chooser.addChoosableFileFilter(new NetKartFileFilter());
				if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
					final File saveFile = chooser.getSelectedFile().getName().endsWith(".netkart") ? chooser
							.getSelectedFile() : new File(chooser.getSelectedFile().getAbsolutePath() + ".netkart");
					if (saveFile.isFile()) {
						if (JOptionPane.showConfirmDialog(frame, "\"" + saveFile + "\"は既に存在します。上書きしますか？", "保存",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
							file = saveFile;
							saveItem.doClick();
						}
					} else {
						file = saveFile;
						saveItem.doClick();
					}
				}
			}
		});
		straightItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				straightRadio.setSelected(true);
				temporaryRail.type = new StraightRail();
				if (stage.getRails().isEmpty()) {
					temporaryRail.angle = 0;
					temporaryRail.isReverse = false;
				}
				mode = Mode.ADD_RAIL;
				panel.repaint();
			}
		});
		straightItem2.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				straightRadio2.setSelected(true);
				temporaryRail.type = new ShortStraightRail();
				if (stage.getRails().isEmpty()) {
					temporaryRail.angle = 0;
					temporaryRail.isReverse = false;
				}
				mode = Mode.ADD_RAIL;
				panel.repaint();
			}
		});
		straightItem3.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				straightRadio3.setSelected(true);
				temporaryRail.type = new LongStraightRail();
				if (stage.getRails().isEmpty()) {
					temporaryRail.angle = 0;
					temporaryRail.isReverse = false;
				}
				mode = Mode.ADD_RAIL;
				panel.repaint();
			}
		});
		curveItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				curveRadio.setSelected(true);
				temporaryRail.type = new CurveRail();
				if (stage.getRails().isEmpty()) {
					temporaryRail.angle = 0;
					temporaryRail.isReverse = false;
				}
				mode = Mode.ADD_RAIL;
				panel.repaint();
			}
		});
		curveItem2.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				curveRadio2.setSelected(true);
				temporaryRail.type = new ShortCurveRail();
				if (stage.getRails().isEmpty()) {
					temporaryRail.angle = 0;
					temporaryRail.isReverse = false;
				}
				mode = Mode.ADD_RAIL;
				panel.repaint();
			}
		});
		curveItem3.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				curveRadio3.setSelected(true);
				temporaryRail.type = new LongCurveRail();
				if (stage.getRails().isEmpty()) {
					temporaryRail.angle = 0;
					temporaryRail.isReverse = false;
				}
				mode = Mode.ADD_RAIL;
				panel.repaint();
			}
		});
		sCurveItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				sCurveRadio.setSelected(true);
				temporaryRail.type = new SCurveRail();
				if (stage.getRails().isEmpty()) {
					temporaryRail.angle = 0;
					temporaryRail.isReverse = false;
				}
				mode = Mode.ADD_RAIL;
				panel.repaint();
			}
		});
		sCurveItem2.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				sCurveRadio2.setSelected(true);
				temporaryRail.type = new ShortSCurveRail();
				if (stage.getRails().isEmpty()) {
					temporaryRail.angle = 0;
					temporaryRail.isReverse = false;
				}
				mode = Mode.ADD_RAIL;
				panel.repaint();
			}
		});
		sCurveItem3.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				sCurveRadio3.setSelected(true);
				temporaryRail.type = new LongSCurveRail();
				if (stage.getRails().isEmpty()) {
					temporaryRail.angle = 0;
					temporaryRail.isReverse = false;
				}
				mode = Mode.ADD_RAIL;
				panel.repaint();
			}
		});
		checkPointItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				checkPointRadio.setSelected(true);
				mode = Mode.ADD_CHECK_POINT;
				panel.repaint();
			}
		});
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteRadio.setSelected(true);
				mode = Mode.DELETE_RAIL;
				panel.repaint();
			}
		});
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
				if (askIfSave(frame, saveItem, "終了")) {
					frame.dispose();
				}
			}

			public void windowClosed(final WindowEvent e) {
			}

			public void windowActivated(final WindowEvent e) {
			}
		});

		frame.setVisible(true);
	}

	/**
	 * 保存するかどうかを尋ねます。
	 * @param frame フレーム
	 * @param saveItem 保存するメニュー項目
	 * @param title ダイアログボックスのタイトルバーに表示する文字列
	 * @return 次に進んでも良いかどうか
	 */
	private static boolean askIfSave(final JFrame frame, final JMenuItem saveItem, final String title) {
		if (isModified) {
			switch (JOptionPane.showConfirmDialog(frame, "\"" + (file == null ? "新規ステージ" : file)
					+ "\"は修正されています。保存しますか？", title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)) {
			case JOptionPane.YES_OPTION:
				saveItem.doClick();
				return !isModified;
			case JOptionPane.NO_OPTION:
				return true;
			default:
				return false;
			}
		} else {
			return true;
		}
	}

}
