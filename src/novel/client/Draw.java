package novel.client;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * �Զ������
 * @author 22442
 *
 */
@SuppressWarnings("serial")
public abstract class Draw extends JPanel{

	/**
	 * ���еĻ��Ʒ���
	 * @param g
	 */
	abstract void draw(Graphics g);
}
