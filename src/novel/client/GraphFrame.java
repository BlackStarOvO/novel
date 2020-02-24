package novel.client;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import novel.tool.FileTool;
import novel.tool.TextTool;

/**
 * 图形窗体,用于绘制图形
 * 
 * @author 22442
 *
 */
@SuppressWarnings("serial")
public class GraphFrame extends JFrame {

	// 图形窗体
	private JFrame frame;
	// 客户端引用
	private Client client;
	// 选项卡
	private JTabbedPane tabPanel;
	// 姓名输入文本框
	private JTextField textfield;
	// 添加,删除,上传,活跃,关系按钮
	private JButton insert, remove, upload, density, relation;
	// 绘图面板
	private First first;
	private Second second;
	private Third third;
	// 绘制列表
	private Map<String, Integer> persons;
	// 密度列表
	private List<Boolean> densityList;
	// 密度统计人物
	private String densityPerson = "";
	// 关系中心人物
	private String centerPerson = "";
	// 人物之间的距离
	private Map<String, Integer> distances;

	public GraphFrame(Client client) {
		this.client = client;
	}

	/*
	 * 显示窗体
	 */
	public void show() {
		// 初始化窗体
		if (frame == null)
			initFrame();
		// 显示窗体
		frame.setVisible(true);
	}

	/*
	 * 根据persons绘制内容(就是绘制人物存在感强度)
	 */
	private void drawByPersons() {
		first.setPersons(persons);
		first.repaint();
	}

	/*
	 * 根据density绘制内容(就是绘制出现密度)
	 */
	private void drawByDensity() {
		second.setDensityList(densityList);
		second.repaint();
	}

	/*
	 * 根据distances绘制内容(就是绘制关系密度)
	 */
	private void drawByDistance() {
		third.setCenter(centerPerson);
		third.setDistances(distances);
		third.repaint();
	}

	/*
	 * 初始化绘制窗体
	 */
	private void initFrame() {
		frame = new JFrame("绘制窗体");
		/*
		 * 设置窗体
		 */
		frame.setBounds(100, 100, 600, 600);
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 设置Frame默认关闭事件
		frame.setLayout(new BorderLayout());
		/*
		 * 添加控件
		 */
		// 选项卡面板,设置选项卡放置在上面
		tabPanel = new JTabbedPane(JTabbedPane.TOP);
		frame.add(tabPanel, BorderLayout.CENTER);
		// 添加tab
		first = new First(new BorderLayout());
		tabPanel.addTab("存在强度", first);
		tabPanel.setTabLayoutPolicy(0);
		second = new Second(new BorderLayout());
		tabPanel.addTab("活跃状态", second);
		third = new Third(new BorderLayout());
		tabPanel.addTab("关系密度", third);
		// 选择第一个panel
		tabPanel.setSelectedIndex(0);
		// 顶部panel
		JPanel topPanel = new JPanel(new BorderLayout());
		frame.add(topPanel, BorderLayout.NORTH);
		// 输入文本框
		textfield = new JTextField();
		topPanel.add(textfield, BorderLayout.CENTER);
		// 再一个Jpanel
		JPanel panel = new JPanel(new GridLayout(1, 4));
		topPanel.add(panel, BorderLayout.EAST);
		// 添加,移除,上传,密度按钮
		insert = new JButton("添加");
		panel.add(insert);
		remove = new JButton("移除");
		panel.add(remove);
		density = new JButton("活跃");
		panel.add(density);
		relation = new JButton("关系");
		panel.add(relation);
		upload = new JButton("上传");
		panel.add(upload);
		/*
		 * 添加监听事件
		 */
		addListener();
	}

	/*
	 * 监听事件
	 */
	private void addListener() {
		// 关闭窗体
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				// 关闭绘制窗口
				frame.dispose();
				// 显示主窗体
				client.mainFrame.show();
			}
		});
		// 插入按钮
		insert.addActionListener(l -> {
			tabPanel.setSelectedIndex(0);
			if (persons == null)
				persons = new HashMap<String, Integer>();
			// 如果列表中包含需要插入的人物,直接返回不进行任何操作
			String person = textfield.getText().trim();
			if (person.length() < 1 || persons.containsKey(person))
				return;
			// 如果绘制的人物个数大于等于10,直接返回不进行任何操作
			if (persons.size() >= 10)
				return;
			// 计算插入的人物出现的次数
			int count = TextTool.wordStatistic(client.mainFrame.contentList, person);
			// 如果个数为0就直接返回不进行任何操作
			if (count == 0)
				return;
			// 向任务绘制列表中添加插入的人物
			persons.put(person, count);
			// 重绘存在强度
			drawByPersons();
			// 清除textfield值
			textfield.setText("");
		});
		// 移除选中的人物
		remove.addActionListener(l -> {
			tabPanel.setSelectedIndex(0);
			if (persons == null)
				return;
			// 如果列表中包含需要插入的人物,直接返回不进行任何操作
			String person = textfield.getText().trim();
			// 如果人物列表中包含指定人物就移除
			if (person != "" && persons.containsKey(person)) {
				persons.remove(person);
				// 重绘
				drawByPersons();
				// 清空textfield
				textfield.setText("");
			}
		});
		// 活跃状态
		density.addActionListener(l -> {
			tabPanel.setSelectedIndex(1);
			String person = textfield.getText().trim();
			// 需要统计密度的人物不为空,并且未记录过
			if (person != "" && !densityPerson.equals(person)) {
				// 记录当前统计的人物
				densityPerson = person;
				// 统计关键词出现的密度
				densityList = TextTool.densityStatistic(client.mainFrame.contentList, person, 30);
				// 清空textfield
				textfield.setText("");
				// 绘制密度图形
				drawByDensity();
			}
		});
		// 关系密度
		relation.addActionListener(l -> {
			tabPanel.setSelectedIndex(2);
			// 人物列表不为空
			if (persons == null || persons.size() == 0)
				return;
			// 中心人物不为空
			String person = textfield.getText().trim();
			if (person.length() < 1)
				return;
			if (person.equals(centerPerson))
				return;
			/*
			 * 计算人物之间的距离
			 */
			centerPerson = person;
			distances = TextTool.relationStatistic(client.mainFrame.contentList, person, persons.keySet(), 30);
			// 清空textfield
			textfield.setText("");
			// 绘制
			drawByDistance();
		});
		// 上传图片
		upload.addActionListener(l -> {
			Draw panel = null;
			int index = tabPanel.getSelectedIndex();
			if (index == 0) {
				panel = first;
				if (persons == null)
					return;
			} else if (index == 1) {
				panel = second;
				if (densityList == null)
					return;
			} else {
				panel = third;
				if (distances == null)
					return;
			}
			// 创建图片
			String path = createImage(panel);
			// 上传图片
			String res = "上传失败";
			if (path != null) {
				textfield.setText("正在上传...");
				System.out.println("正在上传...");
				boolean flag = client.uploadImage(path);
				if (flag)
					res = "上传成功";
			}
			textfield.setText(res);
		});
	}

	/*
	 * 创建图片
	 */
	private String createImage(Draw panel) {
		// 创建图片
		BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), 1);
		Graphics2D g = image.createGraphics();
		panel.draw(g);
		// 保存路径
		String path = client.root + "\\" + "image" + "\\" + System.currentTimeMillis() + ".jpg";
		try {
			ImageIO.write(image, "jpeg", FileTool.OpenFile(path));
			System.out.println("创建成功...");
			return path;
		} catch (Exception e) {
			System.out.println("创建失败...");
			e.printStackTrace();
			return null;
		}
	}
}
