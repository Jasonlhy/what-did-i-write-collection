import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.*;

class GameGUI extends JFrame {

	JPanel topPanel, bottomPanel, leftPanel, numpadPanel, buttonPanel;
	DrawPanel middle;
	JLabel score, nextThree, message;
	JButton[] numpad = new JButton[9];
	JButton undo, load, save, free, test;
	Game g;
	boolean random = true;
	boolean gameOver = false;
	NumpadListener np = new NumpadListener();

	JFileChooser fc = new JFileChooser(".");

	public GameGUI(Game game) {
		super("Game GUI");
		this.g = game;
		g.setLimit(9);

		Font big = new Font("SansSerif", Font.BOLD, 24);

		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		score = new JLabel();
		score.setFont(big);
		score.setText(g.getScore() + "");
		nextThree = new JLabel();
		nextThree.setFont(big);
		nextThree.setText(g.getNextThree().toString());
		nextThree.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		nextThree.setBackground(Color.YELLOW);
		nextThree.setOpaque(true);
		topPanel.add(new JLabel("SCORE:"));
		topPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		topPanel.add(score);
		topPanel.add(Box.createHorizontalGlue());
		topPanel.add(new JLabel("Next 3 Numbers:"));
		topPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		topPanel.add(nextThree);
		add(topPanel, BorderLayout.NORTH);

		middle = new DrawPanel(game);
		add(middle, BorderLayout.CENTER);

		bottomPanel = new JPanel(new BorderLayout(5, 5));
		leftPanel = new JPanel(new BorderLayout(5, 5));
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1, 5, 5));
		numpadPanel = new JPanel(new GridLayout(3, 3, 5, 5));

		for (int i = 0; i < numpad.length; i++) {
			numpad[i] = new JButton((i + 1) + "");
			numpad[i].addActionListener(np);
			numpad[i].setMnemonic(KeyEvent.VK_1 + i);
			numpadPanel.add(numpad[i]);

			KeyStroke k1 = KeyStroke.getKeyStroke(KeyEvent.VK_1 + i, 0);
			KeyStroke k2 = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1 + i, 0);
			// should use KeyMap and ActionMap instead of registerKeyboardAction
			// I'll modifiy it if I've got time
			bottomPanel.registerKeyboardAction(np, (i + 1) + "", k1,
					JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
			bottomPanel.registerKeyboardAction(np, (i + 1) + "", k2,
					JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		}

		KeyStroke k1 = KeyStroke.getKeyStroke(KeyEvent.VK_0, 0);
		KeyStroke k2 = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0, 0);
		// should use KeyMap and ActionMap instead of registerKeyboardAction
		// I'll modifiy it if I've got time
		bottomPanel.registerKeyboardAction(np, "0", k1,
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		bottomPanel.registerKeyboardAction(np, "0", k2,
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		undo = new JButton("Undo");
		undo.setEnabled(false);
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				g.undo();
				checking();
				repaintInfo();
			}
		});

		load = new JButton("Load Game Data");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc.resetChoosableFileFilters();
				fc.setFileFilter(new FileNameExtensionFilter(
						"Number Matching Game File (*.sav)", "sav"));
				// fc.setAcceptAllFileFilterUsed(true);
				int response = fc.showOpenDialog(GameGUI.this);
				File file = fc.getSelectedFile();
				if (file != null && response == JFileChooser.APPROVE_OPTION) {
					try {
						ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
						g=(Game)input.readObject();
						middle.setGame(g);
						System.out.println("load the game !");
						System.out.println(g);
						checking();
						repaintInfo();
					
					}catch(Exception ex){
						ex.getStackTrace();
						
					}
					
				}
				repaintInfo();
			}
		});

		save = new JButton("Save Game Data");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc.resetChoosableFileFilters();
				fc.setFileFilter(new FileNameExtensionFilter(
						"Number Matching Game File (*.sav)", "sav"));
				// fc.setAcceptAllFileFilterUsed(true);
				int response = fc.showSaveDialog(GameGUI.this);
				File file = fc.getSelectedFile();
				if (file != null && response == JFileChooser.APPROVE_OPTION) {
					try{
						ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
						output.writeObject(g);
						System.out.println("Save the game");
					}catch(Exception ex){
						System.out.println(ex.getMessage());
					}
				}
			}
		});

		free = new JButton("Start Free Mode");
		free.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				g = new Game();
				random = true;
				middle.setGame(g);
				undo.setEnabled(false);
				repaintInfo();
			}
		});

		test = new JButton("Start Test Mode");
		test.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc.resetChoosableFileFilters();
				fc.setFileFilter(new FileNameExtensionFilter(
						"Plain Text File (*.txt)", "txt"));
				// fc.setAcceptAllFileFilterUsed(true);
				int response = fc.showOpenDialog(GameGUI.this);
				File file = fc.getSelectedFile();
				if (file != null && response == JFileChooser.APPROVE_OPTION) {
					LinkedList l = readQueue(file);
					g = new Game(l);
				}
				random = false;
				middle.setGame(g);
				undo.setEnabled(false);
				repaintInfo();
			}
		});

		buttonPanel.add(free);
		buttonPanel.add(test);
		buttonPanel.add(load);
		buttonPanel.add(save);

		message = new JLabel(" ");
		message.setFont(big);
		message.setAlignmentX(0.5f);
		message.setForeground(Color.RED);

		leftPanel.add(numpadPanel, BorderLayout.CENTER);
		leftPanel.add(undo, BorderLayout.SOUTH);
		bottomPanel.add(leftPanel, BorderLayout.CENTER);
		bottomPanel.add(buttonPanel, BorderLayout.EAST);
		bottomPanel.add(message, BorderLayout.SOUTH);
		add(bottomPanel, BorderLayout.SOUTH);

		setSize(400, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private class NumpadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int pos = Integer.parseInt(e.getActionCommand()) - 1;
			if (pos == -1) {
				g.undo();
				checking();
				repaintInfo();
				return;
			}
			if (g.stackHeight(pos) >= g.getLimit())
				return;
			LinkedList result = g.putNext(pos);
			if (!result.isEmpty()) {
				String msg = "";
				if (result.count() > 1)
					msg += result.count() + " COMBOS!! ";
				while (!result.isEmpty())
					msg += " +" + result.removeFromHead();
				message.setText(msg);
			} else
				message.setText(" ");
			if (random)
				g.addToQueue();
			gameOver = g.gameOver();
			checking();
			repaintInfo();
		}
	}

	private void repaintInfo() {
		score.setText(g.getScore() + "");
		nextThree.setText(g.getNextThree().toString());
		middle.repaint();
		if (gameOver) {
			message.setText("Game Over!!");
			undo.setEnabled(false);
		}
	}

	private void checking() { // enable / disable buttons
		boolean flag = true;
		for (int i = 0; i < 9; i++) {
			numpad[i].setEnabled(g.stackHeight(i) < g.getLimit());
			if (g.stackHeight(i) > 0)
				flag = false;
		}
		undo.setEnabled(!(flag && g.getScore() == 0)); // check if game started?
		save.setEnabled(!gameOver);
	}

	private static LinkedList readQueue(File f) {
		LinkedList l = new LinkedList();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s = br.readLine();
			String[] array = s.split(" ");
			for (String item : array)
				l.addToTail(new Integer(item));
		} catch (IOException ioe) {
			System.err.println("I/O Exception in reading the input queue file");
			ioe.printStackTrace();
		}
		return l;
	}

	public static void main(String args[]) {
		GameGUI gui = new GameGUI(args.length == 0 ? new Game() : new Game(
				readQueue(new File(args[0]))));
		if (args.length != 0)
			gui.random = false;
		gui.setVisible(true);
	}
}

