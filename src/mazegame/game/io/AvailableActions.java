package mazegame.game.io;
	public enum AvailableActions {

		// Movement
		FORWARD, FORWARD_STOP,
		BACK, BACK_STOP,
		RIGHT, RIGHT_STOP,
		LEFT, LEFT_STOP,
		
		// CLICK BUTTON
		CONSUME_FOOD, APPLY_FIRST_AID, HOLD_STICK, LIGHT_CANDLE, // HoldLight candle and hold in hand
		// RIGHT-CLICK BUTTON
		PLACE_FOOD, PLACE_FIRST_AID, PLACE_STICK, PLACE_CANDLE,
		
		/*
		 * LEFT CLICK
		 * Add to inventory
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
