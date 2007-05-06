import java.awt.Shape;


/**
 * レールの種類を表すインターフェイスです。
 * @author zenjiro
 */
public interface RailType {
	
	/**
	 * @return レールの中心線
	 */
	public Shape getSkelton();
	
	/**
	 * @return レールの外側線
	 */
	public Shape getShape();

	/**
	 * @return レールの塗りつぶし部
	 */
	public Shape getFill();
	
	/**
	 * @return レールの起点
	 */
	public Point getPoint();

	/**
	 * @return レールの脱出角度[ラジアン]
	 */
	public double getAngle();
	
}
