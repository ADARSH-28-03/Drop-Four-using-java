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
                int x = col * CELL_WIDTH;
                int y = row * CELL_WIDTH;
                g2.drawRect(x, y, CELL_WIDTH, CELL_WIDTH);
                if (grid[row][col] != null) {
                    g2.setColor(grid[row][col]);
                    g2.fillOval(x, y, CELL_WIDTH, CELL_WIDTH);
                }
            }
        }

        // Check for winner
        if (checkForWinner(Color.RED)) {
            JOptionPane.showMessageDialog(this, "Red wins!");
            newGame();
            return;
        } else if (checkForWinner(Color.YELLOW)) {
            JOptionPane.showMessageDialog(this, "Yellow wins!");
            newGame();
            return;
        }

        // Display current player's turn
        g2.setColor(Color.BLACK);
        g2.drawString("Current player: " + (currentPlayer == 1 ? "Red" : "Yellow"), 0, getHeight() - 5);
    }


    /**
     * Handles mouse clicks on the game board.
     */
    private class MouseHandler extends MouseAdapter {
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

            if (checkForWinner(Color.RED)) {
                JOptionPane.showMessageDialog(this, "Red wins!");
                newGame();
                return;
            } else if (checkForWinner(Color.YELLOW)) {
                JOptionPane.showMessageDialog(this, "Yellow wins!");
                newGame();
                return;
            } else if (isGridFull()) {
                JOptionPane.showMessageDialog(this, "The game is a draw!");
                newGame();
                return;
            }

            repaint();
        }

        private boolean isGridFull() {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (grid[row][col] == Color.WHITE) {
                        return false; // there is still an empty cell
                    }
                }
            }
            return true; // the grid is full, no more moves can be made
        }
        /**
         * Checks if the game has been won by a player.
         * Returns the color of the winning player, or null if the game is not over yet.
         */
        private Color checkForWinner() {
            // Check horizontal
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS - 3; col++) {
                    Color color = grid[row][col];
                    if (color != Color.WHITE && color == grid[row][col + 1] && color == grid[row][col + 2] && color == grid[row][col + 3]) {
                        return color;
                    }
                }
            }

            // Check vertical
            for (int row = 0; row < ROWS - 3; row++) {
                for (int col = 0; col < COLS; col++) {
                    Color color = grid[row][col];
                    if (color != Color.WHITE && color == grid[row + 1][col] && color == grid[row + 2][col] && color == grid[row + 3][col]) {
                        return color;
                    }
                }
            }

            // Check diagonal (top-left to bottom-right)
            for (int row = 0; row < ROWS - 3; row++) {
                for (int col = 0; col < COLS - 3; col++) {
                    Color color = grid[row][col];
                    if (color != Color.WHITE && color == grid[row + 1][col + 1] && color == grid[row + 2][col + 2] && color == grid[row + 3][col + 3]) {
                        return color;
                    }
                }
            }

            // Check diagonal (bottom-left to top-right)
            for (int row = ROWS - 1; row >= 3; row--) {
                for (int col = 0; col < COLS - 3; col++) {
                    Color color = grid[row][col];
                    if (color != Color.WHITE && color == grid[row - 1][col + 1] && color == grid[row - 2][col + 2] && color == grid[row - 3][col + 3]) {
                        return color;
                    }
                }
            }

            // No winner yet
            return null;
        }

        /**
         * Displays a dialog box showing the winner of the game.
         */
        private void showWinnerDialog(Color winner) {
            if (winner == Color.RED) {
                JOptionPane.showMessageDialog(this, "Red player wins!");
            } else if (winner == Color.YELLOW) {
                JOptionPane.showMessageDialog(this, "Yellow player wins!");
            } else {
                JOptionPane.showMessageDialog(this, "Tie game!");
            }
        }

        /**
         * Starts a new game of Connect4.
         */
        public void newGame() {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    grid[row][col] = Color.WHITE;
                }
            }
            currentPlayer = 1;
            repaint();
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
                if (isWinningMove(row, col, currentPlayer)) {
                    JOptionPane.showMessageDialog(Connect4Panel.this, "Player " + currentPlayer + " wins!");
                    newGame();
                } else if (isGridFull()) {
                    JOptionPane.showMessageDialog(Connect4Panel.this, "Tie game!");
                    newGame();
                } else {
                    currentPlayer = 3 - currentPlayer; // switch player (1 -> 2, 2 -> 1)
                    repaint();
                }
            }

            /**

             Finds the lowest empty row in the specified column.
             Returns -1 if the column is already full.
             */
            private int getLowestEmptyRow(int col) {
                for (int row = ROWS - 1; row >= 0; row--) {
                    if (grid[row][col] == 0) {
                        return row;
                    }
                }
                return -1; // column is full
            }
            /**

             Checks if the specified move (row, col) for the specified player is a winning move.
             */
            private boolean isWinningMove(int row, int col, int player) {
// Check row
                int count = 0;
                for (int c = 0; c < COLS; c++) {
                    if (grid[row][c] == player) {
                        count++;
                        if (count == 4) {
                            return true;
                        }
                    } else {
                        count = 0;
                    }
                }

// Check column
                count = 0;
                for (int r = 0; r < ROWS; r++) {
                    if (grid[r][col] == player) {
                        count++;
                        if (count == 4) {
                            return true;
                        }
                    } else {
                        count = 0;
                    }
                }

// Check diagonal
                count = 0;
                int r = row, c = col;
                while (r > 0 && c > 0) {
                    r--;
                    c--;
                }
                while (r < ROWS && c < COLS) {
                    if (grid[r][c] == player) {
                        count++;
                        if (count == 4) {
                            return true;
                        }
                    } else {
                        count = 0;
                    }
                    r++;
                    c++;
                }

// Check anti-diagonal
                count = 0;
                r = row;
                c = col;
                while (r > 0 && c < COLS - 1) {
                    r--;
                    c++;
                }
                while (r < ROWS && c >= 0) {
                    if (grid[r][c] == player) {
                        count++;
                        if (count == 4) {
                            return true;
                        }
                    } else {
                        count = 0;
                    }
                    r++;
                    c--;
                }

                return false;
            }

            /**

             Checks if the grid is full.
             */
            private boolean isGridFull() {
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLS; col++) {
                        if (grid[row][col] == 0) {
                            return false; // found an empty cell
                        }
                    }
                }
                return true; // grid is full
            }

            /**

             Checks if there is a winner.
             */
            private boolean hasWinner(int player) {
// Check horizontal
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLS - 3; col++) {
                        if (grid[row][col] == player && grid[row][col+1] == player && grid[row][col+2] == player && grid[row][col+3] == player) {
                            return true;
                        }
                    }
                }
