import java.awt.Shape;
import java.awt.geom.Point2D;


public interface RailType {
	
	public Shape getShape();
	
	public Point2D getPoint();

	public double getAngle();
	
}
