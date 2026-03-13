package server;


public class BoardLoader {
    private static final int BLACK = StdDraw.BLACK.getRGB();
    private static final int MAGENTA = StdDraw.MAGENTA.getRGB();
    private static final int PINK = StdDraw.PINK.getRGB();
    private static final int CYAN = StdDraw.CYAN.getRGB();

    public static int[][] createBoard(int level) {
        int size = 15;
        int[][] board = new int[size][size];

        //coins
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                board[x][y] = PINK;
            }
        }

        //out walls
        for (int i = 0; i < size; i++) {
            board[i][0] = MAGENTA;
            board[i][14] = MAGENTA;
            board[0][i] = MAGENTA;
            board[14][i] = MAGENTA;
        }

        //row 2
        board[2][2] = MAGENTA; board[3][2] = MAGENTA; board[4][2] = MAGENTA;
        board[6][2] = MAGENTA;
        board[8][2] = MAGENTA;
        board[10][2] = MAGENTA; board[11][2] = MAGENTA; board[12][2] = MAGENTA;

        //inside walls
        board[2][4] = MAGENTA; board[3][4] = MAGENTA;
        board[6][4] = MAGENTA; board[7][4] = BLACK; board[8][4] = MAGENTA;
        board[11][4] = MAGENTA; board[12][4] = MAGENTA;

        //inside walls
        board[11][5] = MAGENTA; board[11][6] = MAGENTA;
        board[3][5] = MAGENTA; board[3][6] = MAGENTA;

        //inside walls
        board[0][6] = MAGENTA; board[1][6] = MAGENTA;
        board[6][6] = MAGENTA; board[8][6] = MAGENTA;
        board[13][6] = MAGENTA; board[14][6] = MAGENTA;

        //center cage
        board[8][5] = BLACK;
        board[6][5] = BLACK;
        board[5][5] = MAGENTA;

        board[9][5] = MAGENTA;
        board[7][3] = MAGENTA;
        board[7][6] = BLACK;  // center empty
        board[7][5] = BLACK;  // center empty


        //inside walls
        board[0][8] = MAGENTA; board[1][8] = MAGENTA; board[2][8] = MAGENTA;
        board[4][8] = MAGENTA;
        board[6][8] = MAGENTA; board[7][8] = MAGENTA; board[8][8] = MAGENTA;
        board[10][8] = MAGENTA;
        board[12][8] = MAGENTA; board[13][8] = MAGENTA; board[14][8] = MAGENTA;

        //inside walls
        board[4][9] = MAGENTA;
        board[10][9] = MAGENTA;

        //inside walls
        board[2][10] = MAGENTA; board[3][10] = MAGENTA; board[4][10] = MAGENTA;
        board[6][10] = MAGENTA; board[7][10] = MAGENTA; board[8][10] = MAGENTA;
        board[10][10] = MAGENTA; board[11][10] = MAGENTA; board[12][10] = MAGENTA;

        //inside wlls
        board[2][12] = MAGENTA; board[3][12] = MAGENTA; board[4][12] = MAGENTA;
        board[6][12] = MAGENTA;
        board[8][12] = MAGENTA;
        board[10][12] = MAGENTA; board[11][12] = MAGENTA; board[12][12] = MAGENTA;

        //coins
        board[1][3] = CYAN;
        board[13][3] = CYAN;
        board[1][11] = CYAN;
        board[13][11] = CYAN;

        //start
        board[7][7] = BLACK;

        return board;
    }
    public static MyGhost[] createGhosts(int level){
        int count = Math.min(level + 1, 4);
        MyGhost[] ghosts = new MyGhost[count];

        for (int i = 0; i < count; i++) {
            ghosts[i] = new MyGhost(6 + i, 5, i, true);

        }
        return ghosts;
    }
}
