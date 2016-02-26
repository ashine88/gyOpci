package copyFilePack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.naming.directory.DirContext;

import org.apache.commons.io.FileUtils;

public class FileOperateUtils {

	// public static svnOp sOp = new svnOp();

	/**
	 * @param args
	 */
	/****
	 * 根据现在公司的内网环境，每个项目由Jenkins构建后生成一个对应名称的目录
	 * 在某个目录下复制部分文件到另一个指定目录中，复制前把目标目录及其子目录删除掉
	 * 
	 * @param startDirs
	 *            #起始目录数组
	 * @param desDirs
	 *            #目标目录数组
	 * @param noCopyFolder
	 *            #在起始目录中不需要复制的文件夹
	 * @return 返回TRUE复制成功，否则失败
	 */

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}

			}
		}
		return dir.delete();

	}

	public static boolean copyDir(String startDirs, String desDirs,
			String noCopyFolder) {
		try {
			File file = new File(desDirs);
			if (!file.exists()) {
				if (!file.mkdir()) {
					System.out.print("目标文件不存在，创建失败");
				}
			}
			File startFile = new File(startDirs);
			File[] allFiles = startFile.listFiles();
			int totalNum = allFiles.length;
			String srcName = "";
			String desName = "";

			for (int i = 0; i < totalNum; i++) {
				if (!allFiles[i].isDirectory()) {
					srcName = allFiles[i].toString();

					desName = desDirs + "\\" + allFiles[i].getName();
					copyfile(srcName, desName);

				} else {
					// System.out.println(allFiles[i].getName().toString());
					if (allFiles[i].getName().toString().equals(noCopyFolder)) {
						continue;
					}
					if (copyDir(allFiles[i].getPath().toString(), desDirs
							+ "\\" + allFiles[i].getName().toString(),
							noCopyFolder)) {
						System.out.println(" Copy Successfully!");
					} else {
						System.out.println("SubDirectory Copy Error!");
					}

				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/***
	 * 复制文件内容到另一个文件里
	 * 
	 * @param startPath
	 *            #源文件路径
	 * 
	 * @param desPath
	 *            #目标文件路径
	 * 
	 * @return #复制成功返回TRUE否则false
	 */
	public static boolean copyfile(String startPath, String desPath) {
		FileInputStream fis;
		FileOutputStream fos;
		int readNum = 0;
		try {

			File srcFile = new File(startPath);
			File destFile = new File(desPath);
			FileUtils.copyFile(srcFile, destFile);
			// fis = new FileInputStream(srcFile);
			// fos = new FileOutputStream(targetFile);
			// byte[] bt = new byte[1024];
			// while ((readNum = fis.read(bt)) != -1) {
			// fos.write(bt, 0, bt.length);
			// }
			//
			// fis.close();
			// fos.close();
			// //设置最后更新时间
			// targetFile.setLastModified(srcFile.lastModified());
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {

		/*
		 * 复制前把目标源文件和lib目录删除掉
		 */

		// 目录比较长，应该放在一个专门的文件中,以后做改进
		// String startClassfileDir =
		// "E:\\update-maven\\workspace\\zhangmen-shequn\\zm-shequn-service\\target\\zm-shequn-service-0.0.1\\WEB-INF\\classes\\cn";
		String startClassfileDir = "E:\\update-maven\\workspace\\zhangmen-shequn\\zm-shequn-service\\target\\zm-shequn-service-0.0.1\\WEB-INF\\classes\\cn";
		String startLibDir = "E:\\update-maven\\workspace\\zhangmen-shequn\\zm-shequn-service\\target\\zm-shequn-service-0.0.1\\WEB-INF\\lib";

		// 存放开始复制目录的数组
		String[] startDirs = { startClassfileDir, startLibDir };

		String desClassfileDir = "E:\\update-work\\zhangmen\\zhangmen-shequn\\Alpha\\trunk\\WEB-INF\\classes\\cn";
		String descLibDir = "E:\\update-work\\zhangmen\\zhangmen-shequn\\Alpha\\trunk\\WEB-INF\\lib";

		// 存放目标复制目录的数组
		String[] descDirs = { desClassfileDir, descLibDir };

		// 调用删除目录的方法

		// boolean success = deleteDir(new File("D:/temp1"));
		// if(success) {
		// System.out.println("删除成功！");
		// }else{
		// System.out.println("删除失败！");
		// }

		
		 //改进代码一
		boolean successClass = deleteDir(new File(desClassfileDir));
		boolean successLib = deleteDir(new File(descLibDir));
		if (successClass && successLib) {
			System.out.println("删除成功！");
		} else {
			System.out.println("删除失败！");
		}

		// main方法里面有项目要复制的目录 把想要复制的目录放到一个数组里面，用for循环遍历，然后执行复制语句
		for (int i = 0; i < startDirs.length; i++) {
			if (copyDir(startDirs[i], descDirs[i], "")) {
				System.out.print("复制成功");
			} else {
				System.out.print("复制失败");
			}

		}

	}
}
