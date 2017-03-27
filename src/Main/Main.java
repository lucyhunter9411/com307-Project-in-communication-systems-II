package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class Main extends JPanel {
	public final static int WINDOW_HEIGHT = 1500;
	public final static int WINDOW_WIDTH = 1000;
	public final static int MAP_HEIGHT = 5;
	public final static int MAP_WIDTH = 5;
	public final static long DEFAULT_SEED = 1234567890;
	// NBR_PREDATOR must be 4 if we use at least one team mate aware predator
	// TODO
	private static int nbrTeamPredator = 0;
	private static boolean useOneMtcPredator = true;
	private static int nbrGreedyPredator = 3;
	public final static int NBR_SIMULATION_STACK = 1000;
	static Simulation s;
	 //label info of total of predator since it's not really clear
	static Label labelNbrPredator = new Label("Total number of predator: 4");

	public static void main(String[] args) {
		s = new Simulation(MAP_HEIGHT, MAP_WIDTH, nbrTeamPredator, DEFAULT_SEED, nbrGreedyPredator, useOneMtcPredator);
		JFrame f = new JFrame();
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(1000,1300));
		JPanel mapPanel = new Main();
		JPanel controlPanel = new JPanel(new GridLayout(2, 2));
		mapPanel.setPreferredSize(new Dimension(1000, 1000));
		controlPanel.setPreferredSize(new Dimension(1000, 300));

		
		
		// initialize the buttons in the control panel
		JTextField textFieldSeed = new JTextField(DEFAULT_SEED + "", 12);
		JSpinner spinnerNbrGreedy = new JSpinner(new SpinnerNumberModel(new Integer(3), // value
				new Integer(0), // min
				new Integer(10), // max
				new Integer(1) // step
				));
		spinnerNbrGreedy.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner spinner = (JSpinner) e.getSource();
				int currentValue  =(Integer)spinner.getValue();
				nbrGreedyPredator = currentValue;
				updatePredatorLabel();
			}
		});
		
		// spinner to define the number of teammate aware predator
		JSpinner spinnerNbrTeam = new JSpinner(new SpinnerNumberModel(new Integer(0), // value
				new Integer(0), // min
				new Integer(10), // max
				new Integer(1) // step
				));
		spinnerNbrTeam.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner spinner = (JSpinner) e.getSource();
				nbrTeamPredator = (Integer)spinner.getValue();
				updatePredatorLabel();
			}
		});

		// checkBox to define if an agent is a MTC predator
		JCheckBox checkButtonMTC = new JCheckBox();
		checkButtonMTC.setSelected(true);
		checkButtonMTC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				useOneMtcPredator = abstractButton.getModel().isSelected();
				updatePredatorLabel();
			}
		});

		// button to iterate once on the simulation
		JButton buttonIterate = new JButton();
		buttonIterate.setVisible(true);
		buttonIterate.setText("Iterate");
		buttonIterate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (s.iterate()) {
					int currentIteration = s.getNbrOfIteration();
					System.out.println("captured in " + currentIteration + " steps");
				}
				mapPanel.repaint();
			}
		});
		
		// button to restart in function of the param of the inputs
		JButton buttonRestart = new JButton();
		buttonRestart.setVisible(true);
		buttonRestart.setText("Restart with Seed");
		buttonRestart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				s = new Simulation(MAP_HEIGHT, MAP_WIDTH, nbrTeamPredator, Long.parseLong(textFieldSeed.getText()),
						nbrGreedyPredator, useOneMtcPredator);
				mapPanel.repaint();
			}
		});

		// button to run N simulation and compute the average
		JButton buttonComputeAverage = new JButton();
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
					s = new Simulation(MAP_HEIGHT, MAP_WIDTH, nbrTeamPredator, generatedSeed, nbrGreedyPredator,
							useOneMtcPredator);
					// the simulation is iterated until the prey is captured
					while (!s.iterate()) {
					}
					currentIteration = s.getNbrOfIteration();
					System.out.println("captured in " + currentIteration + " steps");
					accumulator += currentIteration;
					// refresh the min and max iteration
					if (currentIteration < minIteration) {
						minIteration = currentIteration;
					}
					if (currentIteration > maxIteration) {
						maxIteration = currentIteration;
					}
					// System.out.println("Seed is: "+generatedSeed);
					if (i % (NBR_SIMULATION_STACK / 10) == 0 && i != 0) {
						int percent = i * 100 / NBR_SIMULATION_STACK;
						System.out.println(
								"Simulation at " + percent + "% *****************************************************");
					}
				}
				mapPanel.repaint();
				double average = (double) (accumulator) / NBR_SIMULATION_STACK;
				System.out.println(
						"Finished the " + NBR_SIMULATION_STACK + " simulations; average is " + average + " steps.");
				System.out.println("Minimum: " + minIteration + " Maximum: " + maxIteration);

			}
		});
		JPanel firstLine = new JPanel(new FlowLayout(FlowLayout.LEFT));
		firstLine.add(new Label("Seed:"));
		firstLine.add(textFieldSeed);
		firstLine.add(new Label("Use one MTC Predator:"));
		firstLine.add(checkButtonMTC);
		controlPanel.add(firstLine);

		JPanel secondLine = new JPanel(new FlowLayout(FlowLayout.LEFT));
		secondLine.add(new Label("Number of greedy agent:"));
		secondLine.add(spinnerNbrGreedy);
		secondLine.add(new Label("Number of teammate aware agent:"));
		secondLine.add(spinnerNbrTeam);
		secondLine.add(labelNbrPredator);
		controlPanel.add(secondLine);

		JPanel thirdLine = new JPanel(new FlowLayout(FlowLayout.LEFT));
		thirdLine.add(buttonIterate);
		thirdLine.add(buttonRestart);
		thirdLine.add(buttonComputeAverage);
		controlPanel.add(thirdLine);

		// add all the panel in the main JFrame
		mainPanel.add(mapPanel,BorderLayout.PAGE_START);
		mainPanel.add(controlPanel,BorderLayout.PAGE_END);
		f.add(mainPanel);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBackground(Color.BLACK);
		f.setVisible(true);
	}

	public static void updatePredatorLabel(){
		int nbrPredator = nbrTeamPredator + nbrGreedyPredator;
		if(useOneMtcPredator){
			nbrPredator++;
		}
		labelNbrPredator.setText("Total number of predator: "+nbrPredator);
	}
	
	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		s.draw(g, WINDOW_WIDTH);
	}
}