package mazegame.menu.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TopBarMenuPanel extends JPanel {
	
	JButton goToMenuButton = new JButton("Back to Menu");
	
	public TopBarMenuPanel() {
		// Initialized, but does not appear here. 
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(Box.createHorizontalGlue());
		
		add(goToMenuButton);
		setBackground(Color.BLACK);
		
		goToMenuButton.setFont(new Font ("Sans Serif", Font.PLAIN, 24));		
						
		goToMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((ApplicationFrame) SwingUtilities.getWindowAncestor(TopBarMenuPanel.this)).backToMenu();
			}
		});

	}

}
