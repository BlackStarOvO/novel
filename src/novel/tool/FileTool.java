package novel.tool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件操作辅助工具类,简化文件的操作
 * 
 * @author 22442
 *
 */
public class FileTool {
	/**
	 * 打开或者创建目标文件
	 * 
	 * @param path
	 * @return
	 */
	public static File OpenFile(String path) {
		File file = new File(path);
		// 判断上级文件目录是否存在
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		// 如果文件不存在,创建新的文件
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return file;
	}

	/**
	 * 读取目标文件
	 * 
	 * @param path
	 * @return
	 */
	public static String read(String path) {
		// 打开文件
		File file = OpenFile(path);
		// 读取内容
		String s;
		try {
			// 通过缓存流读取
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			while ((s = reader.readLine()) != null) {
				sb.append(s).append("\r\n"); // 读取一行需要添加换行符
			}
			reader.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "文件读取失败";
		}
	}

	/**
	 * 将字符串写入到指定文件中
	 * 
	 * @param content
	 * @param path
	 */
	public static void write(String content, String path) {
		// 打开文件
		File file = OpenFile(path);
		// 写入文件
		byte[] b = content.getBytes();
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
			dos.write(b, 0, b.length);
			dos.flush();
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 找寻目标目录下的文件
	 * 
	 * @param path
	 * @return
	 */
	public static File[] findFiles(String path) {
		// 获取源文件
		File file = OpenFile(path);
		// 返回所有文件
		return file.listFiles();
	}

	/**
	 * 找寻目标文件下的文件
	 * 
	 * @param path
	 * @return
	 */
	public static Set<String> findFilesSet(String path) {
		Set<String> set = new HashSet<String>();
		File[] files = findFiles(path);
		if (files == null)
			return set;
		// 查询所有文件
		for (File file : files) {
			set.add(file.getName());
		}
		return set;
	}
}
