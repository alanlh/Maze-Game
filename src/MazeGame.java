// See this link:
// https://stackoverflow.com/questions/12021249/adding-jpanel-from-another-class-to-jpanel-in-jframe
// http://www.java-gaming.org/index.php?topic=25864.0

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


// A second class is created to avoid the clutter of fitting everything within one thing

public class MazeGame extends JPanel {

	private Timer delay;
	
	JLabel[] textDump;
	int[] delayTimesMilliSec;
	
	private int textDumpIndex = 0;

	// No need to include another JPanel, because this class is already a JPanel. 
	
	// includes:
	Maze map; // Maze maze: includes an array of rooms, along with testing to make sure there is a valid path.
	Character player; // Character person
	PlayerAction playerAction;
	
	// --- Items --- //
	
	JButton candleButton = new JButton("No Spare Candle!"); // Torches can be seen in adjacent rooms,  but only last for 5 minutes.
	JButton firstAidButton = new JButton ("No First Aid!"); // Used to heal self.
	JButton foodButton = new JButton("No food!"); // Used to lessen hunger.
	JButton stickButton = new JButton("No sticks!"); // Sticks are placed vertically away from the player. Can be used for pointing.
	
	// -- Where map and info are displayed -- //
	
	MainMap mainMap;
	JPanel characterInfoPanel;
	
	final double UPDATE_FREQUENCY = 1.0/24.0;
	final int MAX_UPDATES_PER_RENDER = 5;
	final int MAX_RENDERS_PER_UPDATE = 50;
	
	final double MAX_FPS = 1.0/120.0;
	final long SEC_TO_NANOSEC = 1000000000;
	
	public boolean running;
			
	/**
	 * Constructor
	 * Sets up the JPanel where the game itself is located. Starts entry dialogue.
	 * When finished, starts the game itself. 
	 */
	public MazeGame() {		
		// Initialize variables, but do not perform any actions.
					
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				
		setTexts();
		
		// -- sets layout of panel -- //
				
		introTextDump();
	}
	
	private void setTexts() {
		textDump = new JLabel[8];
		delayTimesMilliSec = new int[textDump.length];

		// the delaytimes[i] value corresponds to the i+1st textDump
		// This is because the delayTime takes effect for the next one.? 
		textDump[0] = new JLabel("");
		delayTimesMilliSec[0] = 4000;
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
		
		Font textFont = new Font("Sans Serif", Font.BOLD, 24);
		
		for (int i = 0; i < textDump.length; i ++) {
			// So that testing is faster
			delayTimesMilliSec[i] = 100;
			
			textDump[i].setFont(textFont);
			textDump[i].setAlignmentX(CENTER_ALIGNMENT);
		}
				
		this.add(textDump[0]);
	}
	
