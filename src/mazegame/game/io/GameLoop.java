package mazegame.game.io;

import mazegame.game.logic.MazeGame;
import mazegame.game.ui.GamePanel;

public class GameLoop extends Thread {
		
	MazeGame game;
	GamePanel panel;
	
	GameStatus status;
	
	static final double UPDATE_FREQUENCY = 1.0/48.0;
	static final int MAX_UPDATES_PER_RENDER = 5;
	static final int MAX_RENDERS_PER_UPDATE = 50;
	
	static final double MAX_FPS = 1.0/120.0;
	static final long SEC_TO_NANOSEC = 1000000000;
	
	public GameLoop(GamePanel panel, GameStatus status) {
		this.game = status.getGameReference();
		this.panel = panel;
		
		this.status = status;
	}
	
	/**
	 * Bulk of game loop in a new thread
	 * Updates game per constant time assuming rendering is done fast enough, and renders as often as possible up to a max FPS
	 */
	public void run() {					
		long nextUpdateTime = System.nanoTime();
		long nextRenderTime = System.nanoTime();
		
		int loopsSinceRender = 0;
		int loopsSinceUpdate = 0;
										
		while (status.getRunning() || !status.getWin()) {
			
			long currentTime = System.nanoTime();
						
			double interpolation = (currentTime - nextUpdateTime + UPDATE_FREQUENCY * SEC_TO_NANOSEC) 
					/ (UPDATE_FREQUENCY * SEC_TO_NANOSEC);
			status.setInterpolation(interpolation);
						
			if (currentTime > nextRenderTime
					&& loopsSinceUpdate < MAX_RENDERS_PER_UPDATE) {
				loopsSinceUpdate ++;
				
				// TODO:
				panel.repaint();
				nextRenderTime = currentTime + (long)(MAX_FPS * SEC_TO_NANOSEC);
				loopsSinceRender = 0;
			}
			
			// Makes sure that it doesn't render too often, or too little
			// Second line allows frame rate to drop if rendering takes too much time
			// If updating takes too long, same as if rendering is too slow(?)
			while (currentTime > nextUpdateTime
					&& loopsSinceRender < MAX_UPDATES_PER_RENDER) {
				loopsSinceRender ++;
				nextUpdateTime += UPDATE_FREQUENCY * SEC_TO_NANOSEC;
				game.update();
				
				loopsSinceUpdate = 0;
			}
		}
	}

	
	public static double getUpdateFrequency() {
		return UPDATE_FREQUENCY;
	}
	
}
