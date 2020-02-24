package novel.server;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

import novel.tool.FileTool;
import novel.tool.Instruct;

/**
 * ������
 * 
 * @author 22442
 *
 */
public class Server {
	// �Ƿ����ָ��
	private boolean isListenInstruct = true;
	// ioͨ��
	private DataInputStream in;
	private DataOutputStream out;

	// С˵��ŵĸ�Ŀ¼
	private String root = "E:\\novel\\server";
	// С˵�ļ�
	private Set<String> novels;

	public static void main(String[] args) throws IOException {
		new Server();
	}

	public Server() throws IOException {
		/*
		 * ��������������
		 */
		ServerSocket serverSocket = new ServerSocket(8888);
		System.out.println("server listen...");
		Socket socket = serverSocket.accept();
		System.out.println("a client connect...");
		/*
		 * ʵ������Ҫ�Ĺ���
		 */
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		novels = FileTool.findFilesSet(root);
		/*
		 * ѭ������ָ��
		 */
		while (isListenInstruct) {
			// ����ָ��
			String s = in.readUTF();
			System.out.println("ָ��:" + s);
			// ��ȡС˵�б�
			if (s.contains(Instruct.NOVEL_LIST.name())) {
				StringBuilder sb = new StringBuilder();
				// ��С˵�б�ת��Ϊ�ַ�������,����Ϊ�ָ���
				for (String file : novels) {
					if(file.endsWith(".txt"))
						sb.append(file).append(",");
				}
				// ���
				out.writeUTF(sb.substring(0, sb.length() - 1));
				out.flush();
			}
			// ��ȡС˵����(ָ��,С˵��)
			else if (s.contains(Instruct.NOVEL_CONTENT.name())) {
				// С˵��
				String novel = s.split(",")[1];
				// ��ȡС˵����
				String str = FileTool.read(root + "\\" + novel);
				StringBuilder sb = new StringBuilder(str);
				// ����С˵����,ÿ�η���3000*3���ַ�(9000*2byte=17kB)
				int i = 1; // ��¼�������
				while (sb.length() > 0) {
					System.out.println("���䣺" + (i++));
					int len = sb.length() >= 9000 ? 9000 : sb.length();
					out.writeUTF(sb.substring(0, len));
					// �����͵����ݴ��ַ�����ɾ��
					sb.delete(0, len);
				}
				// ���ͽ�������
				out.writeUTF(Instruct.FILE_EOF.name());
				out.flush();
			}
			// �ϴ�ͼƬ
			else if(s.contains(Instruct.UPLOAD_IMAGE.name())) {
				// ����׼������ָ��
				out.writeUTF(Instruct.RETURN_UPLOAD_IMAGE.name());
				out.flush();
				/*
				 * ������Ϣ
				 */
				System.out.println("���ڽ���ͼƬ...");
				// ����ͼƬ
				String path = root + "\\" + "image" +"\\" + System.currentTimeMillis() + ".jpg";
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(FileTool.OpenFile(path)));
				int len = 0;
				byte[] bArr = new byte[1024];
				while((len = in.read(bArr)) > 4) {
					bos.write(bArr);
					System.out.println("����:" + len);
				}
				bos.flush();
				bos.close();
				System.out.println("�������");
				/*
				 * ���ز������
				 */
				Instruct ins = Instruct.SUCCESS;
				out.writeUTF(ins.name());
				out.flush();
			}
			// �ر�����,ָ��ΪCLOSE
			else {
				isListenInstruct = false;
			}
		}
		/*
		 * �ر�����
		 */
		in.close();
		out.close();
		socket.close();
		serverSocket.close();
		System.out.println("server close success...");
	}
}
