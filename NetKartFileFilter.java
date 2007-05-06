import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * NetKartステージ形式を受け入れるファイルフィルタの実装です。
 * @author zenjiro
 */
public class NetKartFileFilter extends FileFilter {
	@Override
	public boolean accept(final File file) {
		return file.getName().toLowerCase().endsWith(".netkart");
	}

	@Override
	public String getDescription() {
		return "NetKartステージ形式（*.netkart）";
	}
}
