package mazegame.game.ui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import mazegame.game.io.GameStatus;
import mazegame.game.io.Actions;
import mazegame.game.logic.MazeGame;
import mazegame.game.logic.Character;


public class GamePanel extends JPanel {

	boolean initialized = false;
	
	MazeGame game;
	GameStatus gameStatus;
	
	IntroPanel introPanel;
	
	MapPanel mapPanel;
	CharacterPanel characterPanel;
	ItemsPanel itemsPanel;
			
	public GamePanel() {
		this.requestFocus();
		
		// This if else condition potentially allows users to not restart every time they exist. 
		if (!initialized) {
			introPanel = new IntroPanel(this);
			
			mapPanel = new MapPanel();
			characterPanel = new CharacterPanel();
			itemsPanel = new ItemsPanel();
			
			this.add(introPanel);
			introPanel.startIntro();
			
			initialized = true;
		} else {
			setUpGame();
		}
	}
		
	public void setGameStatus(GameStatus gameStatus, MazeGame game) {
		this.gameStatus = gameStatus;
		this.game = game;
		mapPanel.setGameStatus(gameStatus);
		characterPanel.setGameStatus(gameStatus);
		itemsPanel.setGameStatus(gameStatus);
	}
	
	// TODO: DO SOMETHING WITH THIS
	void setUpGame() {
		this.remove(introPanel);
		
		this.requestFocus();
		
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0.15;
		constraints.weighty = 0.8;
		this.add(itemsPanel, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0.85;
		constraints.weighty = 0.8;
		constraints.gridx = 1;
		constraints.gridy = 0;
		this.add(mapPanel, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 0.2;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		this.add(characterPanel, constraints);
		
		mapPanel.initialize();
		characterPanel.initialize();
		itemsPanel.initialize();
		
		game.initialize();
		
		addKeyBindings();

		repaint();
		revalidate();
	}
	
	
	/**
	 * Adds key bindings to used keys (WASD)
	 */
	private void addKeyBindings() {
		
		InputMap inputMap = this.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap actionMap = this.getActionMap();
						
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), Actions.FORWARD);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), Actions.FORWARD_STOP);
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), Actions.BACK);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), Actions.BACK_STOP);
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), Actions.RIGHT);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), Actions.RIGHT_STOP);
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), Actions.LEFT);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), Actions.LEFT_STOP);

		actionMap.put(Actions.FORWARD, new MovementAction(game, Actions.FORWARD, true));
		actionMap.put(Actions.FORWARD_STOP, new MovementAction(game, Actions.FORWARD, false));

		actionMap.put(Actions.BACK, new MovementAction(game, Actions.BACK, true));
		actionMap.put(Actions.BACK_STOP, new MovementAction(game, Actions.BACK, false));

		actionMap.put(Actions.LEFT, new MovementAction(game, Actions.LEFT, true));
		actionMap.put(Actions.LEFT_STOP, new MovementAction(game, Actions.LEFT, false));

		actionMap.put(Actions.RIGHT, new MovementAction(game, Actions.RIGHT, true));
		actionMap.put(Actions.RIGHT_STOP, new MovementAction(game, Actions.RIGHT, false));	
	}
	
	/**
	 * AbstractAction class that calls the setMovement method for the player character
	 * whenever a key is pressed. 
	 */
	class MovementAction extends AbstractAction {
		MazeGame game;
		Actions type;
		boolean pressed;
		
		public MovementAction(MazeGame game, Actions type, boolean pressed) {
			this.game = game;
			this.type = type;
			this.pressed = pressed;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (game.getCharacter() != null) {
				// Safety check
				game.getCharacter().setMovement(type, pressed);
			}
		}
	}

	/**
	 * Tells the panel to redraw itself with a given interpolation. 
	 * @param interpolation
	 */
	@Override
	public void repaint() {		
		super.repaint();
		
		if (gameStatus != null && gameStatus.getRunning()) {
			mapPanel.repaint();
			characterPanel.repaint();
			itemsPanel.repaint();
		}
	}
}
