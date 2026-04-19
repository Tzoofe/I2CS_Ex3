package server;

import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;

public class Ex3Algo implements PacManAlgo {
    @Override
    /**
     *  Add a short description for the algorithm as a String.
     */
    public String getInfo() {
        return "State-based Pacman AI: collects pink dots (then green), runs from dangerous ghosts, hunts eatable ghosts.";
    }

    /**
     * This ia the main method - that you should design, implement and test.
     */
    //STATES
    private static final int NORMAL = 0;
    private static final int PANIC = 1;
    private static final int HUNTING = 2;
    private int _state = NORMAL;
    private int _huntTimer = 0;


    private int _startTimer = 100;

    @Override
    public int move(PacmanGame game) {

        int code = 0;
        //gamestate
        int[][] board = game.getGame(code);
        String posStr = game.getPos(code);
        String[] parts = posStr.split(",");
        int px = Integer.parseInt(parts[0]);
        int py = Integer.parseInt(parts[1]);
        Pixel2D pacman = new Index2D(px, py);
        System.out.println(pacman);
        int black = StdDraw.BLACK.getRGB();
        int blue = StdDraw.MAGENTA.getRGB();
        int pink = StdDraw.PINK.getRGB();
        int green = StdDraw.CYAN.getRGB();
        //ghosts
        GhostCL[] ghosts = game.getGhosts(code);
        printGhosts(ghosts);


        //create a map
        Map map = new Map(board);
        Map2D distances = map.allDistance(pacman, blue); //get distances from pacman to the next available block

        if (_startTimer > 0) {
            _startTimer--;
            //grace period chase food
            Pixel2D target = findNearestColor(board, distances, pink);
            if (target == null) target = findNearestColor(board, distances, green);
            if (target == null) return randomDir();

            Pixel2D[] path = map.shortestPath(pacman, target, blue);
            if (path == null || path.length < 2) return randomDir();

            return getDirection(pacman, path[1]);
        }
        //states
        updateState(pacman, ghosts, code);

        switch (_state) {
            case PANIC:
                System.out.println("PANICA PANICA");
                return runFromGhost(pacman, ghosts, board, blue, code);
            case HUNTING:
                System.out.println("HUNTING");
                return chaseGhost(pacman, ghosts, map, blue, code);
            default:
                //pink finding
                Pixel2D target = findNearestColor(board, distances, pink);
                System.out.println("Target: " + target);
                if (target == null) {
                    target = findNearestColor(board, distances, green);
                }
                if (target == null) {
                    return randomDir();
                }


                //shortest path find
                Pixel2D[] path = map.shortestPath(pacman, target, blue);
                if (path == null || path.length < 2) {
                    System.out.println("No path or too short!");
                    return randomDir();

                }

                return getDirection(pacman, path[1]);
        }
    }

    private void updateState(Pixel2D pacman, GhostCL[] ghosts, int code) {
        _state = NORMAL;

        for (GhostCL ghost : ghosts) {
            //skip caged ghosts
            if (ghost.getStatus() == 0) continue;

            String ghostPosString = ghost.getPos(code);
            String[] ghostPosRaw = ghostPosString.split(",");
            int gx = Integer.parseInt(ghostPosRaw[0]);
            int gy = Integer.parseInt(ghostPosRaw[1]);
            Pixel2D ghostPos = new Index2D(gx, gy);
            //distance calc
            double dist = pacman.distance2D(ghostPos);

            if (dist <= 2) {
                if (ghost.remainTimeAsEatable(code) > 0) {
                    _state = HUNTING;
                } else {
                    _state = PANIC;
                    return;
                }
            }
        }
    }

