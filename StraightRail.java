import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


public class StraightRail implements RailType {

	public Point2D getPoint() {
		return new Point2D.Double(100, 0);
	}

	public Shape getShape() {
		return new Line2D.Double(new Point2D.Double(), getPoint());
	}

	public double getAngle() {
		return 0;
	}

}
