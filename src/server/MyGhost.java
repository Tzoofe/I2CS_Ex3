package server;
import exe.ex3.game.GhostCL;


public class MyGhost implements GhostCL {
    private int _x, _y;
    private int _status;
    private int _type;
    private int _eatableTime;

    public String getInfo(){
        return ("Ghost type: " + _type + " at (" + _x + "," + _y + ")");
    }

    public MyGhost(int x, int y, int type) {
        _x = x;
        _y = y;
        _type = type;
        _status = 1;
        _eatableTime = 0;
    }
    @Override
    public int getStatus() { return _status;}
    @Override
    public int getType() { return _type;}
    @Override
    public String getPos(int code){return _x + "," + _y;}
    @Override
    public double remainTimeAsEatable(int code){return _eatableTime;}


    //SETTERS
    public void setPos(int x, int y){_x = x; _y = y;}
    public void setStatus(int s){_status = s;}
    public void setEatableTime(int time){_eatableTime = time;}
    public void tick(){
        if(_eatableTime > 0) {
            _eatableTime--;
        }
    }
    public int getX(){return _x;}
    public int getY(){return _y;}
}
