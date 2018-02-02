package mazegame.game.ui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import mazegame.game.io.AvailableActions;
import mazegame.game.io.GameStatus;
import mazegame.game.io.PlayerAction;
import mazegame.game.logic.MazeGame;


public class GamePanel extends JPanel {

	boolean initialized = false;
	
	MazeGame game;
	GameStatus gameStatus;
	
	IntroPanel introPanel;
	
	MapPanel mapPanel;
	CharacterPanel characterPanel;
	ButtonsPanel buttonsPanel;
			
	public GamePanel() {
		this.requestFocus();
		
		// This if else condition potentially allows users to not restart every time they exist. 
		if (!initialized) {
			introPanel = new IntroPanel(this);
			
			mapPanel = new MapPanel();
			characterPanel = new CharacterPanel();
			buttonsPanel = new ButtonsPanel();
			
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
		buttonsPanel.setGameStatus(gameStatus);
	}
	
	// TODO: DO SOMETHING WITH THIS
	void setUpGame() {
		this.remove(introPanel);
		
		this.requestFocus();
		
		addKeyBindings();
		addMouseListener();

		this.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0.15;
		constraints.weighty = 0.8;
		this.add(buttonsPanel, constraints);
		
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
		buttonsPanel.initialize();
		
		repaint();
		revalidate();
		
		game.initialize();
	}
	
	
	/**
	 * Adds key bindings to used keys (WASD)
	 */
	private void addKeyBindings() {
		InputMap inputMap = this.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap actionMap = this.getActionMap();
						
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), AvailableActions.FORWARD);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), AvailableActions.FORWARD_STOP);
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), AvailableActions.BACK);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), AvailableActions.BACK_STOP);
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), AvailableActions.LEFT);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), AvailableActions.LEFT_STOP);
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), AvailableActions.RIGHT);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), AvailableActions.RIGHT_STOP);
		
		actionMap.put(AvailableActions.FORWARD, new PlayerAction(AvailableActions.FORWARD));
		actionMap.put(AvailableActions.FORWARD_STOP, new PlayerAction(AvailableActions.FORWARD_STOP));

		actionMap.put(AvailableActions.BACK, new PlayerAction(AvailableActions.BACK));
		actionMap.put(AvailableActions.BACK_STOP, new PlayerAction(AvailableActions.BACK_STOP));

		actionMap.put(AvailableActions.LEFT, new PlayerAction(AvailableActions.LEFT));
		actionMap.put(AvailableActions.LEFT_STOP, new PlayerAction(AvailableActions.LEFT_STOP));

		actionMap.put(AvailableActions.RIGHT, new PlayerAction(AvailableActions.RIGHT));
		actionMap.put(AvailableActions.RIGHT_STOP, new PlayerAction(AvailableActions.RIGHT_STOP));	
	}

	/**
	 * Adds a mouse listener for clicking. 
	 */
	private void addMouseListener() {
		this.addMouseListener(new MouseAdapter() {
			PlayerAction leftClick = new PlayerAction(AvailableActions.PICK_ITEM);
			PlayerAction rightClick = new PlayerAction(AvailableActions.SPECIAL_COMMAND);
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					leftClick.actionPerformed(e);
				} else if (SwingUtilities.isRightMouseButton(e)) {
					rightClick.actionPerformed(e);
				}
			}
		});
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
			buttonsPanel.repaint();
		}
	}
}
