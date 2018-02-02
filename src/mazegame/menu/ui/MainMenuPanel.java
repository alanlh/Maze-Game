package mazegame.menu.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainMenuPanel extends JPanel {

	// Buttons in mainMenu
	JLabel mazeTitle = new JLabel ("Maze", JLabel.CENTER);
	JButton startNewGame = new JButton("Start New Game");
	JButton exitGame = new JButton("Exit");
	
	public MainMenuPanel() {
		displayMenuObjects();
		addButtonListeners();
	}
	
	private void displayMenuObjects() {
		// Panels
		// ------ mainMenu layout ------ //
		// mainMenu uses boxlayout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.DARK_GRAY);
		
		// Adds expandable/retractable space at top
		add(Box.createVerticalGlue());
		add(Box.createVerticalGlue());
		
		// Font is the best way to increase size
		add(mazeTitle);
		mazeTitle.setAlignmentX(CENTER_ALIGNMENT);
		mazeTitle.setForeground(new Color(255, 255, 255));
		mazeTitle.setFont(new Font("Sans Serif", Font.BOLD, 96));
		
		// Bigger space: is there a way to customize glue?
		add(Box.createVerticalGlue());
		add(Box.createVerticalGlue());
		add(Box.createVerticalGlue());

		// Add start new game button
		add(startNewGame);
		startNewGame.setAlignmentX(CENTER_ALIGNMENT);
		startNewGame.setPreferredSize(new Dimension(200,50));
		startNewGame.setFont(new Font("Sans Serif", Font.BOLD, 24));
		
		// Fixed (non expandable) space between the two buttons
		add(Box.createRigidArea(new Dimension(0,50)));
		
		// Add exit game button
		add(exitGame);
		exitGame.setAlignmentX(CENTER_ALIGNMENT);
		exitGame.setPreferredSize(new Dimension(20,50));
		exitGame.setFont(new Font("Sans Serif", Font.BOLD, 24));
		
		//Makes sure there space at the bottom, because glue.
		add(Box.createVerticalGlue());
		add(Box.createVerticalGlue());
	}
	
	private void addButtonListeners() {
		// ---- Adds action items to buttons ---- //
		exitGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent click) {
				((ApplicationFrame) SwingUtilities.getWindowAncestor(MainMenuPanel.this)).exitGame();
			}
		});
		startNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((ApplicationFrame) SwingUtilities.getWindowAncestor(MainMenuPanel.this)).displayGame();	
			}
		});
	}
	
}
