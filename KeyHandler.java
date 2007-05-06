import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;


/**
 * キー入力を扱うクラスです。
 * @author zenjiro
 */
public class KeyHandler implements KeyListener {

	/**
	 * 押されているキーの一覧
	 */
	private final Set<Integer> keys;
	
	/**
	 * コンストラクタです。
	 */
	public KeyHandler() {
		this.keys = new HashSet<Integer>();
	}
	
	public void keyPressed(final KeyEvent e) {
		this.keys.add(e.getKeyCode());
	}

	public void keyReleased(final KeyEvent e) {
		this.keys.remove(e.getKeyCode());
	}

	public void keyTyped(final KeyEvent e) {
	}
	
	/**
	 * @param keyCode キーコード
	 * @return 指定したキーが押されているかどうか
	 */
	public boolean isPressed(final int keyCode) {
		return this.keys.contains(keyCode);
	}

}
