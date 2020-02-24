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
 * ͼ�δ���,���ڻ���ͼ��
 * 
 * @author 22442
 *
 */
@SuppressWarnings("serial")
public class GraphFrame extends JFrame {

	// ͼ�δ���
	private JFrame frame;
	// �ͻ�������
	private Client client;
	// ѡ�
	private JTabbedPane tabPanel;
	// ���������ı���
	private JTextField textfield;
	// ���,ɾ��,�ϴ�,��Ծ,��ϵ��ť
	private JButton insert, remove, upload, density, relation;
	// ��ͼ���
	private First first;
	private Second second;
	private Third third;
	// �����б�
	private Map<String, Integer> persons;
	// �ܶ��б�
	private List<Boolean> densityList;
	// �ܶ�ͳ������
	private String densityPerson = "";
	// ��ϵ��������
	private String centerPerson = "";
	// ����֮��ľ���
	private Map<String, Integer> distances;

	public GraphFrame(Client client) {
		this.client = client;
	}

	/*
	 * ��ʾ����
	 */
	public void show() {
		// ��ʼ������
		if (frame == null)
			initFrame();
		// ��ʾ����
		frame.setVisible(true);
	}

	/*
	 * ����persons��������(���ǻ���������ڸ�ǿ��)
	 */
	private void drawByPersons() {
		first.setPersons(persons);
		first.repaint();
	}

	/*
	 * ����density��������(���ǻ��Ƴ����ܶ�)
	 */
	private void drawByDensity() {
		second.setDensityList(densityList);
		second.repaint();
	}

	/*
	 * ����distances��������(���ǻ��ƹ�ϵ�ܶ�)
	 */
	private void drawByDistance() {
		third.setCenter(centerPerson);
		third.setDistances(distances);
		third.repaint();
	}

	/*
	 * ��ʼ�����ƴ���
	 */
	private void initFrame() {
		frame = new JFrame("���ƴ���");
		/*
		 * ���ô���
		 */
		frame.setBounds(100, 100, 600, 600);
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // ����FrameĬ�Ϲر��¼�
		frame.setLayout(new BorderLayout());
		/*
		 * ��ӿؼ�
		 */
		// ѡ����,����ѡ�����������
		tabPanel = new JTabbedPane(JTabbedPane.TOP);
		frame.add(tabPanel, BorderLayout.CENTER);
		// ���tab
		first = new First(new BorderLayout());
		tabPanel.addTab("����ǿ��", first);
		tabPanel.setTabLayoutPolicy(0);
		second = new Second(new BorderLayout());
		tabPanel.addTab("��Ծ״̬", second);
		third = new Third(new BorderLayout());
		tabPanel.addTab("��ϵ�ܶ�", third);
		// ѡ���һ��panel
		tabPanel.setSelectedIndex(0);
		// ����panel
		JPanel topPanel = new JPanel(new BorderLayout());
		frame.add(topPanel, BorderLayout.NORTH);
		// �����ı���
		textfield = new JTextField();
		topPanel.add(textfield, BorderLayout.CENTER);
		// ��һ��Jpanel
		JPanel panel = new JPanel(new GridLayout(1, 4));
		topPanel.add(panel, BorderLayout.EAST);
		// ���,�Ƴ�,�ϴ�,�ܶȰ�ť
		insert = new JButton("���");
		panel.add(insert);
		remove = new JButton("�Ƴ�");
		panel.add(remove);
		density = new JButton("��Ծ");
		panel.add(density);
		relation = new JButton("��ϵ");
		panel.add(relation);
		upload = new JButton("�ϴ�");
		panel.add(upload);
		/*
		 * ��Ӽ����¼�
		 */
		addListener();
	}

	/*
	 * �����¼�
	 */
	private void addListener() {
		// �رմ���
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				// �رջ��ƴ���
				frame.dispose();
				// ��ʾ������
				client.mainFrame.show();
			}
		});
		// ���밴ť
		insert.addActionListener(l -> {
			tabPanel.setSelectedIndex(0);
			if (persons == null)
				persons = new HashMap<String, Integer>();
			// ����б��а�����Ҫ���������,ֱ�ӷ��ز������κβ���
			String person = textfield.getText().trim();
			if (person.length() < 1 || persons.containsKey(person))
				return;
			// ������Ƶ�����������ڵ���10,ֱ�ӷ��ز������κβ���
			if (persons.size() >= 10)
				return;
			// ��������������ֵĴ���
			int count = TextTool.wordStatistic(client.mainFrame.contentList, person);
			// �������Ϊ0��ֱ�ӷ��ز������κβ���
			if (count == 0)
				return;
			// ����������б�����Ӳ��������
			persons.put(person, count);
			// �ػ����ǿ��
			drawByPersons();
			// ���textfieldֵ
			textfield.setText("");
		});
		// �Ƴ�ѡ�е�����
		remove.addActionListener(l -> {
			tabPanel.setSelectedIndex(0);
			if (persons == null)
				return;
			// ����б��а�����Ҫ���������,ֱ�ӷ��ز������κβ���
			String person = textfield.getText().trim();
			// ��������б��а���ָ��������Ƴ�
			if (person != "" && persons.containsKey(person)) {
				persons.remove(person);
				// �ػ�
				drawByPersons();
				// ���textfield
				textfield.setText("");
			}
		});
		// ��Ծ״̬
		density.addActionListener(l -> {
			tabPanel.setSelectedIndex(1);
			String person = textfield.getText().trim();
			// ��Ҫͳ���ܶȵ����ﲻΪ��,����δ��¼��
			if (person != "" && !densityPerson.equals(person)) {
				// ��¼��ǰͳ�Ƶ�����
				densityPerson = person;
				// ͳ�ƹؼ��ʳ��ֵ��ܶ�
				densityList = TextTool.densityStatistic(client.mainFrame.contentList, person, 30);
				// ���textfield
				textfield.setText("");
				// �����ܶ�ͼ��
				drawByDensity();
			}
		});
		// ��ϵ�ܶ�
		relation.addActionListener(l -> {
			tabPanel.setSelectedIndex(2);
			// �����б�Ϊ��
			if (persons == null || persons.size() == 0)
				return;
			// �������ﲻΪ��
			String person = textfield.getText().trim();
			if (person.length() < 1)
				return;
			if (person.equals(centerPerson))
				return;
			/*
			 * ��������֮��ľ���
			 */
			centerPerson = person;
			distances = TextTool.relationStatistic(client.mainFrame.contentList, person, persons.keySet(), 30);
			// ���textfield
			textfield.setText("");
			// ����
			drawByDistance();
		});
		// �ϴ�ͼƬ
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
			// ����ͼƬ
			String path = createImage(panel);
			// �ϴ�ͼƬ
			String res = "�ϴ�ʧ��";
			if (path != null) {
				textfield.setText("�����ϴ�...");
				System.out.println("�����ϴ�...");
				boolean flag = client.uploadImage(path);
				if (flag)
					res = "�ϴ��ɹ�";
			}
			textfield.setText(res);
		});
	}

	/*
	 * ����ͼƬ
	 */
	private String createImage(Draw panel) {
		// ����ͼƬ
		BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), 1);
		Graphics2D g = image.createGraphics();
		panel.draw(g);
		// ����·��
		String path = client.root + "\\" + "image" + "\\" + System.currentTimeMillis() + ".jpg";
		try {
			ImageIO.write(image, "jpeg", FileTool.OpenFile(path));
			System.out.println("�����ɹ�...");
			return path;
		} catch (Exception e) {
			System.out.println("����ʧ��...");
			e.printStackTrace();
			return null;
		}
	}
}
