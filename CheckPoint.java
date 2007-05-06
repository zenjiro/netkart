/**
 * チェックポイントをカプセル化するクラスです。
 * @author zenjiro
 */
public class CheckPoint {

	/**
	 * 位置
	 */
	public Point location;

	/**
	 * 向き
	 */
	public double angle;

	@Override
	public String toString() {
		return "(" + this.location + ", " + this.angle + ")";
	}
	
	/**
	 * デフォルトコンストラクタです。
	 */
	public CheckPoint() {
		this.location = null;
		this.angle = 0;
	}
	
	/**
	 * コピーコンストラクタです。
	 * @param checkPoint チェックポイント
	 */
	public CheckPoint(final CheckPoint checkPoint) {
		this.location = checkPoint.location;
		this.angle = checkPoint.angle;
	}

	/**
	 * @return 向き
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * @param angle 向き
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	/**
	 * @return 位置
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @param location 位置
	 */
	public void setLocation(Point location) {
		this.location = location;
	}
}
