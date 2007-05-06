/**
 * プレイヤの状態を保持するクラスです。
 * @author zenjiro
 */
public class Player {

	/**
	 * x座標
	 */
	public double x;

	/**
	 * y座標
	 */
	public double y;

	/**
	 * 向き[ラジアン]（0:X軸正の向き）
	 */
	public double direction;

	/**
	 * 速度
	 */
	public double speed;

	/**
	 * 横滑りの速度
	 */
	public double slideSpeed;

	/**
	 * 向きの変化量
	 */
	public double handle;

}
