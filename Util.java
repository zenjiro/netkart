import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;

/**
 * ユーティリティクラスです。
 * @author zenjiro
 */
public class Util {

	/**
	 * @param rail レール
	 * @return レールの終点
	 */
	public static Point getPoint2(final Rail rail) {
		return new Point(rail.location.getX()
				+ rail.type.getPoint().distance(0, 0)
				* Math.cos(Math.atan2(rail.isReverse ? -rail.type.getPoint().getY() : rail.type.getPoint().getY(),
						rail.type.getPoint().getX())
						+ rail.angle), rail.location.getY()
				+ rail.type.getPoint().distance(0, 0)
				* Math.sin(Math.atan2(rail.isReverse ? -rail.type.getPoint().getY() : rail.type.getPoint().getY(),
						rail.type.getPoint().getX())
						+ rail.angle));
	}

	/**
	 * @param type レールの種類
	 * @param angle レールの角度[ラジアン]
	 * @param isReverse 裏返すかどうか
	 * @return レールを原点に指定した角度で置いた場合の終点の座標
	 */
	public static Point getPoint(final RailType type, final double angle, final boolean isReverse) {
		return new Point(type.getPoint().distance(0, 0)
				* Math.cos(Math.atan2(isReverse ? -type.getPoint().getY() : type.getPoint().getY(), type.getPoint()
						.getX())
						+ angle), type.getPoint().distance(0, 0)
				* Math.sin(Math.atan2(isReverse ? -type.getPoint().getY() : type.getPoint().getY(), type.getPoint()
						.getX())
						+ angle));
	}

	/**
	 * @param file 編集中のファイル
	 * @param isModified 変更されたかどうか
	 * @return タイトルバーに表示する文字列
	 */
	public static String getTitle(final File file, final boolean isModified) {
		return (isModified ? "*" : "") + (file == null ? "新規ステージ" : file.toString()) + " - NetKartステージエディタ";
	}

	/**
	 * @param rail レール
	 * @return レールの塗りつぶし
	 */
	public static Shape getFill(final Rail rail) {
		final AffineTransform transform = new AffineTransform();
		transform.translate(rail.location.getX(), rail.location.getY());
		transform.rotate(rail.angle);
		if (rail.isReverse) {
			transform.scale(1, -1);
		}
		return transform.createTransformedShape(rail.type.getFill());
	}

	/**
	 * @param checkPoint チェックポイント
	 * @return チェックポイントの塗りつぶし
	 */
	public static Shape getFill(final CheckPoint checkPoint) {
		final AffineTransform transform = new AffineTransform();
		transform.translate(checkPoint.location.getX(), checkPoint.location.getY());
		transform.rotate(checkPoint.angle);
		return transform.createTransformedShape(new Rectangle2D.Double(-Const.EDITOR_CHECK_POINT_WIDTH / 2,
				-Const.RAIL_WIDTH / 2, Const.EDITOR_CHECK_POINT_WIDTH, Const.RAIL_WIDTH));
	}

}
