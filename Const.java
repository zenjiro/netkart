import java.awt.Color;
import java.awt.Font;

/**
 * 定数を集めたクラスです。
 * @author zenjiro
 */
public class Const {
	
	/**
	 * エディタに関する定数を集めたクラスです。
	 * @author zenjiro
	 */
	public static class Editor {

		/**
		 * グリッドの1辺[px]
		 */
		public static final int EDITOR_GRID = 100;
		/**
		 * エディタのフォント
		 */
		public static final Font EDITOR_FONT = new Font("Sans", Font.BOLD, 50);
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
		
	}

	/**
	 * 通信に関する定数を集めたクラスです。
	 * @author zenjiro
	 */
	public static class Network {
		
		/**
		 * サーバのホスト名
		 */
		public static final String SERVER = "zenjiro.dyndns.org";
		
		/**
		 * サーバが接続を待ち受けるポート番号
		 */
		public static final int PORT = 60143;
		
		/**
		 * クライアントがサーバにプレイヤの識別子を送る文字列
		 * 続いて「playerName」
		 */
		public static final String PUT_PLAYER_NAME = "PUT_PLAYER_NAME";

		/**
		 * クライアントがサーバにプレイヤの識別子を要求する文字列
		 * 戻り値は「playerName」
		 */
		public static final String GET_PLAYER_NAME = "GET_PLAYER_NAME";
		
		/**
		 * クライアントがサーバにステージの一覧を要求する文字列
		 * 戻り値は「stageID1    stageID2    ...」
		 */
		public static final String GET_STAGE_LIST = "GET_STAGE_LIST";

		/**
		 * クライアントがサーバにステージの識別子を要求する文字列
		 * 戻り値は「stageID」
		 */
		public static final String GET_STAGE_ID = "GET_STAGE_ID";
		
		/**
		 * クライアントがサーバにステージの識別子を送る文字列
		 * 続いて「stageID」
		 */
		public static final String PUT_STAGE_ID = "PUT_STAGE_ID";
		
		/**
		 * クライアントがサーバにプレイヤの位置を送る文字列
		 * 続いて「x座標」「y座標」「向き」
		 */
		public static final String PUT_LOCATION = "PUT_LOCATION";
		
		/**
		 * クライアントがサーバにプレイヤの一覧を要求する文字列
		 * 戻り値は「playerName1    playerName2    ...」
		 */
		public static final String GET_PLAYER_LIST = "GET_PLAYER_LIST";
		
		/**
		 * クライアントがサーバにプレイヤの位置を要求する文字列
		 * 続いて「playerName」
		 * 戻り値は「x座標」「y座標」「向き」
		 */
		public static final String GET_LOCATION = "GET_LOCATION";
	}
	
	/**
	 * 道の幅[px]
	 */
	public static final int RAIL_WIDTH = 80;

	/**
	 * チェックポイントの幅
	 */
	public static final int CHECK_POINT_WIDTH = 20;
	
}
