import java.awt.Shape;
import java.awt.geom.GeneralPath;

/**
 * S字曲線レールの実装です。
 * @author zenjiro
 */
public class LongSCurveRail implements RailType {

	public Point getPoint() {
		return new Point(400, 100);
	}

	public Shape getSkelton() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(0, 0);
		path.curveTo(200, 0, 200, 100, 400, 100);
		return path;
	}

	public double getAngle() {
		return 0;
	}

	public Shape getShape() {
		final GeneralPath path = new GeneralPath();
		final int fatness = Const.RAIL_WIDTH / 4;
		path.moveTo(0, -Const.RAIL_WIDTH / 2);
		path.curveTo(200 + fatness, -Const.RAIL_WIDTH / 2, 200 + fatness,
				100 - Const.RAIL_WIDTH / 2, 400, 100 - Const.RAIL_WIDTH / 2);
		path.moveTo(400, 100 + Const.RAIL_WIDTH / 2);
		path.curveTo(200 - fatness, 100 + Const.RAIL_WIDTH / 2, 200 - fatness,
				Const.RAIL_WIDTH / 2, 0, Const.RAIL_WIDTH / 2);
		return path;
	}

	public Shape getFill() {
		final GeneralPath path = new GeneralPath();
		final int fatness = Const.RAIL_WIDTH / 4;
		path.moveTo(0, -Const.RAIL_WIDTH / 2);
		path.curveTo(200 + fatness, -Const.RAIL_WIDTH / 2, 200 + fatness,
				100 - Const.RAIL_WIDTH / 2, 400, 100 - Const.RAIL_WIDTH / 2);
		path.lineTo(400, 100 + Const.RAIL_WIDTH / 2);
		path.curveTo(200 - fatness, 100 + Const.RAIL_WIDTH / 2, 200 - fatness,
				Const.RAIL_WIDTH / 2, 0, Const.RAIL_WIDTH / 2);
		path.closePath();
		return path;
	}
}