// Check vertical
                for (int row = 0; row < ROWS - 3; row++) {
                    for (int col = 0; col < COLS; col++) {
                        if (grid[row][col] == player && grid[row+1][col] == player && grid[row+2][col] == player && grid[row+3][col] == player) {
                            return true;
                        }
                    }
                }

// Check diagonal (down-right)
                for (int row = 0; row < ROWS - 3; row++) {
                    for (int col = 0; col < COLS - 3; col++) {
                        if (grid[row][col] == player && grid[row+1][col+1] == player && grid[row+2][col+2] == player && grid[row+3][col+3] == player) {
                            return true;
                        }
                    }
                }

// Check diagonal (up-right)
                for (int row = 3; row < ROWS; row++) {
                    for (int col = 0; col < COLS - 3; col++) {
                        if (grid[row][col] == player && grid[row-1][col+1] == player && grid[row-2][col+2] == player && grid[row-3][col+3] == player) {
                            return true;
                        }
                    }
                }

                return false; // no winner found
            }

            /**

             Handles mouse clicks on the game board.
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
                    if (hasWinner(currentPlayer)) {
                        JOptionPane.showMessageDialog(Connect4Panel.this, "Player " + currentPlayer + " wins!");
                        newGame();
                    } else if (isGridFull()) {
                        JOptionPane.showMessageDialog(Connect4Panel.this, "The game ends in a draw.");
                        newGame();
                    } else {
                        currentPlayer = 3 - currentPlayer; // switch player (1 -> 2, 2 -> 1)
                        repaint();
                    }
                }
            }






