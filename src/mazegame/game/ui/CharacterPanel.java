package mazegame.game.ui;

import java.awt.Color;

import javax.swing.JPanel;

import mazegame.game.io.GameStatus;

public class CharacterPanel extends JPanel {

	GameStatus gameStatus;
	
	public CharacterPanel() {
		
	}
	
	void initialize() {
		this.setBackground(Color.BLUE);
		this.setVisible(true);

	}
	
	void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}
	
}
