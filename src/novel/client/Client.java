package novel.client;

import java.io.File;
import java.util.Set;

import javax.swing.JOptionPane;

import novel.tool.FileTool;
import novel.tool.Instruct;

/**
 * 客户端
 * 
 * @author 22442
 *
 */
public class Client {

	// 连接对象
	private ClientNet clientNet;
	// 主窗口
	public MainFrame mainFrame;
	// 绘图窗口
	public GraphFrame graphFrame;
	// 小说保存根目录
	public String root = "E:\\novel\\client";
	// 小说目录列表
	private Set<String> novels;

	public static void main(String[] args) {
		new Client();
	}

	public Client() {
		// 加载主窗体
		loadMainFrame();
		// 登录
		int index = 0;
		while(!login("login")) {
			if(index ++ >= 3) {
				JOptionPane.showMessageDialog(mainFrame, "你是(￣(oo)￣)吗？都"+index+"次了还输不对");
				JOptionPane.showMessageDialog(mainFrame, "劳资不伺候了╭(╯^╰)╮");
				closeNet();
				System.exit(0);
			}
		}
	}

	/*
	 * 登录验证，只能使用特定的名称
	 */
	private boolean login(String nickname) {
		// 输入对话框获取输入名称
		String loginname = JOptionPane.showInputDialog("请输入登录名").trim();
		boolean flag = loginname.equals(nickname); 
		if(!flag)
			JOptionPane.showMessageDialog(mainFrame, "用户名输入错误(应该输入"+nickname+")");
		return flag;
	}
	
	/**
	 * 关闭socket连接
	 */
	public void closeNet() {
		if (clientNet != null)
			clientNet.close();
	}

	/**
	 * 获取小说的内容
	 * 
	 * @param novel 小说名称
	 * @return
	 */
	public String getNovelContent(String novel) {
		/*
		 * 检查本地是否含有该小说
		 */
		novels = FileTool.findFilesSet(root); // 获取本地小说列表
		/*
		 * 如果本地缓存列表中没有该小说,就从服务器下载小说
		 */
		String str = "";
		if (!novels.contains(novel)) {
			// 发送指令
			if (!send(Instruct.NOVEL_CONTENT, novel)) {
				return "获取小说指令发送失败,请联系管理员";
			}
			StringBuilder sb = new StringBuilder();
			// 获取内容,防止文件过大需要多次读取
			str = recv();
			// 读取结束指令结束循环
			while (!str.equals(Instruct.FILE_EOF.name())) {
				sb.append(str);
				str = recv();
			}
			str = sb.toString();
			// 写入本地文件
			FileTool.write(str, root + "\\" + novel);
		} else {
			str = FileTool.read(root + "\\" + novel);
		}
		return str;
	}

	/*
	 * 加载主窗体
	 */
	private void loadMainFrame() {
		mainFrame = new MainFrame(this);
		// 显示窗体
		mainFrame.show();
		// 获取小说列表
		String[] list = getNovelList();
		// 设置小说列表框
		mainFrame.setNovelList(list);
	}
	
	/**
	 * 上传图片
	 * @param path
	 * @return
	 */
	public boolean uploadImage(String path) {
		// 发送指令,上传图片
		if(send(Instruct.UPLOAD_IMAGE, FileTool.OpenFile(path))) {
			return true;
		}
		return false;
	}

	/**
	 * 加载绘图窗口
	 */
	public void loadGraphFrame() {
		graphFrame = new GraphFrame(this);
		// 显示窗体
		graphFrame.show();
	}
	
	/*
	 * 获取服务器小说列表
	 */
	private String[] getNovelList() {
		// 发送指令
		if (!send(Instruct.NOVEL_LIST, "")) { // 指令发送失败
			return "小说列表获取失败".split(",");
		}
		// 接收消息
		String msg = recv();
		return msg.split(",");
	}

	/*
	 * 连接服务器
	 */
	private ClientNet getConnect() {
		if (clientNet == null)
			clientNet = new ClientNet("127.0.0.1", 8888);
		return clientNet;
	}

	/*
	 * 发送指令
	 */
	private boolean send(Instruct ins, String padding) {
		// 连接服务器
		getConnect();
		// 发送指令
		return clientNet.send(ins, padding);
	}
	
	private boolean send(Instruct ins, File file) {
		// 连接服务器
		getConnect();
		// 发送指令
		return clientNet.send(ins, file);
	}

	/*
	 * 接收消息
	 */
	private String recv() {
		// 连接服务器
		getConnect();
		// 接收消息
		return clientNet.recv();
	}
}
