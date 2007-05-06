import java.awt.Shape;
import java.awt.geom.GeneralPath;

/**
 * 短いS字曲線レールの実装です。
 * @author zenjiro
 */
public class ShortSCurveRail implements RailType {

	public Point getPoint() {
		return new Point(100, 50);
	}

	public Shape getSkelton() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(0, 0);
		path.curveTo(50, 0, 50, 50, 100, 50);
		return path;
	}

	public double getAngle() {
		return 0;
	}

	public Shape getShape() {
		final GeneralPath path = new GeneralPath();
		final float fatness = (float) (Const.RAIL_WIDTH / 4);
		path.moveTo(0, -Const.RAIL_WIDTH / 2);
		path.curveTo(50 + fatness, -Const.RAIL_WIDTH / 2, 50 + fatness, 50 - Const.RAIL_WIDTH / 2, 100, 50 - Const.RAIL_WIDTH / 2);
		path.moveTo(100, 50 + Const.RAIL_WIDTH / 2);
		path.curveTo(50 - fatness, 50 + Const.RAIL_WIDTH / 2, 50 - fatness, Const.RAIL_WIDTH / 2, 0, Const.RAIL_WIDTH / 2);
		return path;
	}

	public Shape getFill() {
		final GeneralPath path = new GeneralPath();
		final float fatness = (float) (Const.RAIL_WIDTH / 4);
		path.moveTo(0, -Const.RAIL_WIDTH / 2);
		path.curveTo(50 + fatness, -Const.RAIL_WIDTH / 2, 50 + fatness, 50 - Const.RAIL_WIDTH / 2, 100, 50 - Const.RAIL_WIDTH / 2);
		path.lineTo(100, 50 + Const.RAIL_WIDTH / 2);
		path.curveTo(50 - fatness, 50 + Const.RAIL_WIDTH / 2, 50 - fatness, Const.RAIL_WIDTH / 2, 0, Const.RAIL_WIDTH / 2);
		path.closePath();
		return path;
	}
}
