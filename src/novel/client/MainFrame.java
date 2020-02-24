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
 * 主窗体,用于构建小说显示主窗体和操作其中控件显示
 * 
 * @author 22442
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public JFrame frame; // 主窗体对象

	// 小说目录下拉列表
	public JComboBox<String> comboBox;
	// 获取小说内容按钮
	public JButton button;
	// 显示小说内容容器
	public JTextArea textarea;
	// 滚动条
	public JScrollPane scroll;
	// 上一页,下一页
	public JButton prev, next;
	// 显示绘图窗口
	public JButton showGraphFrame;
	// 小说页码
	public int page = 0;

	// 小说内容
	public List<String> contentList;
	// 小说标题
	private String novel;
	// 客户端实体,用于调用实体中的其他资源,如连接的socket
	private Client client;

	public MainFrame(Client client) {
		this.client = client;
	}

	/**
	 * 显示窗体
	 */
	public void show() {
		if (frame == null) {
			initFrame();
		}
		frame.setVisible(true);
	}

	/**
	 * 隐藏窗体
	 */
	public void hidden() {
		if (frame != null)
			frame.setVisible(false);
	}

	/**
	 * 设置小说列表
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
	 * 初始化主窗体
	 */
	private void initFrame() {
		frame = new JFrame("小说窗体");
		/*
		 * 设置窗体
		 */
		frame.setBounds(100, 100, 360, 600);
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 设置Frame默认关闭事件
		frame.setLayout(new BorderLayout());
		/*
		 * 添加控件
		 */
		JPanel panel = new JPanel(new BorderLayout());
		frame.add(panel, BorderLayout.NORTH);
		// 添加list
		comboBox = new JComboBox<String>();
		panel.add(comboBox, BorderLayout.CENTER);
		// 添加buttom
		button = new JButton("导入");
		panel.add(button, BorderLayout.EAST);
		// 添加textarea
		textarea = new JTextArea();
		textarea.setLineWrap(true); // 换行
		// 滚动条
		scroll = new JScrollPane(textarea);
		// 垂直滚动条
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// 水平滚动条
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		frame.add(scroll, BorderLayout.CENTER);
		// 底部panel
		JPanel panel_b = new JPanel(new BorderLayout());
		frame.add(panel_b, BorderLayout.SOUTH);
		// 上一页,下一页
		prev = new JButton("上一页");
		next = new JButton("下一页");
		panel_b.add(prev, BorderLayout.WEST);
		panel_b.add(next, BorderLayout.EAST);
		// 显示绘图窗口
		showGraphFrame = new JButton("图形分析");
		panel_b.add(showGraphFrame, BorderLayout.CENTER);
		/*
		 * 添加监听
		 */
		addListener();
	}
	
	/**
	 * 添加监听函数
	 */
	public void addListener() {
		// 添加窗口关闭监听
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				// 关闭和服务器的socket连接
				client.closeNet();
				// 关闭窗口, 退出程序
				System.exit(0);
			}
		});
		// 添加导入小说按钮的监听事件
		button.addActionListener(l -> {
			page = 0;
			String s = getNovelContent(page);
			textarea.setText(s);
		});
		// 上一页和下一页翻页
		prev.addActionListener(l -> {
			// 如果list为空直接返回不做任何处理
			if (contentList == null || contentList.size() == 0)
				return;
			page = --page >= 0 ? page : 0;
			textarea.setText(contentList.get(page));
		});
		next.addActionListener(l -> {
			// 如果list为空直接返回不做任何处理
			if (contentList == null || contentList.size() == 0)
				return;
			page = ++page < contentList.size() ? page : contentList.size() - 1;
			textarea.setText(contentList.get(page));
		});
		// 显示绘图窗口
		showGraphFrame.addActionListener(l -> {
			// 如果list为空直接返回不做任何处理
			if (contentList == null || contentList.size() == 0)
				return;
			// 加载绘图窗口
			client.loadGraphFrame();
			// 当前窗体不显示
			hidden();
		});
	}

	/*
	 * 获取小说内容
	 */
	private String getNovelContent(int index) {
		// 获取小说下拉框中选中的小说
		String select = (String) comboBox.getSelectedItem();
		// 获取选中的小说的内容
		if (!select.equals(novel)) {
			novel = select;
			contentList = new ArrayList<>();
			String content = client.getNovelContent(select);
			StringBuilder sb = new StringBuilder(content);
			// 分割小说内容
			while (sb.length() > 0) {
				// 每次显示3000个字符
				int len = sb.length() >= 3000 ? 3000 : sb.length();
				contentList.add(sb.substring(0, len));
				sb.delete(0, len);
			}
		}
		// 判断页码的上下限
		if (index >= contentList.size()) {
			index = contentList.size() - 1;
			page = index;
		}
		// 返回指定页码
		return contentList.get(index);
	}
}
