package server;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacmanGame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
    private int _coinsLeft;

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
    public int coinsLeft(int i) {
        return _coinsLeft;
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
    public int getCoinsLeft() {
        return _coinsLeft;
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
            if(g.isAlive()) {
                String img = "imgs/g" + g.getType() + ".png";
                StdDraw.picture(g.getX() + 0.5, g.getY() + 0.5, img, 0.7, 0.9);
            }

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
            _coinsLeft--;
            _board[newX][newY] = BLACK;
        } else if (_board[newX][newY] == CYAN) {
            _score += 10;
            _coinsLeft--;
            _board[newX][newY] = BLACK;
            //make gshots eatable
            for (int j = 0; j < _ghosts.length; j++) {
                _ghosts[j].setEatableTime(50);
                
            }
        }
        if(_coinsLeft == 0) {
            try {
                String win = Files.readString(Path.of("ascii/winning.txt"));
                System.out.println(win);
            } catch (IOException e) {
                e.printStackTrace();
            }
            _status = DONE;
        }
        //update pos
        _pacX = newX;
        _pacY = newY;

        draw();
        checkCollision();
        return (_pacX + "," + _pacY);


    }
    public void tickGhosts() {
        for (int i = 0; i < _ghosts.length; i++) {
            _ghosts[i].tick();
        }
    }



    //ghosts movement
    public void moveGhost() {
        for (int i = 0; i < _ghosts.length; i++) {
            MyGhost ghost = _ghosts[i];
            if(ghost.isAlive()){
                //curretn pos of ghost
                int gx = ghost.getX();
                int gy = ghost.getY();

                //dirs
                int[] dirs = {UP, DOWN, LEFT, RIGHT};

                //random directoins
                int ranDir = dirs[(int)(Math.random() * 4)];
                int newX = gx;
                int newY = gy;

                if (ranDir == UP) newY++;
                else if (ranDir == DOWN) newY--;
                else if (ranDir == LEFT) newX--;
                else if (ranDir == RIGHT) newX++;

                //cyclic
                if(_cyclic) {
                    newX = (newX + _width) % _width;
                    newY = (newY + _height) % _height;
                }
                //bounds if !cyclic
                else {
                    if (newX < 0 || newX >= _width || newY < 0 || newY >= _height) {
                        continue;
                    }
                }
                //wall
                if(_board[newX][newY] != MAGENTA) {
                    ghost.setPos(newX, newY);
                }
            }

        }
        checkCollision();

    }
    //ghost collision
    private void checkCollision() {
        //if the pacmans position and ghost posistion are the same make the status done
        for (int i = 0; i < _ghosts.length; i++) {
            MyGhost ghost = _ghosts[i];
            if(ghost.isAlive()){
                if(_pacX == ghost.getX() && _pacY == ghost.getY()) {
                    //pacman eat ghost
                    if(ghost.remainTimeAsEatable(0) > 0 ) {
                        _score += 100;
                        ghost.setAlive(false);
                    }else {
                        _status = DONE;
                        System.out.println("GAME OVER - COLLISION");
                    }

                }
            }
        }
    }





    //init
    //7 params to match the already existing main
    public String init(int level, String id, boolean cyclic, long seed, double res, int dt, int x) {
        _board = BoardLoader.createBoard(level);
        _ghosts = BoardLoader.createGhosts(level);
        _width = _board.length;
        _height = _board[0].length;
        _coinsLeft = 0;
        for (int posx = 0; posx < _width; posx++) {
            for (int posy = 0; posy < _height; posy++) {
                if(_board[posx][posy] == PINK ||_board[posx][posy] == CYAN) {
                    _coinsLeft++;
                }
            }
        }
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
