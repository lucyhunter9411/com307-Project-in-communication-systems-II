package Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Main extends JPanel{
	public final static int WINDOW_HEIGHT = 1000;
	public final static int WINDOW_WEIGHT = 2000;
	static Simulation s;
	public static void main(String[] args) {
		s = new Simulation(10,10,4,1234567890,true);
		JFrame f = new JFrame();
		JPanel mapPanel = new Main();
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(5,5));
		mapPanel.setPreferredSize(new Dimension(WINDOW_WEIGHT, WINDOW_HEIGHT));
		
		//initialize the buttons in the control panel
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
	    controlPanel.add(buttonIterate);
	    JButton buttonStart = new JButton();
		buttonStart.setSize(100,100);
	    buttonStart.setVisible(true);
	    buttonStart.setText("Start");
	    buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	s.iterate();
            	mapPanel.repaint();
            }
        });
	    controlPanel.add(buttonStart);
	    
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