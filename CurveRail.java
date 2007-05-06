import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;


public class CurveRail implements RailType {

	public Point2D getPoint() {
		return new Point2D.Double(100 / Math.sqrt(2), 100 - 100 / Math.sqrt(2));
	}

	public Shape getShape() {
		return new Arc2D.Double(-100, 0, 200, 200, 45, 45, Arc2D.OPEN);
	}

	public double getAngle() {
		return Math.PI / 4;
	}

}
