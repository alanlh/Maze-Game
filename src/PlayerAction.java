import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Queue;

import javax.swing.AbstractAction;

public class PlayerAction extends AbstractAction {
	
	private static final int MAX_ACTIONS_PER_UPDATE = 10;
	// Ideally should be executable within the update time
	// Probably not needed in this game.
	
	private final AvailableActions action;
	
	private MouseEvent event;
	private static MazeGame game;
	
	public PlayerAction (AvailableActions action, MazeGame game) {
		this.action = action;
		PlayerAction.game = game;
	}

	private static ArrayList<AvailableActions> actionHandleQueue = new ArrayList<>();
	
	public static void addAction(AvailableActions action) {
		System.out.println(action);
		actionHandleQueue.add(action);
	}

	/**
	 * There just has to be a cleaner structure for doing this.
	 */
	public static void executeList() {
		int actionCount = 0;
		while (actionCount < MAX_ACTIONS_PER_UPDATE && !actionHandleQueue.isEmpty()) {
			AvailableActions action = actionHandleQueue.remove(0);
			switch (action) {
			case FORWARD:
				game.getCharacter().setMovement(AvailableActions.FORWARD, true);
				break;
			case FORWARD_STOP:
				game.getCharacter().setMovement(AvailableActions.FORWARD, false);
				break;
			case BACK:
				game.getCharacter().setMovement(AvailableActions.BACK, true);
				break;
			case BACK_STOP:
				game.getCharacter().setMovement(AvailableActions.BACK, false);
				break;
			case RIGHT:
				game.getCharacter().setMovement(AvailableActions.RIGHT, true);
				break;
			case RIGHT_STOP:
				game.getCharacter().setMovement(AvailableActions.RIGHT, false);
				break;
			case LEFT:
				game.getCharacter().setMovement(AvailableActions.LEFT, true);
				break;
			case LEFT_STOP:
				game.getCharacter().setMovement(AvailableActions.LEFT, false);
				break;
			case CONSUME_FOOD:
				game.getCharacter().setHealth(AvailableActions.CONSUME_FOOD);
				break;
			case APPLY_FIRST_AID:
				game.getCharacter().setHealth(AvailableActions.APPLY_FIRST_AID);
				break;
			case HOLD_STICK:
				break;
			case LIGHT_CANDLE:
				break;
			case SPECIAL_COMMAND:
				break;
			case PICK_ITEM:
				break;
			}
			actionCount ++;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		actionHandleQueue.add(action);
	}

	public void actionPerformed(MouseEvent e) {
		actionHandleQueue.add(action);
		this.event = e;
	}

}
