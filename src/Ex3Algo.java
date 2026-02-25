import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;

public class Ex3Algo implements PacManAlgo{
	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return null;
	}
	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
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
        int black = Game.getIntColor(Color.BLACK, code);
        int blue = Game.getIntColor(Color.BLUE, code);
        int pink = Game.getIntColor(Color.PINK, code);
        int green = Game.getIntColor(Color.GREEN, code);

        //create a map
        Map map = new Map(board);
        Map2D distances = map.allDistance(pacman, blue); //get distances from pacman to the next available block

        //pink finding
        Pixel2D target = findNearestColor(board, distances, pink);
        System.out.println("Target: " + target);
        if(target == null) {
            target = findNearestColor(board, distances, green);
        }
        if(target == null) {
            return randomDir();
        }


        //shortest path find
        Pixel2D[] path = map.shortestPath(pacman, target, blue);
        if(path == null || path.length < 2) {
            System.out.println("No path or too short!");
            return randomDir();

        }

        return getDirection(pacman, path[1]);



    }
	private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}
	private static void printGhosts(GhostCL[] gs) {
		for(int i=0;i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}
	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}

    private Pixel2D findNearestColor(int[][] board, Map2D distances, int color) {
        Pixel2D nearest = null;
        int minDist = Integer.MAX_VALUE;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if(board[x][y] == color) {
                    int dist = distances.getPixel(x, y);
                    if(dist != -1 && dist < minDist){
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

        if(dx > 1) dx = -1;
        if(dx < -1) dx = 1;
        if(dy > 1) dy = -1;
        if(dy < -1) dy = 1;

        //define path
        if(dx == 1) return Game.RIGHT;
        if(dx == -1) return Game.LEFT;
        if(dy == 1) return Game.UP;
        if(dy == -1) return Game.DOWN; //screen cords inverted

        //fallback
        return Game.UP;

    }
}