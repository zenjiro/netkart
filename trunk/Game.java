/**
 * ゲームの状態を保持するクラスです。
 * @author zenjiro
 */
public class Game {

	/**
	 * ゲームの状態を表す定数の列挙型
	 */
	public enum Status {
		/**
		 * オープニング画面
		 */
		OPENING,
		/**
		 * ステージ選択画面
		 */
		SELECTING_STAGE,
		/**
		 * 車種選択画面
		 */
		SELECTING_CAR,
		/**
		 * スタート待ち画面
		 */
		WAITING_FOR_START,
		/**
		 * ゲーム中
		 */
		PLAYING,
		/**
		 * 一時停止中
		 */
		PAUSING,
		/**
		 * ゲームオーバー画面
		 */
		OVER,
	}

	/**
	 * ゲームの状態
	 */
	public Status status;

}
