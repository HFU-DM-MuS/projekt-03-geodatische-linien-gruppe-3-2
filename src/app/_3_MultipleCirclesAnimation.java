package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import utils.ApplicationTime;

public class _3_MultipleCirclesAnimation extends Animation {

	@Override
	protected ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread) {
		ArrayList<JFrame> frames = new ArrayList<>();
		// Create main frame (window)
		JFrame frame = new JFrame("Mathematik und Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new _3_MultipleCirclesAnimationPanel(applicationTimeThread);
		frame.add(panel);
		frame.pack(); // adjusts size of the JFrame to fit the size of it's components
		frame.setVisible(true);
		
		frames.add(frame);
		return frames;
	}
	
}


class _3_MultipleCirclesAnimationPanel extends JPanel {

	// panel has a single time tracking thread associated with it
	private final ApplicationTime t;
	private double time;

	public _3_MultipleCirclesAnimationPanel(ApplicationTime thread) {
		this.t = thread;
	}

	// set this panel's preferred size for auto-sizing the container JFrame
	public Dimension getPreferredSize() {
		return new Dimension(_0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT);
	}

	// drawing operations should be done in this method
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		time = t.getTimeInSeconds();

		int[] moveDown = { 0, 10 };
		int[] moveRight = { 10, 0 };

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, _0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT);

		for (int i = 1; i <= 10; i++) {
			DrawMovingCircle(g, Color.BLUE, i * 60, 0, 50, moveDown[0], moveDown[1] * i);
		}

		for (int i = 10; i >= 1; i--) {
			DrawMovingCircle(g, Color.YELLOW, 0, i * 60, 50, moveRight[0] * i, moveRight[1]);
		}
	}

	// drawing operations can be performed in methods, as long as the Graphics
	// context (g) is given
	private void DrawMovingCircle(Graphics g, Color c, int x, int y, int size, int speedX, int speedY) {
		g.setColor(c);
		g.fillOval(x + (int) (time * speedX), y + (int) (time * speedY), size, size);
	}

}
