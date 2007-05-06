import java.awt.geom.Point2D;

public class Util {

	public static Point2D getPoint2(final Rail rail) {
		return new Point2D.Double(rail.location.getX() + rail.type.getPoint().distance(0, 0)
				* Math.cos(Math.atan2(rail.type.getPoint().getY(), rail.type.getPoint().getX()) + rail.angle),
				rail.location.getY() + rail.type.getPoint().distance(0, 0)
						* Math.sin(Math.atan2(rail.type.getPoint().getY(), rail.type.getPoint().getX()) + rail.angle));
	}

	public static Point2D getPoint(final RailType type, final double angle) {
		return new Point2D.Double(type.getPoint().distance(0, 0)
				* Math.cos(Math.atan2(type.getPoint().getY(), type.getPoint().getX()) + angle), type.getPoint()
				.distance(0, 0)
				* Math.sin(Math.atan2(type.getPoint().getY(), type.getPoint().getX()) + angle));
	}

}
