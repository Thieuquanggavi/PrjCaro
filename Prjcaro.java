package out.production.bienjava1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Prjcaro extends JPanel {
    private static final int SIZE = 25;
    private static final int CELL_SIZE = 30;
    private static final char EMPTY = '.';
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';
    private char[][] board = new char[SIZE][SIZE];
    private boolean isPlayerXTurn = true;
    private boolean gameWon = false;
    private int scoreX = 0;
    private int scoreO = 0;
    private JLabel scoreLabel;

    public Prjcaro() {
        initializeBoard();
        setLayout(new BorderLayout());
        // Tỉ số
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(3, 1));
        scoreLabel = new JLabel("Score - Player X: 0 | Player O: 0", SwingConstants.CENTER);
        scorePanel.add(scoreLabel);
        // Chơi lại
        JButton resetButton = new JButton("Play Again");
        resetButton.addActionListener(e -> resetGame());
        scorePanel.add(resetButton);
        scorePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scorePanel, BorderLayout.EAST);
        //Dùng chuột
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e)
            {
                if (gameWon) return;
                int row = e.getY() / CELL_SIZE;
                int col = e.getX() / CELL_SIZE;
                if (row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == EMPTY)
                {
                    makeMove(row, col, isPlayerXTurn ? PLAYER_X : PLAYER_O);
                    repaint();
                    if (checkWin(row, col, isPlayerXTurn ? PLAYER_X : PLAYER_O))
                    {
                        gameWon = true;
                        if (isPlayerXTurn)
                        {
                            scoreX++;
                        } else {
                            scoreO++;
                        }
                        updateScoreLabel();
                        int response = JOptionPane.showConfirmDialog(null, "Player " + (isPlayerXTurn ? "X" : "O") + " won! Do you want to play again?", "End", JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.YES_OPTION)
                        {
                            resetGame();
                        }
                    }
                    isPlayerXTurn = !isPlayerXTurn;
                }
            }
        });
    }

    private void initializeBoard()
    {
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                board[i][j] = EMPTY;
            }
        }
        gameWon = false;
        isPlayerXTurn = true;
    }

    private void makeMove(int row, int col, char player)
    {
        board[row][col] = player;
    }

    private boolean checkWin(int row, int col, char player)
    {
        return checkDirection(row, col, player, 1, 0)
                || checkDirection(row, col, player, 0, 1)
                || checkDirection(row, col, player, 1, 1)
                || checkDirection(row, col, player, 1, -1);
    }

    private boolean checkDirection(int row, int col, char player, int dx, int dy)
    {
        int count = 1;

        for (int i = 1; i < 5; i++)
        {
            int newRow = row + i * dx;
            int newCol = col + i * dy;
            if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE && board[newRow][newCol] == player)
            {
                count++;
            } else {
                break;
            }
        }

        for (int i = 1; i < 5; i++)
        {
            int newRow = row - i * dx;
            int newCol = col - i * dy;
            if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE && board[newRow][newCol] == player) {
                count++;
            } else
            {
                break;
            }
        }

        return count >= 5;
    }


    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        for (int i = 0; i <= SIZE; i++)
        {
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, SIZE * CELL_SIZE);
            g.drawLine(0, i * CELL_SIZE, SIZE * CELL_SIZE, i * CELL_SIZE);
        }

        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                if (board[i][j] == PLAYER_X)
                {
                    g.setColor(Color.RED);
                    g.drawLine(j * CELL_SIZE + 5, i * CELL_SIZE + 5, (j + 1) * CELL_SIZE - 5, (i + 1) * CELL_SIZE - 5);
                    g.drawLine((j + 1) * CELL_SIZE - 5, i * CELL_SIZE + 5, j * CELL_SIZE + 5, (i + 1) * CELL_SIZE - 5);
                } else if (board[i][j] == PLAYER_O)
                {
                    g.setColor(Color.BLUE);  //
                    g.drawOval(j * CELL_SIZE + 5, i * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                }
            }
        }
    }

    private void resetGame()
    {
        initializeBoard();
        repaint();
    }

    private void updateScoreLabel()
    {
        scoreLabel.setText("Score - Player X: " + scoreX + " | Player O: " + scoreO);
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Caro Game");
        Prjcaro gamePanel = new Prjcaro();
        gamePanel.setPreferredSize(new Dimension(SIZE * CELL_SIZE, SIZE * CELL_SIZE));
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
