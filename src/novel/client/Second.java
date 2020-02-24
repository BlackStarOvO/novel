package novel.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.List;

/**
 * ѡѡ��ĵڶ������
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
	 * �Զ������
	 */
	@Override
	public void paint(Graphics g) {
		// ���densityΪ��,ֱ����������
		if (densityList == null || densityList.size() == 0)
			return;
		draw(g);
	}

	@Override
	public void draw(Graphics g) {
		/*
		 * ÿ�仰30����,ÿ��30�仰
		 */
		int height = this.getHeight();
		int width = this.getWidth();

		int total = densityList.size(); // �ܸ���
		// ����ÿ����ʾ����
		int line = 0; // ÿ����ʾ����
		if (total > height * width) {
			line = height; // ÿ���������ٱ���1������
		} else {
			line = (int) Math.sqrt(total); // ÿ����ʾ����
		}
		// ����
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		// ���ƾ��γ���
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
