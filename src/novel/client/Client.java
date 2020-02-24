package novel.client;

import java.io.File;
import java.util.Set;

import javax.swing.JOptionPane;

import novel.tool.FileTool;
import novel.tool.Instruct;

/**
 * �ͻ���
 * 
 * @author 22442
 *
 */
public class Client {

	// ���Ӷ���
	private ClientNet clientNet;
	// ������
	public MainFrame mainFrame;
	// ��ͼ����
	public GraphFrame graphFrame;
	// С˵�����Ŀ¼
	public String root = "E:\\novel\\client";
	// С˵Ŀ¼�б�
	private Set<String> novels;

	public static void main(String[] args) {
		new Client();
	}

	public Client() {
		// ����������
		loadMainFrame();
		// ��¼
		int index = 0;
		while(!login("login")) {
			if(index ++ >= 3) {
				JOptionPane.showMessageDialog(mainFrame, "����(��(oo)��)�𣿶�"+index+"���˻��䲻��");
				JOptionPane.showMessageDialog(mainFrame, "���ʲ��ź��˨q(�s^�t)�r");
				closeNet();
				System.exit(0);
			}
		}
	}

	/*
	 * ��¼��֤��ֻ��ʹ���ض�������
	 */
	private boolean login(String nickname) {
		// ����Ի����ȡ��������
		String loginname = JOptionPane.showInputDialog("�������¼��").trim();
		boolean flag = loginname.equals(nickname); 
		if(!flag)
			JOptionPane.showMessageDialog(mainFrame, "�û����������(Ӧ������"+nickname+")");
		return flag;
	}
	
	/**
	 * �ر�socket����
	 */
	public void closeNet() {
		if (clientNet != null)
			clientNet.close();
	}

	/**
	 * ��ȡС˵������
	 * 
	 * @param novel С˵����
	 * @return
	 */
	public String getNovelContent(String novel) {
		/*
		 * ��鱾���Ƿ��и�С˵
		 */
		novels = FileTool.findFilesSet(root); // ��ȡ����С˵�б�
		/*
		 * ������ػ����б���û�и�С˵,�ʹӷ���������С˵
		 */
		String str = "";
		if (!novels.contains(novel)) {
			// ����ָ��
			if (!send(Instruct.NOVEL_CONTENT, novel)) {
				return "��ȡС˵ָ���ʧ��,����ϵ����Ա";
			}
			StringBuilder sb = new StringBuilder();
			// ��ȡ����,��ֹ�ļ�������Ҫ��ζ�ȡ
			str = recv();
			// ��ȡ����ָ�����ѭ��
			while (!str.equals(Instruct.FILE_EOF.name())) {
				sb.append(str);
				str = recv();
			}
			str = sb.toString();
			// д�뱾���ļ�
			FileTool.write(str, root + "\\" + novel);
		} else {
			str = FileTool.read(root + "\\" + novel);
		}
		return str;
	}

	/*
	 * ����������
	 */
	private void loadMainFrame() {
		mainFrame = new MainFrame(this);
		// ��ʾ����
		mainFrame.show();
		// ��ȡС˵�б�
		String[] list = getNovelList();
		// ����С˵�б��
		mainFrame.setNovelList(list);
	}
	
	/**
	 * �ϴ�ͼƬ
	 * @param path
	 * @return
	 */
	public boolean uploadImage(String path) {
		// ����ָ��,�ϴ�ͼƬ
		if(send(Instruct.UPLOAD_IMAGE, FileTool.OpenFile(path))) {
			return true;
		}
		return false;
	}

	/**
	 * ���ػ�ͼ����
	 */
	public void loadGraphFrame() {
		graphFrame = new GraphFrame(this);
		// ��ʾ����
		graphFrame.show();
	}
	
	/*
	 * ��ȡ������С˵�б�
	 */
	private String[] getNovelList() {
		// ����ָ��
		if (!send(Instruct.NOVEL_LIST, "")) { // ָ���ʧ��
			return "С˵�б��ȡʧ��".split(",");
		}
		// ������Ϣ
		String msg = recv();
		return msg.split(",");
	}

	/*
	 * ���ӷ�����
	 */
	private ClientNet getConnect() {
		if (clientNet == null)
			clientNet = new ClientNet("127.0.0.1", 8888);
		return clientNet;
	}

	/*
	 * ����ָ��
	 */
	private boolean send(Instruct ins, String padding) {
		// ���ӷ�����
		getConnect();
		// ����ָ��
		return clientNet.send(ins, padding);
	}
	
	private boolean send(Instruct ins, File file) {
		// ���ӷ�����
		getConnect();
		// ����ָ��
		return clientNet.send(ins, file);
	}

	/*
	 * ������Ϣ
	 */
	private String recv() {
		// ���ӷ�����
		getConnect();
		// ������Ϣ
		return clientNet.recv();
	}
}
