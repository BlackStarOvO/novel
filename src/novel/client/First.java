package novel.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ѡѡ��ĵ�һ�����
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
	 * ���������
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
	 * �Զ������
	 */
	@Override
	public void paint(Graphics g) {
		// ���personsΪ��,�����л���
		if (persons == null || persons.size() == 0)
			return;
		draw(g);
	}
	
	@Override
	public void draw(Graphics g) {
		// ��ͼ����Ŀ�Ⱥ͸߶�
		int height = this.getHeight();
		int width = this.getWidth();
		// ��ȡ���ƻ���
		// �Ȼ��Ʊ�������Ϊ��ɫ
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		// ���ƾ��εĿ��(��ͼ����Ŀ��/���θ���*2-1)
		int w = width / (persons.size() * 2 - 1);
		// ѭ�����ƾ���
		int i = 0, padding = 10;
		for (Entry<String, Integer> entry : persons.entrySet()) {
			g.setColor(Color.BLUE);
			// ���ƾ��εĸ߶�
			int h = (int) (entry.getValue() * scale(height) - padding);
			// ���ƾ���
			g.fillRect(i * w, height - h + padding, w, h);
			g.setColor(Color.BLACK);
			// ��������
			String s = new String(entry.getKey()).concat("(").concat(entry.getValue().toString()).concat(")");
			g.drawString(s, (int) ((i + 0.2) * w), height - padding);
			i += 2;
		}
	}
}
