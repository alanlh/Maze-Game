package mazegame.game.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import mazegame.game.io.AvailableActions;
import mazegame.game.io.GameStatus;
import mazegame.game.io.PlayerAction;
import mazegame.game.logic.Maze;
import mazegame.game.logic.MazeGame;

public class ButtonsPanel extends JPanel {
	
	private GameStatus gameStatus;

	private JButton candleButton = new JButton("No Spare Candle!"); // Torches can be seen in adjacent rooms,  but only last for 5 minutes.
	private JButton firstAidButton = new JButton ("No First Aid!"); // Used to heal self.
	private JButton foodButton = new JButton("No food!"); // Used to lessen hunger.
	private JButton stickButton = new JButton("No sticks!"); // Sticks are placed vertically away from the player. Can be used for pointing.
	
	// TODO: Remove?
	public ButtonsPanel() {

	}
	
	public void initialize()
	{		
		organizePanel();
		
		addButtonActionListeners();
		this.setVisible(true);
	}

	/**
	 * Organizes the game panel. Adds all the buttons, and subpanels.
	 */
	private void organizePanel() {
		this.revalidate();
		this.repaint();
				
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.weightx = 1;
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0.25;
		constraints.gridy = 0;
		this.add(candleButton, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0.25;
		constraints.gridy = 1;
		this.add(stickButton, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0.25;
		constraints.gridy = 2;
		this.add(foodButton, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0.25;
		constraints.gridy = 3;
		this.add(firstAidButton, constraints);
		
		this.revalidate();
		this.repaint();
	}

	/**
	 * Adds action listeners to each of the 4 buttons
	 */
	private void addButtonActionListeners() {
		candleButton.addActionListener(new ActionListener() {
			PlayerAction action = new PlayerAction(AvailableActions.LIGHT_CANDLE);
			
			@Override
			public void actionPerformed(ActionEvent click) {
				action.actionPerformed(click);
			}
		});
		
		stickButton.addActionListener(new ActionListener() {
			PlayerAction action = new PlayerAction(AvailableActions.HOLD_STICK);
			
			@Override
			public void actionPerformed(ActionEvent click) {
				action.actionPerformed(click);
			}
		});

		
		foodButton.addActionListener(new ActionListener() {
			PlayerAction action = new PlayerAction(AvailableActions.CONSUME_FOOD);
			
			@Override
			public void actionPerformed(ActionEvent click) {
				action.actionPerformed(click);
			}
		});
		
		firstAidButton.addActionListener(new ActionListener() {
			PlayerAction action = new PlayerAction(AvailableActions.APPLY_FIRST_AID);
			
			@Override
			public void actionPerformed(ActionEvent click) {
				action.actionPerformed(click);
			}
		});
	}

	void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}
	
	/**
	 * Updates the buttons if needed
	 */
	void draw() {
		
	}
}
