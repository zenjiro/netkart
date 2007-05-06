import java.awt.geom.Point2D;

public class Rail {

	public RailType type;

	public Point2D location;

	public double angle;

	public Rail(final RailType type, final Point2D location, final double angle) {
		this.type = type;
		this.location = location;
		this.angle = angle;
	}
	
	public Rail(final Rail rail) {
		this.type = rail.type;
		this.location = rail.location;
		this.angle = rail.angle;
	}

	@Override
	public String toString() {
		return "(" + type.getClass().getName() + ", " + this.location + ", " + this.angle + ")";
	}
}
