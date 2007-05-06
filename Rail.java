/**
 * レールをカプセル化するクラスです。
 * @author zenjiro
 */
public class Rail {

	/**
	 * レールの種類
	 */
	public RailType type;

	/**
	 * レールの位置
	 */
	public Point location;

	/**
	 * レールの向き[ラジアン]
	 */
	public double angle;

	/**
	 * 裏返すかどうか
	 */
	public boolean isReverse;

	/**
	 * デフォルトコンストラクタです。
	 */
	public Rail() {
		this.type = new StraightRail();
		this.location = null;
		this.angle = 0;
		this.isReverse = false;
	}

	/**
	 * コンストラクタです。
	 * @param type レールの種類
	 * @param location レールの位置
	 * @param angle レールの向き[ラジアン]
	 * @param isReverse 裏返すかどうか
	 */
	public Rail(final RailType type, final Point location, final double angle, final boolean isReverse) {
		this.type = type;
		this.location = location;
		this.angle = angle;
		this.isReverse = isReverse;
	}

	/**
	 * コピーコンストラクタです。
	 * @param rail レール
	 */
	public Rail(final Rail rail) {
		this.type = rail.type;
		this.location = rail.location;
		this.angle = rail.angle;
		this.isReverse = rail.isReverse;
	}

	@Override
	public String toString() {
		return "(" + this.type.getClass().getName() + ", " + this.location + ", " + this.angle + ", " + this.isReverse
				+ ")";
	}

	/**
	 * @return レールの向き[ラジアン]
	 */
	public double getAngle() {
		return this.angle;
	}

	/**
	 * @param angle レールの向き[ラジアン]
	 */
	public void setAngle(final double angle) {
		this.angle = angle;
	}

	/**
	 * @return レールの位置
	 */
	public Point getLocation() {
		return this.location;
	}

	/**
	 * @param location レールの位置
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * @return レールの種類
	 */
	public RailType getType() {
		return this.type;
	}

	/**
	 * @param type レールの種類
	 */
	public void setType(final RailType type) {
		this.type = type;
	}

	/**
	 * @return 裏返すかどうか
	 */
	public boolean isReverse() {
		return this.isReverse;
	}

	/**
	 * @param isReverse 裏返すかどうか
	 */
	public void setReverse(boolean isReverse) {
		this.isReverse = isReverse;
	}

}
