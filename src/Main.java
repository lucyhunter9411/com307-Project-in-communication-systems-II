import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Main extends JPanel{
	public final static int WINDOW_HEIGHT = 1000;
	public final static int WINDOW_WEIGHT = 2000;
	static Simulation s;
	public static void main(String[] args) {
		s = new Simulation(10,10,4);
		JFrame f = new JFrame();
		JPanel mapPanel = new Main();
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(5,5));
		JButton button = new JButton();
		mapPanel.setPreferredSize(new Dimension(WINDOW_WEIGHT, WINDOW_HEIGHT));
		button.setSize(100,100);
	    button.setVisible(true);
	    controlPanel.add(button);
	    button.setText("Iterate");
	    button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	boolean isPreyCaptured = s.iterate();
            	mapPanel.repaint();
            }
        });
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