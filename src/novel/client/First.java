package novel.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 选选项卡的第一块面板
 * 
 * @author 22442
 *
 */
@SuppressWarnings("serial")
public class First extends Draw {

	private Map<String, Integer> persons;

	public void setPersons(Map<String, Integer> persons) {
		this.persons = persons;
	}

	public First(LayoutManager layout) {
		this.setLayout(layout);
	}

	public First() {
	}

	/*
	 * 计算比例尺
	 */
	private double scale(int maxValue) {
		double max = 0;
		for (int value : persons.values()) {
			if (max < value)
				max = value;
		}
		return maxValue / max;
	}

	/**
	 * 自定义绘制
	 */
	@Override
	public void paint(Graphics g) {
		// 如果persons为空,不进行绘制
		if (persons == null || persons.size() == 0)
			return;
		draw(g);
	}
	
	@Override
	public void draw(Graphics g) {
		// 绘图区域的宽度和高度
		int height = this.getHeight();
		int width = this.getWidth();
		// 获取绘制画笔
		// 先绘制背景区域为灰色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		// 绘制矩形的宽度(绘图区域的宽度/矩形个数*2-1)
		int w = width / (persons.size() * 2 - 1);
		// 循环绘制矩形
		int i = 0, padding = 10;
		for (Entry<String, Integer> entry : persons.entrySet()) {
			g.setColor(Color.BLUE);
			// 绘制矩形的高度
			int h = (int) (entry.getValue() * scale(height) - padding);
			// 绘制矩形
			g.fillRect(i * w, height - h + padding, w, h);
			g.setColor(Color.BLACK);
			// 绘制文字
			String s = new String(entry.getKey()).concat("(").concat(entry.getValue().toString()).concat(")");
			g.drawString(s, (int) ((i + 0.2) * w), height - padding);
			i += 2;
		}
	}
}
