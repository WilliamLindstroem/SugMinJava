package FourInARow;

import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public final class GamePanel extends JPanel {

    private static final int ROWS = 6;
    private static final int COLS = 7;

    private static final int TILE = 135;
    private static final int PADDING = 30;

    private static final int BOARD_W = COLS * TILE;
    private static final int BOARD_H = ROWS * TILE;

    private static final int PANEL_W = BOARD_W + PADDING * 2;
    private static final int PANEL_H = BOARD_H + PADDING * 2 + 60; // extra space for text

    private final Board board = new Board(ROWS, COLS);

    private int currentPlayer = 1; // 1 or 2
    private GameState gameState = GameState.PLAYING;

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_W, PANEL_H));
        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });

        // Key binding: R to reset
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "reset");
        getActionMap().put("reset", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                resetMatch();
            }
        });
    }

    private void handleClick(int mouseX, int mouseY) {
        if (gameState != GameState.PLAYING) return;

        int boardX = PADDING;
        int boardY = PADDING;

        // Only accept clicks inside the board rectangle
        if (mouseX < boardX || mouseX >= boardX + BOARD_W) return;
        if (mouseY < boardY || mouseY >= boardY + BOARD_H) return;

        int col = (mouseX - boardX) / TILE;

        int landedRow = board.dropDisc(col, currentPlayer);
        if (landedRow == -1) return; // invalid move (column full)

        int winner = board.checkWinner();
        if (winner == 1) {
            gameState = GameState.WIN_P1;
        } else if (winner == 2) {
            gameState = GameState.WIN_P2;
        } else if (board.isFull()) {
            gameState = GameState.DRAW;
        } else {
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
        }

        repaint();
    }

    private void resetMatch() {
        board.reset();
        currentPlayer = 1;
        gameState = GameState.PLAYING;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int boardX = PADDING;
        int boardY = PADDING;

        g2.setColor(Color.blue);
        g2.fillRect(0,0,getWidth(),getHeight());



        // Board border
        g2.setColor(Color.BLACK);
        g2.drawRect(boardX, boardY, BOARD_W, BOARD_H);

        // Grid + pieces
        int pieceInset = 10;
        int pieceSize = TILE - pieceInset * 2;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int x = boardX + c * TILE;
                int y = boardY + r * TILE;

                // Cell border
                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, TILE, TILE);

                // Piece (if any)
                int cell = board.getCell(r, c);
                if (cell != 0) {
                    int px = x + pieceInset;
                    int py = y + pieceInset;


                    if (cell == 1) {
                        g2.setColor(Color.DARK_GRAY);

                    } else {
                        g2.setColor(Color.LIGHT_GRAY);
                    }
                    // Minimalistic differentiation: filled vs outline would also work.
                    // Here we just fill; default color is fine for now.
                    g2.fillRect(px, py, pieceSize, pieceSize);
                }
            }
        }

        // Status text under the board
        int textY = boardY + BOARD_H + 35;
        String status;
        switch (gameState) {
            case PLAYING -> status = "Playing. Current player: " + currentPlayer + " (click a column). Press R to reset.";
            case WIN_P1 -> status = "Player 1 wins. Press R to reset.";
            case WIN_P2 -> status = "Player 2 wins. Press R to reset.";
            case DRAW -> status = "Draw. Press R to reset.";
            default -> status = "";
        }

        g2.setColor(Color.BLACK);
        g2.drawString(status, boardX, textY);
    }
}
