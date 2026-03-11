package server;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacmanGame;

public class MyGame implements PacmanGame {
    //states
    public static final int DONE = -1;
    public static final int PAUSED = 0;
    public static final int RUNNING = 1;

    //dirs
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;


    private int[][] _board;
    private int _width, _height; //board dims
    private int _pacX, _pacY; //pac pos
    private MyGhost[] _ghosts; //array of ghosts

    //states
    private int _score;
    private int _status;

    //settings
    private boolean _cyclic;
    private int _dt; //delay between frames

    //Colors
    private static final int BLACK = StdDraw.BLACK.getRGB();
    private static final int MAGENTA = StdDraw.MAGENTA.getRGB();
    private static final int PINK = StdDraw.PINK.getRGB();
    private static final int CYAN = StdDraw.CYAN.getRGB();

    public MyGame() {
        //default values
        _status = PAUSED;
        _score = 0;
    }


    @Override
    public Character getKeyChar() {
        if(StdDraw.hasNextKeyTyped()){
            return StdDraw.nextKeyTyped();
        }
        return null;
    }

    @Override
    public String getPos(int i) {
        return (_pacX + "," + _pacY);
    }

    @Override
    public GhostCL[] getGhosts(int i) {
        return _ghosts;
    }

    @Override
    public int[][] getGame(int i) {
        return _board;
    }

    @Override
    public void play() {
        if(_status == PAUSED){
            _status = RUNNING;
        }else if(_status == RUNNING){
            _status = PAUSED;
        }
    }

    @Override
    public String end(int i) {
        return "";
    }

    @Override
    public String getData(int i) {
        return "";
    }

    @Override
    public int getStatus() {
        return _status;
    }

    @Override
    public boolean isCyclic() {
        return _cyclic;
    }

    public void draw() {
        StdDraw.clear();

        //draw actual baord
        for (int x = 0; x < _width; x++) {
            for (int y = 0; y < _height; y++) {
                double centerX = x + 0.5;
                double centerY = y + 0.5;

                if (_board[x][y] == MAGENTA) {
                    StdDraw.picture(centerX, centerY, "imgs/wall.png", 1, 1);
                }
                else if (_board[x][y] == CYAN) {
                    StdDraw.picture(centerX, centerY, "imgs/powerup.jpeg", 0.5, 0.5);
                }
                else if (_board[x][y] == PINK) {
                    StdDraw.picture(centerX, centerY, "imgs/coin.jpg", 0.5, 0.5);
                }

            }
        }
        //draw pacman
        StdDraw.picture(_pacX + 0.5, _pacY + 0.5, "imgs/pacman.png", 0.7, 0.9);

        //ghsts
        for (MyGhost g : _ghosts) {
            String img = "imgs/g" + g.getType() + ".png";
            StdDraw.picture(g.getX() + 0.5, g.getY() + 0.5, img, 0.7, 0.9);
        }

        StdDraw.show();
    }

    @Override
    //move
    public String move(int i){
        int newX = _pacX;
        int newY = _pacY;

        if(i == UP) newY++;
        else if(i == DOWN) newY--;
        else if(i == LEFT) newX--;
        else if(i == RIGHT) newX++;

        //cyclic
        if(_cyclic) {
            newX = (newX + _width) % _width;
            newY = (newY + _height) % _height;
        }
        //bounds if !cyclic
        else {
            if (newX < 0 || newX >= _width || newY < 0 || newY >= _height) {
                return (_pacX + "," + _pacY);
            }
        }
        //wall
        if(_board[newX][newY] == MAGENTA) {
            return (_pacX + "," + _pacY);
        }
        //coins
        if(_board[newX][newY] == PINK) {
            _score +=1;
            _board[newX][newY] = BLACK;
        } else if (_board[newX][newY] == CYAN) {
            _score += 10;
            _board[newX][newY] = BLACK;
        }
        //update pos
        _pacX = newX;
        _pacY = newY;

        draw();
        return (_pacX + "," + _pacY);
    }






    //init
    //7 params to match the already existing main
    public String init(int level, String id, boolean cyclic, long seed, double res, int dt, int x) {
        _board = BoardLoader.createBoard(level);
        _ghosts = BoardLoader.createGhosts(level);
        _width = _board.length;
        _height = _board[0].length;
        _pacX = _width / 2;
        _pacY = _height / 2;
        _cyclic = cyclic;
        _dt = dt;

        //stdraw
        StdDraw.setCanvasSize(_width * 30, _height * 30);
        StdDraw.setXscale(0, _width);
        StdDraw.setYscale(0, _height);
        StdDraw.enableDoubleBuffering();

        draw();


        return "";
    }
}
