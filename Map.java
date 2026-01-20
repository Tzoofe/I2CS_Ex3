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
	
	/**
	 * Constructs a map from a given 2D array.
	 * @param data
	 */
	public Map(int[][] data) {
        init(data);
    }
    @Override
    public void init(int w, int h, int v) {
        //create a matrice with the width and height of x and y, and the numbers inside the matrice is v
        this._map = new int[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                this._map[y][x] = v;
            }
        }
    }
    @Override
    public void init(int[][] arr) {
        if(arr == null || arr.length == 0) return;
        //get dimensions
        int w = arr[0].length;
        int h = arr.length;
        this._map = new int[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                this._map[y][x] = arr[y][x];
            }
        }

    }
	@Override
	public int[][] getMap() {
		return this._map;
	}
	@Override

	public int getWidth() {
        if(_map == null) return 0;
        return _map[0].length;

    }
	@Override
	/////// add your code below ///////
	public int getHeight() {
        if(_map == null) return 0;
        return _map.length;

    }
    public int getPixel(int x, int y) {
        //safety check
        if(x < getWidth() && x >= 0 && y < getHeight() && y >= 0) {
            return _map[y][x];
        }
        return 0;
    }
    @Override
    public int getPixel(Pixel2D p) {
        //return the pixel's position
        return getPixel(p.getX(), p.getY());
    }
    @Override
    public void setPixel(int x, int y, int v) {
        if(x < getWidth() && x >= 0 && y < getHeight() && y >= 0) {
            _map[y][x] = v;
        }
    }
    @Override
    public void setPixel(Pixel2D p, int v) {
        setPixel(p.getX(), p.getY(), v); //sets the new values.
    }

	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
    public int fill(Pixel2D xy, int new_v) {
        int ans = 0; // Initialize the count of filled pixels to 0

        int old_v = getPixel(xy); // Get the current color at pixel p
        if (old_v == new_v) {
            // If the current color is already the new color, no need to fill

            return ans;
        }
        return floodFill(xy, old_v, new_v);
    }
    private int floodFill(Pixel2D p,int old_v,int new_v) {
        // Check if the pixel is inside the map and has the old color
        if (!isInside(p) || getPixel(p) != old_v) {
            return 0;
        }
        setPixel(p, new_v); // Set the pixel color to the new color
        int counter = 1; // Counter to count the current pixel as filled

        Pixel2D[] neighbors = findTheNeighbors(p); // Get the neighboring pixels

        for (int i = 0; i < neighbors.length; i++) {
            Pixel2D neighbor = neighbors[i];
            counter += floodFill(neighbor, old_v, new_v); // Recursively fill the neighboring pixels
        }

        return counter; // Return the total count of filled pixels
    }

	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */

    @Override
    public boolean isCyclic() {
        return _cyclicFlag;
    }
    @Override
    public void setCyclic(boolean cy) {
        this._cyclicFlag=cy;
    }

	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {

        if(p1 == null || p2 == null) return null;
        if (getPixel(p1) == obsColor || getPixel(p2) == obsColor) return null; //if inside a color

        //trackermap
        Pixel2D[][] parents = new Pixel2D[getHeight()][getWidth()];

        //bfs queue
        Queue<Pixel2D> q = new LinkedList<>();
        q.add(p1);

        //visited
        boolean[][] visited = new boolean[getHeight()][getWidth()];
        visited[p1.getY()][p1.getX()] = true;
        boolean found = false;

        //search loop
        while(!q.isEmpty()) {
            Pixel2D current = q.poll();

            if(current.equals(p2)) {
                found = true;
                break;
            }
            //checking 4 directions again
            int[] dx = {0, 0, 1, -1};
            int[] dy = {1, -1, 0, 0};
            //start checking around
            for (int i = 0; i < 4; i++) { //4 for 4 directions
                int nx = current.getX() + dx[i];
                int ny = current.getY() + dy[i];

                Pixel2D neighbor = new Index2D(nx, ny);
                //inside map?
                //not an obstacle?
                //NotVisited?
                if(isInside(neighbor) && getPixel(neighbor) != obsColor && !visited[neighbor.getY()][neighbor.getX()]) {
                    //makr seen
                    visited[neighbor.getY()][neighbor.getX()] = true;
                    parents[neighbor.getY()][neighbor.getX()] = current;
                    q.add(neighbor);
                }
            }
        }
        //blocked route
        if(!found) return null;

        //collect the path
        LinkedList<Pixel2D> pathList = new LinkedList<>();
        Pixel2D walker = p2;

        //reverse
        while (walker != null && !walker.equals(p1)) {
            pathList.addFirst(walker); //add to first to be in order
            walker = parents[walker.getY()][walker.getX()];
        }
        pathList.addFirst(p1); //starting point
        return pathList.toArray(new Pixel2D[0]);
	}
	@Override
	public boolean isInside(Pixel2D p) {
        if (p == null || _map == null) return false; //checks for empty
        int x = p.getX();
        int y = p.getY();
        return (x>=0 && x<getWidth() && y>=0 && y<getHeight());
	}


    @Override
	public Map2D allDistance(Pixel2D start, int obsColor) {
        //obligatory ifs
        if(start == null || !isInside(start) || getPixel(start) == obsColor) {
            return null;
        }
        //new map starting at -1 to represent unreachable areas
        Map2D dMap = new Map(getWidth(), getHeight(), -1);
        //bfs
        Queue<Pixel2D> q = new LinkedList<>();
        q.add(start);

        //set starting point
        dMap.setPixel(start, 0);

        //bfs loop

        while(!q.isEmpty()) {
            Pixel2D current = q.poll();
            int currentDist = dMap.getPixel(current);

            //check directions
            int[] dx = {0, 0, 1, -1};
            int[] dy = {1, -1, 0, 0};
            for (int i = 0; i < 4; i++) {
                int nx = current.getX() + dx[i];
                int ny = current.getY() + dy[i];

                //cylic logic
                if((this.isCyclic())) {
                    nx = (nx + getWidth()) % getWidth();
                    ny = (ny + getHeight()) % getHeight();
                }
                Pixel2D neighbor = new Index2D(nx, ny);

                //inside?
                //not an obstacle!
                //unvisited - v = -1 in new map
                if(isInside(neighbor) && getPixel(neighbor) != obsColor && dMap.getPixel(neighbor) == -1){
                    //neighbor dist = myDist + 1
                    dMap.setPixel(neighbor, currentDist + 1);
                    q.add(neighbor);
                }

            }
        }
        return dMap;
	}
    private Pixel2D[] findTheNeighbors(Pixel2D pixel) {
        Pixel2D[] neighbors = new Pixel2D[4];

        int w=getWidth();
        int h= getHeight();
        int x = pixel.getX();
        int y = pixel.getY();

        neighbors[3] = new Index2D(x, (y - 1 + h) % h);//left
        neighbors[2] = new Index2D(x, (y + 1) % h);//right
        neighbors[0] = new Index2D((x - 1 + w) % w, y);//up
        neighbors[1] = new Index2D((x + 1) % w, y);//down

        return neighbors;
    }
}
