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
 * 服务器
 * 
 * @author 22442
 *
 */
public class Server {
	// 是否接收指令
	private boolean isListenInstruct = true;
	// io通道
	private DataInputStream in;
	private DataOutputStream out;

	// 小说存放的根目录
	private String root = "E:\\novel\\server";
	// 小说文件
	private Set<String> novels;

	public static void main(String[] args) throws IOException {
		new Server();
	}

	public Server() throws IOException {
		/*
		 * 创建并监听连接
		 */
		ServerSocket serverSocket = new ServerSocket(8888);
		System.out.println("server listen...");
		Socket socket = serverSocket.accept();
		System.out.println("a client connect...");
		/*
		 * 实例化需要的工具
		 */
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		novels = FileTool.findFilesSet(root);
		/*
		 * 循环接收指令
		 */
		while (isListenInstruct) {
			// 接收指令
			String s = in.readUTF();
			System.out.println("指令:" + s);
			// 获取小说列表
			if (s.contains(Instruct.NOVEL_LIST.name())) {
				StringBuilder sb = new StringBuilder();
				// 将小说列表转换为字符串传输,逗号为分隔符
				for (String file : novels) {
					if(file.endsWith(".txt"))
						sb.append(file).append(",");
				}
				// 输出
				out.writeUTF(sb.substring(0, sb.length() - 1));
				out.flush();
			}
			// 获取小说内容(指令,小说名)
			else if (s.contains(Instruct.NOVEL_CONTENT.name())) {
				// 小说名
				String novel = s.split(",")[1];
				// 读取小说内容
				String str = FileTool.read(root + "\\" + novel);
				StringBuilder sb = new StringBuilder(str);
				// 发送小说内容,每次发送3000*3个字符(9000*2byte=17kB)
				int i = 1; // 记录传输次数
				while (sb.length() > 0) {
					System.out.println("传输：" + (i++));
					int len = sb.length() >= 9000 ? 9000 : sb.length();
					out.writeUTF(sb.substring(0, len));
					// 将发送的数据从字符串中删除
					sb.delete(0, len);
				}
				// 发送结束命令
				out.writeUTF(Instruct.FILE_EOF.name());
				out.flush();
			}
			// 上传图片
			else if(s.contains(Instruct.UPLOAD_IMAGE.name())) {
				// 返回准备接收指令
				out.writeUTF(Instruct.RETURN_UPLOAD_IMAGE.name());
				out.flush();
				/*
				 * 接收信息
				 */
				System.out.println("正在接收图片...");
				// 接收图片
				String path = root + "\\" + "image" +"\\" + System.currentTimeMillis() + ".jpg";
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(FileTool.OpenFile(path)));
				int len = 0;
				byte[] bArr = new byte[1024];
				while((len = in.read(bArr)) > 4) {
					bos.write(bArr);
					System.out.println("接收:" + len);
				}
				bos.flush();
				bos.close();
				System.out.println("操作完成");
				/*
				 * 返回操作结果
				 */
				Instruct ins = Instruct.SUCCESS;
				out.writeUTF(ins.name());
				out.flush();
			}
			// 关闭连接,指令为CLOSE
			else {
				isListenInstruct = false;
			}
		}
		/*
		 * 关闭连接
		 */
		in.close();
		out.close();
		socket.close();
		serverSocket.close();
		System.out.println("server close success...");
	}
}
