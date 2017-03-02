package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Main extends JPanel {
	public final static int WINDOW_HEIGHT = 1000;
	public final static int WINDOW_WEIGHT = 2000;
	static Simulation s;

	public static void main(String[] args) {
		s = new Simulation(5, 5, 4, 1234567890, false);
		JFrame f = new JFrame();
		JPanel mapPanel = new Main();
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(5, 5));
		mapPanel.setPreferredSize(new Dimension(WINDOW_WEIGHT, WINDOW_HEIGHT));

		// initialize the buttons in the control panel
		JTextField textFieldSeed = new JTextField("1234567890", 12);
		JButton buttonIterate = new JButton();
		buttonIterate.setSize(100, 100);
		buttonIterate.setVisible(true);
		buttonIterate.setText("Iterate");
		buttonIterate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				s.iterate();
				mapPanel.repaint();
			}
		});
		JButton buttonRestart = new JButton();
		buttonRestart.setSize(100, 100);
		buttonRestart.setVisible(true);
		buttonRestart.setText("Restart with Seed");
		buttonRestart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				s = new Simulation(5, 5, 4, Long.parseLong(textFieldSeed.getText()), false);
				mapPanel.repaint();
			}
		});
		JButton buttonCompute1000 = new JButton();
		buttonCompute1000.setSize(100, 100);
		buttonCompute1000.setVisible(true);
		buttonCompute1000.setText("Compute 100 simulations with Seed");
		buttonCompute1000.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long seed = Long.parseLong(textFieldSeed.getText());
				Random generator = new Random(seed);
				long accumulator = 0;
				int minIteration = Integer.MAX_VALUE;
				int maxIteration = 0;
				int currentIteration;
				for (int i = 0; i < 1000; i++) {
					long generatedSeed = generator.nextLong();
					s = new Simulation(5, 5, 4, generatedSeed, false);
					while (!s.iterate()) {
					}
					currentIteration = s.getNbrOfIteration();
					accumulator += currentIteration;
					// refresh the min and max iteration
					if (currentIteration < minIteration) {
						minIteration = currentIteration;
					}
					if (currentIteration > maxIteration) {
						maxIteration = currentIteration;
					}
					System.out.println("Seed is: "+generatedSeed);
				}
				mapPanel.repaint();
				double average = (double) (accumulator) / 1000;
				System.out.println("Finished the 100 simulations; average is " + average + " steps.");
				System.out.println("Minimum: " + minIteration + " Maximum: " + maxIteration);

			}
		});
		controlPanel.add(textFieldSeed);
		controlPanel.add(buttonIterate);
		controlPanel.add(buttonRestart);
		controlPanel.add(buttonCompute1000);
		// add all the panel in the main JFrame
		f.setLayout(new GridLayout(1, 3));
		f.add(mapPanel);
		f.add(controlPanel);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBackground(Color.BLACK);
		f.setVisible(true);
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		s.draw(g, WINDOW_HEIGHT);
	}
}