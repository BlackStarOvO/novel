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
 * �ļ���������������,���ļ��Ĳ���
 * 
 * @author 22442
 *
 */
public class FileTool {
	/**
	 * �򿪻��ߴ���Ŀ���ļ�
	 * 
	 * @param path
	 * @return
	 */
	public static File OpenFile(String path) {
		File file = new File(path);
		// �ж��ϼ��ļ�Ŀ¼�Ƿ����
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		// ����ļ�������,�����µ��ļ�
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
	 * ��ȡĿ���ļ�
	 * 
	 * @param path
	 * @return
	 */
	public static String read(String path) {
		// ���ļ�
		File file = OpenFile(path);
		// ��ȡ����
		String s;
		try {
			// ͨ����������ȡ
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			while ((s = reader.readLine()) != null) {
				sb.append(s).append("\r\n"); // ��ȡһ����Ҫ��ӻ��з�
			}
			reader.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "�ļ���ȡʧ��";
		}
	}

	/**
	 * ���ַ���д�뵽ָ���ļ���
	 * 
	 * @param content
	 * @param path
	 */
	public static void write(String content, String path) {
		// ���ļ�
		File file = OpenFile(path);
		// д���ļ�
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
	 * ��ѰĿ��Ŀ¼�µ��ļ�
	 * 
	 * @param path
	 * @return
	 */
	public static File[] findFiles(String path) {
		// ��ȡԴ�ļ�
		File file = OpenFile(path);
		// ���������ļ�
		return file.listFiles();
	}

	/**
	 * ��ѰĿ���ļ��µ��ļ�
	 * 
	 * @param path
	 * @return
	 */
	public static Set<String> findFilesSet(String path) {
		Set<String> set = new HashSet<String>();
		File[] files = findFiles(path);
		if (files == null)
			return set;
		// ��ѯ�����ļ�
		for (File file : files) {
			set.add(file.getName());
		}
		return set;
	}
}
