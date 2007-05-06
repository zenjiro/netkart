import java.awt.geom.Point2D;

/**
 * 2次元の点をカプセル化するクラスです。
 * @author zenjiro
 */
public class Point {

	/**
	 * x座標
	 */
	private double x;

	/**
	 * y座標
	 */
	private double y;

	/**
	 * デフォルトコンストラクタです。
	 */
	public Point() {
	}

	/**
	 * コンストラクタです。
	 * @param x x座標
	 * @param y y座標
	 */
	public Point(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return x座標
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x x座標
	 */
	public void setX(final double x) {
		this.x = x;
	}

	/**
	 * @return y座標
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y y座標
	 */
	public void setY(final double y) {
		this.y = y;
	}

	/**
	 * @param point 点
	 * @return 指定した点までの距離
	 */
	public double distance(final Point point) {
		return Point2D.distance(this.x, this.y, point.x, point.y);
	}

	/**
	 * @param point 点
	 * @return 指定した点までの距離
	 */
	public double distance(final Point2D point) {
		return point.distance(this.x, this.y);
	}

	/**
	 * @param x x座標
	 * @param y y座標
	 * @return 指定した点までの距離
	 */
	public double distance(final double x, final double y) {
		return Point2D.distance(this.x, this.y, x, y);
	}

	/**
	 * @return Point2Dオブジェクト
	 */
	public Point2D toPoint2D() {
		return new Point2D.Double(this.x, this.y);
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}
