package novel.client;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * 自定义面板
 * @author 22442
 *
 */
@SuppressWarnings("serial")
public abstract class Draw extends JPanel{

	/**
	 * 共有的绘制方法
	 * @param g
	 */
	abstract void draw(Graphics g);
}
