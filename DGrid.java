import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class DrawGrid {
    private JFrame frame;

    public DrawGrid() {
        frame = new JFrame("DrawGrid");
        frame.setSize(310, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(frame.getSize());
        frame.add(new MultiDraw(frame.getSize()));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] argv) {
        new DrawGrid();
    }

    public static class MultiDraw extends JPanel  implements MouseListener {
        int startX = 10;
        int startY = 10;
        int cellWidth = 40; //Coin
        int turn = 2; //players
        int rows = 6;//rows
        int cols = 7;//columns

        Color[][] grid = new Color[rows][cols];

        public MultiDraw(Dimension dimension) {
            setSize(dimension);
            setPreferredSize(dimension);
            addMouseListener(this);
            //1. initialize array here
            int x = 0;
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    grid[row][col]= new Color(255,255,255);
                }
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            Dimension d = getSize();
            g2.setColor(new Color(0, 200, 200));
            g2.fillRect(0,0,d.width,d.height);
            g2.setColor(new Color(0, 0, 200));
            // g2.fillRect(60,60,d.width,d.height);
            startX = 0;
            startY = 0;

            //2) draw grid here
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    g2.setColor(grid[row][col]);
                    g2.fillOval(startX, startY, cellWidth, cellWidth);
                    startX+=cellWidth;
                }
                startX=0;
                startY+=cellWidth;


            }

            g2.setColor(new Color(255, 255, 255));
            if(turn%2==0){
                g2.drawString("Red's Turn", 20, 350);
            }else{
                g2.drawString("Yellow's Turn",20,350);
            }
        }

        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();
            int Xspot= x/cellWidth;
            int Yspot = y/cellWidth;
            Yspot= Test(Xspot);
            if(Yspot<0){
                System.out.println("Not a Valid Entry");
            }
            else{
                if (turn%2==0){
                    grid[Yspot][Xspot]= new Color(255,0,0);
                }
                else{
                    grid[Yspot][Xspot]= new Color(255,255,0);
                }
                turn++;
                if (checkWinner()) {
                    if (turn % 2 == 0) {
                        JOptionPane.showMessageDialog(this, "Red Wins!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Yellow Wins!");
                    }
                    System.exit(0);
                }

            }
            repaint();
        }
        public int Test(int Xspot){
            int yspot = rows-1;
            while(!(grid[yspot][Xspot].equals(new Color(255,255,255))|| yspot<0)){
                yspot--;
            }
            return yspot;
        }

        public boolean checkWinner() {
            // check horizontal
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length - 3; col++) {
                    if (grid[row][col].equals(grid[row][col + 1]) &&
                            grid[row][col].equals(grid[row][col + 2]) &&
                            grid[row][col].equals(grid[row][col + 3]) &&
                            !grid[row][col].equals(new Color(255, 255, 255))) {
                        return true;
                    }
                }
            }

            // check vertical
            for (int col = 0; col < grid[0].length; col++) {
                for (int row = 0; row < grid.length - 3; row++) {
                    if (grid[row][col].equals(grid[row + 1][col]) &&
                            grid[row][col].equals(grid[row + 2][col]) &&
                            grid[row][col].equals(grid[row + 3][col]) &&
                            !grid[row][col].equals(new Color(255, 255, 255))) {
                        return true;
                    }
                }
            }

            // check diagonal (top-left to bottom-right)
            for (int row = 0; row < grid.length - 3; row++) {
                for (int col = 0; col < grid[0].length - 3; col++) {
                    if (grid[row][col].equals(grid[row + 1][col + 1]) &&
                            grid[row][col].equals(grid[row + 2][col + 2]) &&
                            grid[row][col].equals(grid[row + 3][col + 3]) &&
                            !grid[row][col].equals(new Color(255, 255, 255))) {
                        return true;
                    }
                }
            }

            // check diagonal (bottom-left to top-right)
            for (int row = grid.length - 1; row >= 3; row--) {
                for (int col = 0; col < grid[0].length - 3; col++) {
                    if (grid[row][col].equals(grid[row - 1][col + 1]) &&
                            grid[row][col].equals(grid[row - 2][col + 2]) &&
                            grid[row][col].equals(grid[row - 3][col + 3]) &&
                            !grid[row][col].equals(new Color(255, 255, 255))) {
                        return true;
                    }
                }
            }

            return false;
        }


        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }

        public void mouseClicked(MouseEvent e) {

        }
    }
}
