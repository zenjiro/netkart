import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;


/**
 * 直線レールの実装です。
 * @author zenjiro
 */
public class StraightRail implements RailType {

	public Point getPoint() {
		return new Point(100, 0);
	}

	public Shape getSkelton() {
		return new Line2D.Double(0, 0, 100, 0);
	}

	public double getAngle() {
		return 0;
	}

	public Shape getShape() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(0, -Const.RAIL_WIDTH / 2);
		path.lineTo(100, -Const.RAIL_WIDTH / 2);
		path.moveTo(100, Const.RAIL_WIDTH / 2);
		path.lineTo(0, Const.RAIL_WIDTH / 2);
		return path;
	}

	public Shape getFill() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(0, -Const.RAIL_WIDTH / 2);
		path.lineTo(100, -Const.RAIL_WIDTH / 2);
		path.lineTo(100, Const.RAIL_WIDTH / 2);
		path.lineTo(0, Const.RAIL_WIDTH / 2);
		path.closePath();
		return path;
	}

}