class DrawPanel extends JPanel {
	Game game;
	private static final Color[] color = { new Color(255, 255, 128),
			new Color(255, 240, 128), new Color(255, 224, 128),
			new Color(255, 208, 128), new Color(255, 192, 128),
			new Color(255, 176, 128), new Color(255, 160, 128),
			new Color(255, 144, 128), new Color(255, 128, 128) };

	public DrawPanel(Game g) {
		game = g;
		setFont(new Font("SansSerif", Font.BOLD, 32));
	}

	public void setGame(Game g) {
		game = g;
	}

	public void paint(Graphics g) {
		super.paint(g);
		double sx = getWidth() / 200.0;
		double sy = getHeight() / 200.0;
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(sx, sy);
		g2.drawLine(4, 68, 196, 68);
		g2.drawLine(4, 132, 196, 132);
		g2.drawLine(68, 4, 68, 196);
		g2.drawLine(132, 4, 132, 196);

		for (int i = 0; i < 9; i++) {
			int c = 0;
			int startX = 6 + i % 3 * 64;
			int startY = 6 + i / 3 * 64;
			for (int j = game.stackHeight(i) - 1; j >= 0; j--) {
				g2.setColor(color[c++]);
				g2.fillRect(startX + j * 2, startY + j * 2, 42, 42);
				g2.setColor(Color.BLACK);
				g2.drawRect(startX + j * 2, startY + j * 2, 42, 42);
			}
			if (game.stackHeight(i) > 0)
				g2.drawString(game.top(i) + "", startX + 12, startY + 32);
		}
	}
}
