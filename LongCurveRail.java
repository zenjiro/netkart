import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;

/**
 * 長い曲線レールの実装です.
 * @author zenjiro
 */
public class LongCurveRail implements RailType {

	public Point getPoint() {
		return new Point(200 / Math.sqrt(2), 200 - 200 / Math.sqrt(2));
	}

	public Shape getSkelton() {
		return new Arc2D.Double(-200, 0, 400, 400, 45, 45, Arc2D.OPEN);
	}

	public double getAngle() {
		return Math.PI / 4;
	}

	public Shape getShape() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(0, -Const.RAIL_WIDTH / 2);
		path.quadTo((float) ((200 + Const.RAIL_WIDTH / 2) / 2 / Math.sqrt(2)), -Const.RAIL_WIDTH / 2,
				(float) ((200 + Const.RAIL_WIDTH / 2) / Math.sqrt(2)), (float) (200 - (200 + Const.RAIL_WIDTH / 2)
						/ Math.sqrt(2)));
		path.moveTo((float) ((200 - Const.RAIL_WIDTH / 2) / Math.sqrt(2)), (float) (200 - (200 - Const.RAIL_WIDTH / 2)
				/ Math.sqrt(2)));
		path.quadTo((float) ((200 - Const.RAIL_WIDTH / 2) / 2 / Math.sqrt(2)), Const.RAIL_WIDTH / 2, 0,
				Const.RAIL_WIDTH / 2);
		return path;
	}

	public Shape getFill() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(0, -Const.RAIL_WIDTH / 2);
		path.quadTo((float) ((200 + Const.RAIL_WIDTH / 2) / 2 / Math.sqrt(2)), -Const.RAIL_WIDTH / 2,
				(float) ((200 + Const.RAIL_WIDTH / 2) / Math.sqrt(2)), (float) (200 - (200 + Const.RAIL_WIDTH / 2)
						/ Math.sqrt(2)));
		path.lineTo((float) ((200 - Const.RAIL_WIDTH / 2) / Math.sqrt(2)), (float) (200 - (200 - Const.RAIL_WIDTH / 2)
				/ Math.sqrt(2)));
		path.quadTo((float) ((200 - Const.RAIL_WIDTH / 2) / 2 / Math.sqrt(2)), Const.RAIL_WIDTH / 2, 0,
				Const.RAIL_WIDTH / 2);
		path.closePath();
		return path;
	}
}
