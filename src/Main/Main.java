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
public class Main extends JPanel{
	public final static int WINDOW_HEIGHT = 1000;
	public final static int WINDOW_WEIGHT = 2000;
	static Simulation s;
	public static void main(String[] args) {
		s = new Simulation(5,5,4,1234567890,true);
		JFrame f = new JFrame();
		JPanel mapPanel = new Main();
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(5,5));
		mapPanel.setPreferredSize(new Dimension(WINDOW_WEIGHT, WINDOW_HEIGHT));
		
		//initialize the buttons in the control panel
		JTextField textFieldSeed = new JTextField("1234567890",12);
		JButton buttonIterate = new JButton();
		buttonIterate.setSize(100,100);
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
	    buttonRestart.setSize(100,100);
	    buttonRestart.setVisible(true);
	    buttonRestart.setText("Restart with Seed");
	    buttonRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	s = new Simulation(5,5,4,Long.parseLong(textFieldSeed.getText()),true);
            	mapPanel.repaint();
            }
        });
	    JButton buttonCompute100 = new JButton();
	    buttonCompute100.setSize(100,100);
	    buttonCompute100.setVisible(true);
	    buttonCompute100.setText("Compute 100 simulations with Seed");
	    buttonCompute100.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	long seed = Long.parseLong(textFieldSeed.getText());
            	Random generator = new Random(seed);
            	long accumulator = 0;
            	for(int i = 0;i<100;i++){
            		s = new Simulation(5,5,4,generator.nextLong(),true);
            		while(!s.iterate()){}
            		accumulator += s.getNbrOfIteration();
            	}
            	mapPanel.repaint();
            	double average = (double)(accumulator)/100;
            	System.out.println("Finished the 100 simulations; average is "+average+" steps.");
            }
        });
	    controlPanel.add(textFieldSeed);
	    controlPanel.add(buttonIterate);
	    controlPanel.add(buttonRestart);
	    controlPanel.add(buttonCompute100);
	    //add all the panel in the main JFrame
	    f.setLayout(new GridLayout(1,3));
		f.add(mapPanel);
		f.add(controlPanel);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBackground(Color.BLACK);
		f.setVisible(true);
	}

	public void update(Graphics g){
		paint(g);
	}
	
	public void paint(Graphics g) {
		s.draw(g,WINDOW_HEIGHT);
	}
}