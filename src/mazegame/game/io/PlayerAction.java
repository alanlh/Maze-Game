package mazegame.game.io;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Queue;

import javax.swing.AbstractAction;

import mazegame.game.logic.MazeGame;

public class PlayerAction extends AbstractAction {
		
	private final AvailableActions actionType;
	
	private MouseEvent event;
	
	public PlayerAction (AvailableActions action) {
		this.actionType = action;
	}

	public static volatile ArrayList<AvailableActions> actionHandleQueue = new ArrayList<>();
	
	public static void addAction(AvailableActions action) {
		System.out.println(action);
		actionHandleQueue.add(action);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		actionHandleQueue.add(actionType);
	}

	public void actionPerformed(MouseEvent e) {
		actionHandleQueue.add(actionType);
		this.event = e;
	}
}
