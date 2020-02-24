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
 * 选选项卡的第三块面板
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
	 * 自定义绘制
	 */
	@Override
	public void paint(Graphics g) {
		if (distances == null || distances.size() == 0)
			return;
		draw(g);
	}

	/*
	 * 绘制的系列点
	 */
	private List<Circle> getDrawList(int d) {
		List<Circle> list = new LinkedList<Circle>();
		Circle circle = null;
		// 计算中心点
		int centerX = d / 2;
		int centerY = height / 2;
		// 个数
		int size = distances.size();
		// 中心点
		circle = getCircle(centerX, centerY, 0, 0, size);
		circle.text = center;
		circle.d = d;
		circle.c = Color.RED;
		list.add(circle);
		// 计算人物集
		findMaxMin();
		int index = 1;
		// 排序
		Map<Integer, String> tree = sort(distances);
		for (Entry<Integer, String> entry : tree.entrySet()) {
			// 计算坐标
			circle = getCircle(width - d / 2, centerY, index++, size);
			// 文字
			circle.text = entry.getValue();
			// 直径
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
			int value = entry.getValue(); // 关联个数
			String key = entry.getKey();
			treeMap.put(value, key);
		}
		return treeMap;
	}

	/*
	 * 计算点信息
	 */
	private Circle getCircle(int centerX, int centerY, int rela, int size) {
		/*
		 * rela是中心的相对位置,将360分解成size个 360/size*rela/360*pi
		 */
		Circle circle = new Circle();
		double space = (width - 45.0) / (size+0.);
		// 计算坐标
		circle.x = (int) (space * rela);
		circle.y = centerY;
		circle.x = reset(circle.x, width);
		circle.y = reset(circle.y, height);
		// 构建颜色（分类）
		Color c = new Color(255, 0, 0, 255 - (int)(rela * (255 / (size + 0.))));
		circle.c = c;
		return circle;
	}
	
	/*
	 * 计算点信息
	 */
	private Circle getCircle(int centerX, int centerY, int rela, int dis, int size) {
		/*
		 * rela是中心的相对位置,将360分解成size个 360/size*rela/360*pi
		 */
		Circle circle = new Circle();
		// 间隔度数
		double angel = (((360. / size) * rela) / 360) * 2 * Math.PI;
		// 扩大间距
		while (scale(height, dis) < 45 && dis != 0)
			dis = dis * 2;
		// 计算坐标
		circle.x = (int) (centerX + Math.cos(angel) * scale(height, dis));
		circle.y = (int) (centerY - Math.sin(angel) * scale(height, dis));
		circle.x = reset(circle.x, width);
		circle.y = reset(circle.y, height);
		// 构建颜色（分类）
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
	 * 边界检测
	 */
	private int reset(int n, int border) {
		if (n < 10)
			return 10;
		if (n > border - 10)
			return border - 10;
		return n;
	}

	class Circle {
		// 绘制的起始点
		public int x;
		public int y;
		// 绘制的直径
		public int d;
		// 颜色
		public Color c;
		// 文字
		public String text = "NONE";

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "[x:" + x + ",y:" + y + ",text:" + text + "]";
		}
	}

	@Override
	public void draw(Graphics g) {
		// 获取画板的宽度和高度
		height = this.getHeight();
		width = this.getWidth();
		// 将背景墙绘制成白色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		// 获取绘制列表
		List<Circle> list = getDrawList(45);
		// 绘制
		for (Circle circle : list) {
			g.setColor(circle.c);
			g.drawOval(circle.x, circle.y, circle.d, circle.d);
			g.setColor(Color.BLACK);
			g.drawString(circle.text, circle.x + circle.d / 2 - 10 * circle.text.length(), circle.y + circle.d / 2);
		}
	}
}