	/**
	 * Creates entry dialogue, displayed every certain amount of seconds. 
	 * Uses Timer to achieve goal
	 * TODO: Investigate whether or not the counter variable can be stored in the Timer class, rather than MazeGame class.
	 */
	public void introTextDump()
	{
		delay = new Timer(delayTimesMilliSec[textDumpIndex], new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				executeTextDump();
			}
		});
		
		delay.setInitialDelay(500);
		delay.start();
	}
	
	/**
	 * Called whenever the timer fires. Adds text to screen and changes timer value. 
	 * After all messages are displayed, the next Timer fire will remove everything from screen and start the game loop. 
	 */
	private void executeTextDump() {				
		this.textDumpIndex ++;
		
		if (textDumpIndex == textDump.length)
		{
			delay.stop();
			for(int i = 0; i < textDump.length; i ++) {
				this.remove(textDump[i]);
			}
			prepareMap();
			return;
		}
		
		delay.setDelay(delayTimesMilliSec[textDumpIndex]);
		
		this.add(Box.createRigidArea(new Dimension(0, 50)));
		
		this.add(textDump[textDumpIndex]);
		
		
		this.revalidate();
		this.repaint();
				
	}
	
	/**
	 * Starts the game. 
	 */
	public void prepareMap()
	{		
		organizePanel();
		
		this.requestFocus();
		
		addButtonActionListeners();
		addKeyBindings();
		addMouseListener();
		
		runGameLoop();
	}

	/**
	 * Organizes the game panel. Adds all the buttons, and subpanels.
	 */
	private void organizePanel() {
		
		this.revalidate();
		this.repaint();
		
		// Creates character, maze
		mainMap = new MainMap();
		characterInfoPanel = new JPanel();
		map = new Maze();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		
		this.setLayout(gridBagLayout);
		
		GridBagConstraints c = new GridBagConstraints();
				
		final double buttonWidthRatio = 0.15;
		final double buttonHeightRatio = 0.2;
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = buttonWidthRatio;
		c.weighty = buttonHeightRatio;
		c.gridx = 0;
		c.gridy = 0;
		this.add(candleButton, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = buttonWidthRatio;
		c.weighty = buttonHeightRatio;
		c.gridx = 0;
		c.gridy = 1;
		this.add(stickButton,c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = buttonWidthRatio;
		c.weighty = buttonHeightRatio;
		c.gridx = 0;
		c.gridy = 2;
		this.add(foodButton,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = buttonWidthRatio;
		c.weighty = buttonHeightRatio;
		c.gridx = 0;
		c.gridy = 3;
		this.add(firstAidButton,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1 - buttonWidthRatio;
		c.weighty = 4 * buttonHeightRatio;
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 4;
		this.add(mainMap, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1 - (4 * buttonHeightRatio);
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		this.add(characterInfoPanel, c);
		
//		mainMap.setSize(new Dimension(gridBagLayout.getLayoutDimensions()[0][1], gridBagLayout.getLayoutDimensions()[1][0]));
		mainMap.setVisible(true);

		this.revalidate();
		this.repaint();
	}

	/**
	 * Adds action listeners to each of the 4 buttons
	 */
	private void addButtonActionListeners() {
		candleButton.addActionListener(new ActionListener() {
			PlayerAction action = new PlayerAction(AvailableActions.LIGHT_CANDLE, MazeGame.this);
			
			@Override
			public void actionPerformed(ActionEvent click) {
				action.actionPerformed(click);
			}
		});
		
		stickButton.addActionListener(new ActionListener() {
			PlayerAction action = new PlayerAction(AvailableActions.HOLD_STICK, MazeGame.this);
			
			@Override
			public void actionPerformed(ActionEvent click) {
				action.actionPerformed(click);
			}
		});

		
		foodButton.addActionListener(new ActionListener() {
			PlayerAction action = new PlayerAction(AvailableActions.CONSUME_FOOD, MazeGame.this);
			
			@Override
			public void actionPerformed(ActionEvent click) {
				action.actionPerformed(click);
			}
		});
		
		firstAidButton.addActionListener(new ActionListener() {
			PlayerAction action = new PlayerAction(AvailableActions.APPLY_FIRST_AID, MazeGame.this);
			
			@Override
			public void actionPerformed(ActionEvent click) {
				action.actionPerformed(click);
			}
		});
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
		
		actionMap.put(AvailableActions.FORWARD, new PlayerAction(AvailableActions.FORWARD, this));
		actionMap.put(AvailableActions.FORWARD_STOP, new PlayerAction(AvailableActions.FORWARD_STOP, this));

		actionMap.put(AvailableActions.BACK, new PlayerAction(AvailableActions.BACK, this));
		actionMap.put(AvailableActions.BACK_STOP, new PlayerAction(AvailableActions.BACK_STOP, this));

		actionMap.put(AvailableActions.LEFT, new PlayerAction(AvailableActions.LEFT, this));
		actionMap.put(AvailableActions.LEFT_STOP, new PlayerAction(AvailableActions.LEFT_STOP, this));

		actionMap.put(AvailableActions.RIGHT, new PlayerAction(AvailableActions.RIGHT, this));
		actionMap.put(AvailableActions.RIGHT_STOP, new PlayerAction(AvailableActions.RIGHT_STOP, this));	
	}

	/**
	 * Adds a mouse listener for clicking. 
	 */
	private void addMouseListener() {
		mainMap.addMouseListener(new MouseAdapter() {
			PlayerAction leftClick = new PlayerAction(AvailableActions.PICK_ITEM, MazeGame.this);
			PlayerAction rightClick = new PlayerAction(AvailableActions.SPECIAL_COMMAND, MazeGame.this);
			
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
	 * Starts a new game loop
	 */
	public void runGameLoop() {		
		player = new Character(map.getStartingRoom(), this);
		mainMap.setCharacter(player);
		mainMap.setRoom(map.getStartingRoom());

		Thread loop = new Thread() {
			@Override
			public void run() {
				gameLoop();
			}
		};
		loop.start();
	}
	
	/**
	 * Bulk of game loop in a new thread
	 * Updates game per constant time, and renders as often as possible up to a max FPS
	 */
	private void gameLoop() {					
		long nextUpdateTime = System.nanoTime();
		long nextRenderTime = System.nanoTime();
		
		int loopsSinceRender = 0;
		int loopsSinceUpdate = 0;
		
		running = true;
						
		while (running) {
			
			long currentTime = System.nanoTime();
						
			double interpolation = (currentTime - nextUpdateTime + UPDATE_FREQUENCY * SEC_TO_NANOSEC) 
					/ (UPDATE_FREQUENCY * SEC_TO_NANOSEC);
						
			if (currentTime > nextRenderTime
					&& loopsSinceUpdate < MAX_RENDERS_PER_UPDATE) {
				loopsSinceUpdate ++;
				mainMap.setInterpolation(interpolation);
				mainMap.repaint();
				nextRenderTime = currentTime + (long)(MAX_FPS * SEC_TO_NANOSEC);
				loopsSinceRender = 0;
			}
			
			// Makes sure that it doesn't render too often, or too little
			// Second line allows frame rate to drop if rendering takes too much time
			// If updating takes too long, same as if rendering is too slow(?)
			while (currentTime > nextUpdateTime
					&& loopsSinceRender < MAX_UPDATES_PER_RENDER) {
				PlayerAction.executeList();
				loopsSinceRender ++;
				nextUpdateTime += UPDATE_FREQUENCY * SEC_TO_NANOSEC;
				player.updateCharacter();
				
				loopsSinceUpdate = 0;
			}
		}
	}
			
	private void checkUserInputs() {
		
	}
	
	public Character getCharacter() {
		return player;
	}
	
}
