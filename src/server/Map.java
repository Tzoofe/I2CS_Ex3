package server;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D {
	private int[][] _map;
	private boolean _cyclicFlag = true;
	
	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {init(w,h, v);}
	/**
	 * Constructs a square map (size*size).
	 * @param size
	 */
	public Map(int size) {this(size,size, 0);}
	public Map() {
        this(1, 1, 0);
    }
	/**
	 * Constructs a map from a given 2D array.
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}
	@Override
	public void init(int w, int h, int v) {
        _map = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                _map[x][y] = v;
            }
        }
	}
	@Override
	public void init(int[][] arr) {
		int w = arr.length;
        int h = arr[0].length;
        _map = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                _map[x][y] = arr[x][y];
            }
        }
	}
	@Override
	public int[][] getMap() {
		return _map;
	}
	@Override
	public int getWidth() {return _map.length;}
	@Override
	public int getHeight() {return _map[0].length;}
	@Override
	public int getPixel(int x, int y) { return _map[x][y];}
	@Override
	public int getPixel(Pixel2D p) {
		return this.getPixel(p.getX(),p.getY());
	}
	@Override

	public void setPixel(int x, int y, int v) {_map[x][y] = v;}

	@Override
	public void setPixel(Pixel2D p, int v) {
		_map[p.getX()][p.getY()] = v;
	}
	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v) {
//check if inside
        if(!isInside(xy)) return 0;
        //check old color
        int old_v = getPixel(xy);
        //dont color same color twice
        if(old_v == new_v) return 0;
        //BFS
        Queue<Pixel2D> q = new LinkedList<>();
        q.add(xy);

        //paint the pixel
        setPixel(xy, new_v);
        //record pixel painted
        int count = 1;

        //waveloop
        while(!q.isEmpty()) {
            Pixel2D current = q.poll();
            //check directions (up down right left - in that order)
            int[] dx = {0, 0, 1, -1};
            int[] dy = {1, -1, 0, 0};
            //start checking around
            for (int i = 0; i < 4; i++) { //4 for 4 directions
                int nx = current.getX() + dx[i];
                int ny = current.getY() + dy[i];
                if(_cyclicFlag) { //pacman or hit a wall
                    nx = (nx + getWidth()) % getWidth();
                    ny = (ny + getHeight()) % getHeight();
                }
                Pixel2D neighbor = new Index2D(nx, ny);
                if(isInside(neighbor) && getPixel(neighbor) == old_v) {
                    setPixel(neighbor, new_v);
                    q.add(neighbor);
                    count++;
                }
            }
        }

        return count;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        int width = getWidth();
        int height = getHeight();
        //parent
        Pixel2D[][] parent = new Pixel2D[width][height];
        boolean[][] visited = new boolean[width][height];
        //bfs queue
        Queue<Pixel2D> queue = new LinkedList<>();
        queue.add(p1);
        visited[p1.getX()][p1.getY()] = true;

        while(!queue.isEmpty()) {
            Pixel2D currentP = queue.poll();
            //found target stop
            if(currentP.getX() == p2.getX() && currentP.getY() == p2.getY()) {
                break;
            }
            //check surrounding
            int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            for (int[] d : directions) {
                int nx = currentP.getX() + d[0];
                int ny = currentP.getY() + d[1];
                if(_cyclicFlag){
                    nx = (nx + getWidth()) % getWidth();
                    ny = (ny + getHeight()) % getHeight();
                }else {
                    if (nx < 0 || nx >= getWidth() || ny < 0 || ny >= getHeight()) continue;
                }
                Pixel2D neighbor = new Index2D(nx, ny);
                //skip if out of bounds
                if(!isInside(neighbor)) continue;
                //skip if is obs
                if(getPixel(neighbor) == obsColor) continue;
                //skip if visited
                if(visited[nx][ny]) continue;
                //mark point as visited and set parent
                visited[nx][ny] = true;
                parent[nx][ny] = currentP;
                queue.add(neighbor);

            }
        }
        //we check if we actaully reach the point
        if(!visited[p2.getX()][p2.getY()]){
            return null; //no path;
        }
        java.util.ArrayList<Pixel2D> path = new java.util.ArrayList<>();
        Pixel2D currentP = p2;
        while(currentP != null) {
            path.add(currentP);
            currentP = parent[currentP.getX()][currentP.getY()];
        }
        java.util.Collections.reverse(path);
        return path.toArray(new Pixel2D[0]);
    }
	@Override
	public boolean isInside(Pixel2D p) {
		int x = p.getX();
        int y = p.getY();
        return x>= 0 && x< getWidth() && y>= 0 && y < getHeight();


	}

	@Override
	public boolean isCyclic() {
		return _cyclicFlag;
	}
	@Override
	public void setCyclic(boolean cy) {_cyclicFlag = cy;}
	@Override

	public Map2D allDistance(Pixel2D start, int obsColor) {
        Map2D result = new Map(getWidth(), getHeight(), -1);
        result.setPixel(start, 0);

        //bfs queue
        Queue<Pixel2D> queue = new LinkedList<>();
        queue.add(start);
        //bfs loop
        while(!queue.isEmpty()) {
            Pixel2D current = queue.poll();
            int currentDist = result.getPixel(current);

            int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            for (int[] d : dirs) {
                int nx = current.getX() + d[0];
                int ny = current.getY() + d[1];
                if(_cyclicFlag){
                    nx = (nx + getWidth()) % getWidth();
                    ny = (ny + getHeight()) % getHeight();
                }else {
                    if (nx < 0 || nx >= getWidth() || ny < 0 || ny >= getHeight()) continue;
                }
                Pixel2D neighbor = new Index2D(nx, ny);
                //skip if in bounds
                if(!isInside(neighbor)) continue;
                //skip if obstacle
                if(getPixel(neighbor) == obsColor) continue;
                //skip if already visited
                if(result.getPixel(neighbor) != -1) continue;
                //set the distance and add to queue
                result.setPixel(neighbor, currentDist + 1);
                queue.add(neighbor);
            }
        }
        return result;
	}
}
