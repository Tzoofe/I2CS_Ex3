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
		;
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
		Pixel2D[] ans = null;  // the result.
		/////// add your code below ///////

		///////////////////////////////////
		return ans;
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
		Map2D ans = null;  // the result.

		return ans;
	}
}
