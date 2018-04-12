package mazegame.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mazegame.game.io.GameStatus;
import mazegame.game.io.Actions;
import mazegame.game.logic.Maze;
import mazegame.game.logic.MazeGame;

public class ItemsPanel extends JPanel {
		
	private GameStatus gameStatus;

	private JScrollPane inventoryPanel = new JScrollPane(this);
	
	private LinkedList<JLabel> inventoryIcon = new LinkedList<JLabel>();

	GridBagConstraints constraints = new GridBagConstraints();

		
	public ItemsPanel() {
		this.setAutoscrolls(true);
		// this.add(inventoryPanel);
		
		// inventoryPanel.setWheelScrollingEnabled(true);
	}
	
	public void initialize() {		
		organizePanel();
		
		this.setVisible(true);
	}

	/**
	 * Organizes the initial items panel. Adds all the buttons, and subpanels.
	 */
	private void organizePanel() {
		this.revalidate();
		this.repaint();
				
//		inventoryPanel.setLayout(new GridBagLayout());
//		
//		// In case there are any existing items for whatever reason		
//		constraints.fill = GridBagConstraints.BOTH;
//		constraints.weightx = 0.5;
//		constraints.weighty = 0.1;
//		for (int i = 0; i < inventoryIcon.size(); i += 2) {
//			constraints.gridx = 0;
//			constraints.gridy = i / 2;
//			inventoryPanel.add(inventoryIcon.get(i), constraints);
//			addButtonActionListeners(inventoryIcon.get(i), i);
//			
//			constraints.gridx = 1;
//			inventoryPanel.add(inventoryIcon.get(i + 1), constraints);
//			addButtonActionListeners(inventoryIcon.get(i + 1), i + 1);
//		}
				
		this.revalidate();
		this.repaint();
	}

	/**
	 * Adds action listeners to each of the buttons
	 */
	private void addButtonActionListeners(JLabel inventoryItem, int index) {
		inventoryItem.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				gameStatus.alertItemClicked(index);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				//Do nothing
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// Do nothing
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// Do nothing
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// Do nothing
			}
		});
	}
	
	private void removeButtonActionListener(JLabel inventoryItem, int index) {
		// inventoryItem.removeMouseListener();
	}

	void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	@Override
	public void paintComponent(Graphics g) {
		if (gameStatus.getItemsChanged()) {
			
		}
	}
	
}
