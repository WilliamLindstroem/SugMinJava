package FourInARow;

public final class Board {

    private final int rows;
    private final int cols;

    private final int[][] grid;

    public Board(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.grid = new int[rows][cols];

    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }

    public int getCell(int row, int col){
        return grid[row][col];
    }

    public void reset(){
        for (int r = 0; r < rows; r++){
            for (int c = 0; c < cols; c++){
                grid[r][c] = 0;
            }
        }
    }

    public boolean isColumnFull(int col){
        return grid[0][col] != 0;
    }

    public int dropDisc(int col, int player){
        if (col < 0 || col >= cols) return -1;
        if (player != 1 && player != 2) return -1;
        if (isColumnFull(col)) return -1;

        for (int r = rows - 1; r >= 0; r--) {
            if (grid[r][col] == 0) {
                grid[r][col] = player;
                return r;
            }
        }

        return -1;

    }

    public boolean isFull() {
        for (int c = 0; c < cols; c++) {
            if (!isColumnFull(c)) return false;
        }
        return true;
    }

    public int checkWinner() {

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int p = grid[r][c];
                if (p == 0) continue;


                if (c + 3 < cols &&
                p == grid[r][c + 1] &&
                p == grid[r][c + 2] &&
                p == grid[r][c + 3]) {
                    return p;
                }
                if (r + 3 < rows &&
                        p == grid[r + 1][c] &&
                        p == grid[r + 2][c] &&
                        p == grid[r + 3][c]) {
                    return p;
                }
                if (r + 3 < rows && c + 3 < cols &&
                        p == grid[r + 1][c + 1] &&
                        p == grid[r + 2][c + 2] &&
                        p == grid[r + 3][c + 3]) {
                    return p;
                }

                // Diagonal up-right
                if (r - 3 >= 0 && c + 3 < cols &&
                        p == grid[r - 1][c + 1] &&
                        p == grid[r - 2][c + 2] &&
                        p == grid[r - 3][c + 3]) {
                    return p;
                }


            }
        }
        return 0;
    }

}
