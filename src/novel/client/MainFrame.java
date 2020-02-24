package novel.client;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * ������,���ڹ���С˵��ʾ������Ͳ������пؼ���ʾ
 * 
 * @author 22442
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public JFrame frame; // ���������

	// С˵Ŀ¼�����б�
	public JComboBox<String> comboBox;
	// ��ȡС˵���ݰ�ť
	public JButton button;
	// ��ʾС˵��������
	public JTextArea textarea;
	// ������
	public JScrollPane scroll;
	// ��һҳ,��һҳ
	public JButton prev, next;
	// ��ʾ��ͼ����
	public JButton showGraphFrame;
	// С˵ҳ��
	public int page = 0;

	// С˵����
	public List<String> contentList;
	// С˵����
	private String novel;
	// �ͻ���ʵ��,���ڵ���ʵ���е�������Դ,�����ӵ�socket
	private Client client;

	public MainFrame(Client client) {
		this.client = client;
	}

	/**
	 * ��ʾ����
	 */
	public void show() {
		if (frame == null) {
			initFrame();
		}
		frame.setVisible(true);
	}

	/**
	 * ���ش���
	 */
	public void hidden() {
		if (frame != null)
			frame.setVisible(false);
	}

	/**
	 * ����С˵�б�
	 * 
	 * @param list
	 */
	public void setNovelList(String[] list) {
		for (String s : list) {
			if(s.endsWith(".txt"))
				comboBox.addItem(s);
		}
	}

	/*
	 * ��ʼ��������
	 */
	private void initFrame() {
		frame = new JFrame("С˵����");
		/*
		 * ���ô���
		 */
		frame.setBounds(100, 100, 360, 600);
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // ����FrameĬ�Ϲر��¼�
		frame.setLayout(new BorderLayout());
		/*
		 * ��ӿؼ�
		 */
		JPanel panel = new JPanel(new BorderLayout());
		frame.add(panel, BorderLayout.NORTH);
		// ���list
		comboBox = new JComboBox<String>();
		panel.add(comboBox, BorderLayout.CENTER);
		// ���buttom
		button = new JButton("����");
		panel.add(button, BorderLayout.EAST);
		// ���textarea
		textarea = new JTextArea();
		textarea.setLineWrap(true); // ����
		// ������
		scroll = new JScrollPane(textarea);
		// ��ֱ������
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// ˮƽ������
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		frame.add(scroll, BorderLayout.CENTER);
		// �ײ�panel
		JPanel panel_b = new JPanel(new BorderLayout());
		frame.add(panel_b, BorderLayout.SOUTH);
		// ��һҳ,��һҳ
		prev = new JButton("��һҳ");
		next = new JButton("��һҳ");
		panel_b.add(prev, BorderLayout.WEST);
		panel_b.add(next, BorderLayout.EAST);
		// ��ʾ��ͼ����
		showGraphFrame = new JButton("ͼ�η���");
		panel_b.add(showGraphFrame, BorderLayout.CENTER);
		/*
		 * ��Ӽ���
		 */
		addListener();
	}
	
	/**
	 * ��Ӽ�������
	 */
	public void addListener() {
		// ��Ӵ��ڹرռ���
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				// �رպͷ�������socket����
				client.closeNet();
				// �رմ���, �˳�����
				System.exit(0);
			}
		});
		// ��ӵ���С˵��ť�ļ����¼�
		button.addActionListener(l -> {
			page = 0;
			String s = getNovelContent(page);
			textarea.setText(s);
		});
		// ��һҳ����һҳ��ҳ
		prev.addActionListener(l -> {
			// ���listΪ��ֱ�ӷ��ز����κδ���
			if (contentList == null || contentList.size() == 0)
				return;
			page = --page >= 0 ? page : 0;
			textarea.setText(contentList.get(page));
		});
		next.addActionListener(l -> {
			// ���listΪ��ֱ�ӷ��ز����κδ���
			if (contentList == null || contentList.size() == 0)
				return;
			page = ++page < contentList.size() ? page : contentList.size() - 1;
			textarea.setText(contentList.get(page));
		});
		// ��ʾ��ͼ����
		showGraphFrame.addActionListener(l -> {
			// ���listΪ��ֱ�ӷ��ز����κδ���
			if (contentList == null || contentList.size() == 0)
				return;
			// ���ػ�ͼ����
			client.loadGraphFrame();
			// ��ǰ���岻��ʾ
			hidden();
		});
	}

	/*
	 * ��ȡС˵����
	 */
	private String getNovelContent(int index) {
		// ��ȡС˵��������ѡ�е�С˵
		String select = (String) comboBox.getSelectedItem();
		// ��ȡѡ�е�С˵������
		if (!select.equals(novel)) {
			novel = select;
			contentList = new ArrayList<>();
			String content = client.getNovelContent(select);
			StringBuilder sb = new StringBuilder(content);
			// �ָ�С˵����
			while (sb.length() > 0) {
				// ÿ����ʾ3000���ַ�
				int len = sb.length() >= 3000 ? 3000 : sb.length();
				contentList.add(sb.substring(0, len));
				sb.delete(0, len);
			}
		}
		// �ж�ҳ���������
		if (index >= contentList.size()) {
			index = contentList.size() - 1;
			page = index;
		}
		// ����ָ��ҳ��
		return contentList.get(index);
	}
}
