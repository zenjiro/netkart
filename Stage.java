import java.util.ArrayList;
import java.util.List;

/**
 * ステージをカプセル化するクラスです。
 * @author zenjiro
 */
public class Stage {

	/**
	 * レールの一覧
	 */
	private List<Rail> rails;

	/**
	 * チェックポイントの一覧
	 */
	private List<CheckPoint> checkPoints;
	
	/**
	 * タイトル
	 */
	private String title;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * コンストラクタです。
	 */
	public Stage() {
		this.rails = new ArrayList<Rail>();
		this.checkPoints = new ArrayList<CheckPoint>();
		this.title = "";
		this.author = "";
	}

	/**
	 * @return 作者
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * @param author 作者
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return レールの一覧
	 */
	public List<Rail> getRails() {
		return this.rails;
	}

	/**
	 * @param rails レールの一覧
	 */
	public void setRails(List<Rail> rails) {
		this.rails = rails;
	}

	/**
	 * @return タイトル
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title タイトル
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 指定されたステージをコピーします。
	 * @param stage ステージ
	 */
	public void setStage(final Stage stage) {
		this.rails = stage.rails;
		this.checkPoints = stage.checkPoints;
		this.title = stage.title;
		this.author = stage.author;
	}

	/**
	 * @return チェックポイントの一覧
	 */
	public List<CheckPoint> getCheckPoints() {
		return checkPoints;
	}

	/**
	 * @param checkPoints チェックポイントの一覧
	 */
	public void setCheckPoints(List<CheckPoint> checkPoints) {
		this.checkPoints = checkPoints;
	}
}
