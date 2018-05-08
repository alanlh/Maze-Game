package mazegame.game.logic.items;

import java.awt.Graphics;

import mazegame.game.logic.Point;
import mazegame.game.logic.Room;

public class Food extends Items {

	public enum FoodName {
		BREAD,
		WATER,
		TOMATO,
	}
	// Use Enum class initialization to set up hunger value, etc. 
	public Food(FoodName foodtype, Room room, Point position) {
		super(room, position);
	}
	
	double hungerValue;
		
	public Type getType() {
		return Items.Type.FOOD;
	}
	
	public void callSpecialEffects() {
		
	}
	
	@Override
	public void onUse() {
	
	}

	@Override
	public void draw(Graphics g, double itemTopLeftX, double itemTopLeftY) {
		g.drawRect((int) Math.floor(itemTopLeftX), (int) Math.floor(itemTopLeftY), (int) (2 * radius), (int) (2 * radius));		
	}

}
