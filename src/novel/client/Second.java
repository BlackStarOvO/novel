package novel.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.List;

/**
 * 选选项卡的第二块面板
 * 
 * @author 22442
 *
 */
@SuppressWarnings("serial")
public class Second extends Draw{

	private List<Boolean> densityList;
	
	public void setDensityList(List<Boolean> list) {
		this.densityList = list;
	}

	public Second(LayoutManager layout) {
		this.setLayout(layout);
	}

	public Second() {
	}

	/**
	 * 自定义绘制
	 */
	@Override
	public void paint(Graphics g) {
		// 如果density为空,直接跳出函数
		if (densityList == null || densityList.size() == 0)
			return;
		draw(g);
	}

	@Override
	public void draw(Graphics g) {
		/*
		 * 每句话30个字,每行30句话
		 */
		int height = this.getHeight();
		int width = this.getWidth();

		int total = densityList.size(); // 总个数
		// 计算每行显示个数
		int line = 0; // 每行显示个数
		if (total > height * width) {
			line = height; // 每个方格至少保留1个像素
		} else {
			line = (int) Math.sqrt(total); // 每行显示个数
		}
		// 背景
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		// 绘制矩形长高
		int w = width / line;
		int h = height / line;
		int index = 0;
		g.setColor(Color.GREEN);
		for (boolean b : densityList) {
			if (b) {
				int x = index % line * w;
				int y = index / line * h;
				g.fillRect(x, y, w, h);
			}
			index++;
		}
	}
}