    private int runFromGhost(Pixel2D pacman, GhostCL[] ghosts, int[][] board, int obsColor, int code) {
        Pixel2D nearestGhost = null;
        double minDist = Integer.MAX_VALUE;

        for (int i = 0; i < ghosts.length; i++) {
            //skip caged ghosts
            if (ghosts[i].getStatus() == 0) continue;

            String ghostPosString = ghosts[i].getPos(code);
            String[] ghostPosRaw = ghostPosString.split(",");
            int gx = Integer.parseInt(ghostPosRaw[0]);
            int gy = Integer.parseInt(ghostPosRaw[1]);
            Pixel2D ghostPos = new Index2D(gx, gy);

            double dist = pacman.distance2D(ghostPos);
            if (dist < minDist) {
                minDist = dist;
                nearestGhost = ghostPos;
            }
        }
        if (nearestGhost == null) return randomDir();

        int escapeDir = getDirection(nearestGhost, pacman);
        int width = board.length;
        int height = board[0].length;

        //check if escape direction is blocked
        int[] nextPos = getNextPosition(pacman, escapeDir, width, height);
        if (board[nextPos[0]][nextPos[1]] != obsColor) {
            return escapeDir;
        }

        //try other directions
        int[] tryOrder = {MyGame.UP, MyGame.LEFT, MyGame.DOWN, MyGame.RIGHT};
        for (int dir : tryOrder) {
            int[] testPos = getNextPosition(pacman, dir, width, height);
            if (board[testPos[0]][testPos[1]] != obsColor) {
                return dir;
            }
        }

        return randomDir();
    }

    private int[] getNextPosition(Pixel2D pos, int dir, int width, int height) {
        int dx = 0, dy = 0;
        if (dir == MyGame.UP) dy = -1;
        else if (dir == MyGame.DOWN) dy = 1;
        else if (dir == MyGame.LEFT) dx = -1;
        else if (dir == MyGame.RIGHT) dx = 1;

        int nx = (pos.getX() + dx + width) % width;
        int ny = (pos.getY() + dy + height) % height;
        return new int[]{nx, ny};
    }

    private int chaseGhost(Pixel2D pacman, GhostCL[] ghosts, Map map, int code, int obsColor) {
        Pixel2D nearestGhost = null;
        double minDist = Integer.MAX_VALUE;
        for (int i = 0; i < ghosts.length; i++) {
            if (ghosts[i].remainTimeAsEatable(code) <= 0) continue;
            String ghostPosStr = ghosts[i].getPos(code);
            String[] parts = ghostPosStr.split(",");
            int gx = Integer.parseInt(parts[0]);
            int gy = Integer.parseInt(parts[1]);
            Pixel2D ghostPos = new Index2D(gx, gy);

            double dist = pacman.distance2D(ghostPos);
            if (dist < minDist) {
                minDist = dist;
                nearestGhost = ghostPos;
            }
        }
        if (nearestGhost == null) return randomDir();
        Pixel2D[] path = map.shortestPath(pacman, nearestGhost, obsColor);
        if (path == null || path.length < 2) return randomDir();
        return getDirection(pacman, path[1]);
    }

    private Pixel2D findNearestColor(int[][] board, Map2D distances, int color) {
        Pixel2D nearest = null;
        int minDist = Integer.MAX_VALUE;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == color) {
                    int dist = distances.getPixel(x, y);
                    if (dist != -1 && dist < minDist) {
                        minDist = dist;
                        nearest = new Index2D(x, y);
                    }
                }
            }
        }
        return nearest;
    }

    private int getDirection(Pixel2D from, Pixel2D to) {
        //gets the distance difference between where he needs to go and where he came from
        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();

        if (dx > 1) dx = -1;
        if (dx < -1) dx = 1;
        if (dy > 1) dy = -1;
        if (dy < -1) dy = 1;

        //define path
        if (dx == 1) return MyGame.RIGHT;
        if (dx == -1) return MyGame.LEFT;
        if (dy == 1) return MyGame.UP;
        if (dy == -1) return MyGame.DOWN; //screen cords inverted

        //fallback
        return MyGame.UP;

    }


    private static void printBoard(int[][] b) {
        for (int y = 0; y < b[0].length; y++) {
            for (int x = 0; x < b.length; x++) {
                int v = b[x][y];
                System.out.print(v + "\t");
            }
            System.out.println();
        }
    }

    private static void printGhosts(GhostCL[] gs) {
        for (int i = 0; i < gs.length; i++) {
            GhostCL g = gs[i];
            System.out.println(i + ") status: " + g.getStatus() + ",  type: " + g.getType() + ",  pos: " + g.getPos(0) + ",  time: " + g.remainTimeAsEatable(0));
        }
    }

    private static int randomDir() {
        int[] dirs = {MyGame.UP, MyGame.LEFT, MyGame.DOWN, MyGame.RIGHT};
        int ind = (int) (Math.random() * dirs.length);
        return dirs[ind];
    }
}