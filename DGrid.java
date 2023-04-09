import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Connect4 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Connect4");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(new Connect4Panel());
        frame.pack();
        frame.setVisible(true);
    }
}

class Connect4Panel extends JPanel {
    private static final int CELL_WIDTH = 40;
    private static final int ROWS = 6;
    private static final int COLS = 7;

    private Color[][] grid = new Color[ROWS][COLS];
    private int currentPlayer = 1;

    public Connect4Panel() {
        setPreferredSize(new Dimension(COLS * CELL_WIDTH, ROWS * CELL_WIDTH));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int col = x / CELL_WIDTH;
                int row = getRowForColumn(col);

                if (row == -1) {
                    System.out.println("Invalid move");
                    return;
                }

                grid[row][col] = currentPlayer == 1 ? Color.RED : Color.YELLOW;
                currentPlayer = 3 - currentPlayer; // alternate between player 1 and 2
                repaint();
            }
        });

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = Color.WHITE;
            }
        }
    }

    private int getRowForColumn(int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (grid[row][col] == Color.WHITE) {
                return row;
            }
        }
        return -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw the grid
        g2.setColor(Color.BLUE);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = col * CELL_SIZE + BORDER_SIZE;
                int y = row * CELL_SIZE + BORDER_SIZE;
                g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                if (grid[row][col] == 1) {
                    g2.setColor(Color.RED);
                    g2.fillOval(x, y, CELL_SIZE, CELL_SIZE);
                }
                else if (grid[row][col] == 2) {
                    g2.setColor(Color.YELLOW);
                    g2.fillOval(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        // Display current player's turn
        g2.setColor(Color.BLACK);
        g2.drawString("Current player: " + (currentPlayer == 1 ? "Red" : "Yellow"), BORDER_SIZE, getHeight() - BORDER_SIZE);

    }

    /**
     * Handles mouse clicks on the game board.
     */
    private class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int col = (e.getX() - BORDER_SIZE) / CELL_SIZE;
            if (col < 0 || col >= COLS) {
                return; // clicked outside of game board
            }
            int row = getLowestEmptyRow(col);
            if (row < 0) {
                return; // column is full
            }
            grid[row][col] = currentPlayer;
            currentPlayer = 3 - currentPlayer; // switch player (1 -> 2, 2 -> 1)
            repaint();
        }

        /**
         * Finds the lowest empty row in the specified column.
         * Returns -1 if the column is already full.
         */
        private int getLowestEmptyRow(int col) {
            for (int row = ROWS - 1; row >= 0; row--) {
                if (grid[row][col] == 0) {
                    return row;
                }
            }
            return -1; // column is full
        }
    }

    /**
     * Starts a new game of Connect Four.
     */
    public void newGame() {
        grid = new int[ROWS][COLS];
        currentPlayer = 1;
        repaint();
    }

    /**
     * Launches a new Connect Four game window.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Connect Four");
        ConnectFour game = new ConnectFour();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        game.newGame();
    }
}
