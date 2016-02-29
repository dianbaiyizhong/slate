package hhm.slate.singleton;

public class PathUtil {
	private PathUtil() {
	}

	private static PathUtil pathUtil = null;

	// 静态工厂方法
	public static PathUtil getInstance() {
		if (pathUtil == null) {
			pathUtil = new PathUtil();
		}
		return pathUtil;
	}

}
