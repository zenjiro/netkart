import java.awt.Color;
import java.awt.Font;

/**
 * 定数を集めたクラスです。
 * @author zenjiro
 */
public class Const {

	/**
	 * 道の幅[px]
	 */
	public static final int RAIL_WIDTH = 80;

	/**
	 * グリッドの1辺[px]
	 */
	public static final int EDITOR_GRID = 100;

	/**
	 * 追加するときの塗りつぶし色
	 */
	public static final Color COLOR_ADD_FILL = new Color(230, 230, 240);

	/**
	 * 追加するときの描画色
	 */
	public static final Color COLOR_ADD_DRAW = Color.BLUE;
	
	/**
	 * 削除するときの塗りつぶし色
	 */
	public static final Color COLOR_DELETE_FILL = new Color(240, 230, 230);
	
	/**
	 * 削除するときの描画色
	 */
	public static final Color COLOR_DELETE_DRAW = Color.RED;

	/**
	 * エディタのフォント
	 */
	public static final Font EDITOR_FONT = new Font("Sans", Font.BOLD, 50);
	
}
