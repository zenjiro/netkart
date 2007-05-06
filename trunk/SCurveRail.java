import java.awt.Shape;
import java.awt.geom.GeneralPath;

/**
 * S字曲線レールの実装です。
 * @author zenjiro
 */
public class SCurveRail implements RailType {

	public Point getPoint() {
		return new Point(200, 100);
	}

	public Shape getSkelton() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(0, 0);
		path.curveTo(100, 0, 100, 100, 200, 100);
		return path;
	}

	public double getAngle() {
		return 0;
	}

	public Shape getShape() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(0, -Const.RAIL_WIDTH / 2);
		path.curveTo(100 + Const.RAIL_WIDTH / 4, -Const.RAIL_WIDTH / 2, 100 + Const.RAIL_WIDTH / 4,
				100 - Const.RAIL_WIDTH / 2, 200, 100 - Const.RAIL_WIDTH / 2);
		path.moveTo(200, 100 + Const.RAIL_WIDTH / 2);
		path.curveTo(100 - Const.RAIL_WIDTH / 4, 100 + Const.RAIL_WIDTH / 2, 100 - Const.RAIL_WIDTH / 4,
				Const.RAIL_WIDTH / 2, 0, Const.RAIL_WIDTH / 2);
		return path;
	}

	public Shape getFill() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(0, -Const.RAIL_WIDTH / 2);
		path.curveTo(100 + Const.RAIL_WIDTH / 4, -Const.RAIL_WIDTH / 2, 100 + Const.RAIL_WIDTH / 4,
				100 - Const.RAIL_WIDTH / 2, 200, 100 - Const.RAIL_WIDTH / 2);
		path.lineTo(200, 100 + Const.RAIL_WIDTH / 2);
		path.curveTo(100 - Const.RAIL_WIDTH / 4, 100 + Const.RAIL_WIDTH / 2, 100 - Const.RAIL_WIDTH / 4,
				Const.RAIL_WIDTH / 2, 0, Const.RAIL_WIDTH / 2);
		path.closePath();
		return path;
	}
}
