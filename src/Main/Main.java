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

@SuppressWarnings("serial")
public class Main extends JPanel {
	public final static int WINDOW_HEIGHT = 1000;
	public final static int WINDOW_WEIGHT = 2000;
	public final static int MAP_HEIGHT = 5;
	public final static int MAP_WIDTH = 5;
	public final static long DEFAULT_SEED = 1234567890;
	// NBR_PREDATOR must be 4 if we use at least one team mate aware predator
	// TODO
	public final static int NBR_PREDATOR = 4;
	public final static boolean USE_ONE_MTC_PREDATOR = true; 
	public final static int NBR_GREEDY_PREDATOR = 0;
	public final static int NBR_SIMULATION_STACK = 1000;
	static Simulation s;

	public static void main(String[] args) {
		s = new Simulation(MAP_HEIGHT, MAP_WIDTH, NBR_PREDATOR, DEFAULT_SEED, NBR_GREEDY_PREDATOR);
		JFrame f = new JFrame();
		JPanel mapPanel = new Main();
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(5, 5));
		mapPanel.setPreferredSize(new Dimension(WINDOW_WEIGHT, WINDOW_HEIGHT));

		// initialize the buttons in the control panel
		JTextField textFieldSeed = new JTextField(DEFAULT_SEED + "", 12);
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
				s = new Simulation(MAP_HEIGHT, MAP_WIDTH, NBR_PREDATOR, Long.parseLong(textFieldSeed.getText()),
						NBR_GREEDY_PREDATOR);
				mapPanel.repaint();
			}
		});
		JButton buttonComputeAverage = new JButton();
		buttonComputeAverage.setSize(100, 100);
		buttonComputeAverage.setVisible(true);
		buttonComputeAverage.setText("Compute " + NBR_SIMULATION_STACK + " simulations with Seed");
		buttonComputeAverage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long initialSeed = Long.parseLong(textFieldSeed.getText());
				Random generator = new Random(initialSeed);
				long accumulator = 0;
				int minIteration = Integer.MAX_VALUE;
				int maxIteration = 0;
				int currentIteration;
				for (int i = 0; i < NBR_SIMULATION_STACK; i++) {
					long generatedSeed = generator.nextLong();
					s = new Simulation(MAP_HEIGHT, MAP_WIDTH, NBR_PREDATOR, generatedSeed, NBR_GREEDY_PREDATOR);
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
					// System.out.println("Seed is: "+generatedSeed);
				}
				mapPanel.repaint();
				double average = (double) (accumulator) / NBR_SIMULATION_STACK;
				System.out.println(
						"Finished the " + NBR_SIMULATION_STACK + " simulations; average is " + average + " steps.");
				System.out.println("Minimum: " + minIteration + " Maximum: " + maxIteration);

			}
		});
		controlPanel.add(textFieldSeed);
		controlPanel.add(buttonIterate);
		controlPanel.add(buttonRestart);
		controlPanel.add(buttonComputeAverage);
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