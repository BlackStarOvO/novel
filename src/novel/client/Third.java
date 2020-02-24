package novel.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * ѡѡ��ĵ��������
 * 
 * @author 22442
 *
 */
@SuppressWarnings("serial")
public class Third extends Draw {

	private Map<String, Integer> distances;
	public String center;
	private int[] arr = null;
	private int height;
	private int width;

	public void setCenter(String center) {
		this.center = center;
	}

	public void setDistances(Map<String, Integer> distances) {
		this.distances = distances;
		findMaxMin();
	}

	public Third(LayoutManager layout) {
		this.setLayout(layout);
	}

	public Third() {
	}

	private void findMaxMin() {
		int max = 0;
		int min = Integer.MAX_VALUE;
		for (int value : distances.values()) {
			if (max < value)
				max = value;
			if (min > value)
				min = value;
		}
		arr = new int[2];
		arr[0] = max;
		arr[1] = min;
	}

	/**
	 * �Զ������
	 */
	@Override
	public void paint(Graphics g) {
		if (distances == null || distances.size() == 0)
			return;
		draw(g);
	}

	/*
	 * ���Ƶ�ϵ�е�
	 */
	private List<Circle> getDrawList(int d) {
		List<Circle> list = new LinkedList<Circle>();
		Circle circle = null;
		// �������ĵ�
		int centerX = d / 2;
		int centerY = height / 2;
		// ����
		int size = distances.size();
		// ���ĵ�
		circle = getCircle(centerX, centerY, 0, 0, size);
		circle.text = center;
		circle.d = d;
		circle.c = Color.RED;
		list.add(circle);
		// �������Ｏ
		findMaxMin();
		int index = 1;
		// ����
		Map<Integer, String> tree = sort(distances);
		for (Entry<Integer, String> entry : tree.entrySet()) {
			// ��������
			circle = getCircle(width - d / 2, centerY, index++, size);
			// ����
			circle.text = entry.getValue();
			// ֱ��
			circle.d = d;
			if (!circle.text.equals(center))
				list.add(circle);
		}
		return list;
	}
	
	private Map<Integer, String> sort(Map<String, Integer> map) {
		Map<Integer,String> treeMap = new TreeMap<Integer, String>((o1,o2)->{
			int result = o1 - o2;
			if(result == 0)
				result = 1;
			return result;
		});
		for (Entry<String, Integer> entry : distances.entrySet()) {
			int value = entry.getValue(); // ��������
			String key = entry.getKey();
			treeMap.put(value, key);
		}
		return treeMap;
	}

	/*
	 * �������Ϣ
	 */
	private Circle getCircle(int centerX, int centerY, int rela, int size) {
		/*
		 * rela�����ĵ����λ��,��360�ֽ��size�� 360/size*rela/360*pi
		 */
		Circle circle = new Circle();
		double space = (width - 45.0) / (size+0.);
		// ��������
		circle.x = (int) (space * rela);
		circle.y = centerY;
		circle.x = reset(circle.x, width);
		circle.y = reset(circle.y, height);
		// ������ɫ�����ࣩ
		Color c = new Color(255, 0, 0, 255 - (int)(rela * (255 / (size + 0.))));
		circle.c = c;
		return circle;
	}
	
	/*
	 * �������Ϣ
	 */
	private Circle getCircle(int centerX, int centerY, int rela, int dis, int size) {
		/*
		 * rela�����ĵ����λ��,��360�ֽ��size�� 360/size*rela/360*pi
		 */
		Circle circle = new Circle();
		// �������
		double angel = (((360. / size) * rela) / 360) * 2 * Math.PI;
		// ������
		while (scale(height, dis) < 45 && dis != 0)
			dis = dis * 2;
		// ��������
		circle.x = (int) (centerX + Math.cos(angel) * scale(height, dis));
		circle.y = (int) (centerY - Math.sin(angel) * scale(height, dis));
		circle.x = reset(circle.x, width);
		circle.y = reset(circle.y, height);
		// ������ɫ�����ࣩ
		if(dis < 30)
			circle.c = Color.RED;
		else if(dis < 60)
			circle.c = Color.ORANGE;
		else if(dis < 90)
			circle.c = Color.YELLOW;
		else if(dis < 120)
			circle.c = Color.GREEN;
		else if(dis < 150)
			circle.c = Color.BLUE;
		else if(dis < 180)
			circle.c = Color.DARK_GRAY;
		else 
			circle.c = Color.BLACK;
		return circle;
	}

	private double scale(double maxValue, int value) {
		findMaxMin();
		double res = maxValue / (arr[0] - arr[1]);
		return (value - arr[1]) * res;
	}

	/*
	 * �߽���
	 */
	private int reset(int n, int border) {
		if (n < 10)
			return 10;
		if (n > border - 10)
			return border - 10;
		return n;
	}

	class Circle {
		// ���Ƶ���ʼ��
		public int x;
		public int y;
		// ���Ƶ�ֱ��
		public int d;
		// ��ɫ
		public Color c;
		// ����
		public String text = "NONE";

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "[x:" + x + ",y:" + y + ",text:" + text + "]";
		}
	}

	@Override
	public void draw(Graphics g) {
		// ��ȡ����Ŀ�Ⱥ͸߶�
		height = this.getHeight();
		width = this.getWidth();
		// ������ǽ���Ƴɰ�ɫ
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		// ��ȡ�����б�
		List<Circle> list = getDrawList(45);
		// ����
		for (Circle circle : list) {
			g.setColor(circle.c);
			g.drawOval(circle.x, circle.y, circle.d, circle.d);
			g.setColor(Color.BLACK);
			g.drawString(circle.text, circle.x + circle.d / 2 - 10 * circle.text.length(), circle.y + circle.d / 2);
		}
	}
}
