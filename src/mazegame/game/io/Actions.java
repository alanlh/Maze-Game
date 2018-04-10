package mazegame.game.io;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.AbstractAction;

import mazegame.game.logic.MazeGame;

/**
 * A collection of enums that describe various actions that can be taken
 * 
 * @author Alan Hu
 *
 */
public enum Actions {
	// Movement
	FORWARD, FORWARD_STOP,
	BACK, BACK_STOP,
	RIGHT, RIGHT_STOP,
	LEFT, LEFT_STOP,
	
	// CLICK BUTTON. Starts the items' effect and places it in hand
	// Does nothing if inHand is filled
	CONSUME_FOOD, APPLY_FIRST_AID, HOLD_STICK, LIGHT_CANDLE,
	// Left-CLICK on map
	PLACE_FOOD, PLACE_FIRST_AID, PLACE_STICK, PLACE_CANDLE,
	
	/*
	 * LEFT CLICK on map
	 */
	PICK_ITEM,
				
	/*
	 * RIGHT CLICK
	 * Only works for stick/candle
	 * Place stick at location, facing away from player
	 * Place candle at location
	 * Must click close to player position
	 */
	SPECIAL_COMMAND, // RIGHT CLICK
}
	