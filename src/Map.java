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
		int ans=0;
		/////// add your code below ///////

		///////////////////////////////////
		return ans;
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
        java.util.Queue<Pixel2D> queue = new java.util.LinkedList<>();
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
        java.util.Queue<Pixel2D> queue = new java.util.LinkedList<>();
        queue.add(start);
        //bfs loop
        while(!queue.isEmpty()) {
            Pixel2D current = queue.poll();
            int currentDist = result.getPixel(current);

            int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            for (int[] d : dirs) {
                int nx = current.getX() + d[0];
                int ny = current.getY() + d[1];
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
