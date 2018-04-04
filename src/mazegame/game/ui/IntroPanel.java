package mazegame.game.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class IntroPanel extends JPanel {
	
	GamePanel parent;
	
	Font textFont = new Font("Sans Serif", Font.BOLD, 24);
	JLabel[] textDump = new JLabel[8];
	int[] delayTimesMilliSec = new int[8];

	private Timer delay;
	
	private int textDumpProgressionIndex = 0;
	
	public IntroPanel(GamePanel parent) {
		this.parent = parent;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		setLabelText();
		
		delay = new Timer(delayTimesMilliSec[textDumpProgressionIndex], new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				executeTextDump();
			}
		});
		delay.setInitialDelay(500);
	}
	
	/**
	 * Sets the text for the intro text, as well as the time delays. 
	 */
	void setLabelText() {
		textDump[0] = new JLabel("");
		delayTimesMilliSec[0] = 25; // TODO: CHANGE TO 4000
		textDump[1] = new JLabel("You wake up, and find yourself in a dark room...");
		delayTimesMilliSec[1] = 5000;
		textDump[2] = new JLabel("There's sunlight, streaming through a small hole,", JLabel.CENTER);
		delayTimesMilliSec[2] = 4000;
		textDump[3] = new JLabel("but it's too high up to reach.", JLabel.CENTER);
		delayTimesMilliSec[3] = 5000;
		textDump[4] = new JLabel("There are four dark doorways, one in each direction.", JLabel.CENTER);
		delayTimesMilliSec[4] = 6000;
		textDump[5] = new JLabel("You know you must go to through one of those doorways", JLabel.CENTER);
		delayTimesMilliSec[5] = 8000;
		textDump[6] = new JLabel(" to reach the outside world.");
		delayTimesMilliSec[6] = 3000;
		textDump[7] = new JLabel("But how far must you go beyond that?", JLabel.CENTER);
		delayTimesMilliSec[7] = 8000;
	}
	
	void startIntro() {
		for (int i = 0; i < textDump.length; i ++) {
			// So that testing is faster
			// TODO: DELETE WHEN DONE
			delayTimesMilliSec[i] = 5;
			
			textDump[i].setFont(textFont);
			textDump[i].setAlignmentX(CENTER_ALIGNMENT);
		}
				
		this.add(textDump[0]);
		delay.start();
	}
			
	/**
	 * Called whenever the timer fires. Adds text to screen and changes timer value. 
	 * After all messages are displayed, the next Timer fire will remove everything from screen and start the game loop. 
	 */
	private void executeTextDump() {				
		this.textDumpProgressionIndex++;
		
		if (textDumpProgressionIndex == textDump.length) {
			delay.stop();
			parent.setUpGame();
			return;
		}
		
		delay.setDelay(delayTimesMilliSec[textDumpProgressionIndex]);
		
		this.add(Box.createRigidArea(new Dimension(0, 50)));
		
		this.add(textDump[textDumpProgressionIndex]);
		
		this.revalidate();
		this.repaint();
	}
}
