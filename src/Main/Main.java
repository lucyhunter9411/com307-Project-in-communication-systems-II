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
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import Enum.AgentType;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Main extends JPanel {
	public final static int WINDOW_HEIGHT = 1500;
	public final static int WINDOW_WIDTH = 1000;
	public final static int MAP_HEIGHT = 5;
	public final static int MAP_WIDTH = 5;
	public final static long DEFAULT_SEED = 1234567890;
	private final static AgentType[] predatorsList = { AgentType.Greedy, AgentType.Greedy, AgentType.Greedy,
			AgentType.Greedy };
	public final static int NBR_SIMULATION_STACK = 1000;
	static Simulation s;
	static JProgressBar progressBar = new JProgressBar(0, 100);
	static Label resultLabel = new Label("##################################################################");

	public static void main(String[] args) {
		s = new Simulation(MAP_HEIGHT, MAP_WIDTH, DEFAULT_SEED, predatorsList);
		JFrame f = new JFrame();
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(1000, 1300));
		JPanel mapPanel = new Main();
		JPanel controlPanel = new JPanel(new GridLayout(2, 2));
		mapPanel.setPreferredSize(new Dimension(1000, 1000));
		controlPanel.setPreferredSize(new Dimension(1000, 300));

		// initialize the buttons in the control panel
		JTextField textFieldSeed = new JTextField(DEFAULT_SEED + "", 12);

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
		buttonRestart.setText("Update predators and restart with Seed");
		buttonRestart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				s = new Simulation(MAP_HEIGHT, MAP_WIDTH, Long.parseLong(textFieldSeed.getText()), predatorsList);
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
				Thread backgroundThread = new Thread() {
					public void run() {
						long accumulator = 0;
						int minIteration = Integer.MAX_VALUE;
						int maxIteration = 0;
						int currentIteration;
						for (int i = 0; i < NBR_SIMULATION_STACK; i++) {
							long generatedSeed = generator.nextLong();
							s = new Simulation(MAP_HEIGHT, MAP_WIDTH, generatedSeed, predatorsList);
							// the simulation is iterated until the prey is
							// captured
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
							if (i % (NBR_SIMULATION_STACK / 100) == 0 && i != 0) {
								int percent = 1 + i * 100 / NBR_SIMULATION_STACK;
								progressBar.setValue(percent);
								controlPanel.repaint();
							}
						}
						double average = (double) (accumulator) / NBR_SIMULATION_STACK;
						resultLabel
								.setText("Finished the " + NBR_SIMULATION_STACK + " simulations; average is " + average
										+ " steps." + "\n" + "Minimum: " + minIteration + " Maximum: " + maxIteration);
						mapPanel.repaint();
					}
				};

				backgroundThread.start();
			}
		});

		// Radio button group for predator 1
		ButtonGroup predator1Group = new ButtonGroup();
		JRadioButton p1Greedy = new JRadioButton("Greedy");
		p1Greedy.setSelected(true);
		p1Greedy.setActionCommand("1 Greedy");
		JRadioButton p1Team = new JRadioButton("Team");
		p1Team.setActionCommand("1 Team");
		JRadioButton p1MCT = new JRadioButton("MCT");
		p1MCT.setActionCommand("1 MCT");
		predator1Group.add(p1Greedy);
		predator1Group.add(p1Team);
		predator1Group.add(p1MCT);

		// Radio button group for predator 2
		ButtonGroup predator2Group = new ButtonGroup();
		JRadioButton p2Greedy = new JRadioButton("Greedy");
		p2Greedy.setSelected(true);
		p2Greedy.setActionCommand("2 Greedy");
		JRadioButton p2Team = new JRadioButton("Team");
		p2Team.setActionCommand("2 Team");

		predator2Group.add(p2Greedy);
		predator2Group.add(p2Team);

		// Radio button group for predator 3
		ButtonGroup predator3Group = new ButtonGroup();
		JRadioButton p3Greedy = new JRadioButton("Greedy");
		p3Greedy.setSelected(true);
		p3Greedy.setActionCommand("3 Greedy");
		JRadioButton p3Team = new JRadioButton("Team");
		p3Team.setActionCommand("3 Team");
		predator3Group.add(p3Greedy);
		predator3Group.add(p3Team);

		// Radio button group for predator 4
		ButtonGroup predator4Group = new ButtonGroup();
		JRadioButton p4Greedy = new JRadioButton("Greedy");
		p4Greedy.setSelected(true);
		p4Greedy.setActionCommand("4 Greedy");
		JRadioButton p4Team = new JRadioButton("Team");
		p4Team.setActionCommand("4 Team");
		predator4Group.add(p4Greedy);
		predator4Group.add(p4Team);

		class RadioActionListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent event) {
				String[] buttonAction = ((AbstractButton) event.getSource()).getActionCommand().split(" ");
				int indexGroup = new Integer(buttonAction[0]);
				String type = buttonAction[1];
				AgentType returnType = null;
				switch (type) {
				case "Greedy":
					returnType = AgentType.Greedy;
					break;
				case "Team":
					returnType = AgentType.TeammateAware;
					break;
				case "MCT":
					returnType = AgentType.MonteCarlo;
					break;
				default:
					break;
				}
				predatorsList[indexGroup - 1] = returnType;
			}
		}
		// add the actionListener to the radio Buttons
		ActionListener al = new RadioActionListener();
		p1Greedy.addActionListener(al);
		p1Team.addActionListener(al);
		p1MCT.addActionListener(al);
		p2Greedy.addActionListener(al);
		p2Team.addActionListener(al);
		p3Greedy.addActionListener(al);
		p3Team.addActionListener(al);
		p4Greedy.addActionListener(al);
		p4Team.addActionListener(al);

		// add all the Cells inside the control panel
		JPanel firstCell = new JPanel(new FlowLayout(FlowLayout.LEFT));
		firstCell.add(new Label("Seed:"));
		firstCell.add(textFieldSeed);
		controlPanel.add(firstCell);

		JPanel secondCell = new JPanel();
		secondCell = new JPanel(new GridLayout(4, 0));
		secondCell.add(new Label("Predator 1:"));
		secondCell.add(p1Greedy);
		secondCell.add(p1Team);
		secondCell.add(p1MCT);
		secondCell.add(new Label("Predator 2:"));
		secondCell.add(p2Greedy);
		secondCell.add(p2Team);
		secondCell.add(new Label());
		secondCell.add(new Label("Predator 3:"));
		secondCell.add(p3Greedy);
		secondCell.add(p3Team);
		secondCell.add(new Label());
		secondCell.add(new Label("Predator 4:"));
		secondCell.add(p4Greedy);
		secondCell.add(p4Team);
		secondCell.add(new Label());
		controlPanel.add(secondCell);

		JPanel thirdCell = new JPanel(new FlowLayout(FlowLayout.LEFT));
		thirdCell.add(buttonComputeAverage);
		progressBar.setStringPainted(true);
		thirdCell.add(progressBar);
		thirdCell.add(resultLabel);
		controlPanel.add(thirdCell);
		JPanel forthCell = new JPanel(new FlowLayout(FlowLayout.LEFT));
		forthCell.add(buttonIterate);
		forthCell.add(buttonRestart);
		controlPanel.add(forthCell);

		// add all the panel in the main JFrame
		mainPanel.add(mapPanel, BorderLayout.PAGE_START);
		mainPanel.add(controlPanel, BorderLayout.PAGE_END);
		f.add(mainPanel);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBackground(Color.BLACK);
		f.setVisible(true);
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		s.draw(g, WINDOW_WIDTH);
	}
}