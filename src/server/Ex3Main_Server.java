package server;

public class Ex3Main_Server {
    public static void main(String[] arg) {
        MyGame game =new MyGame();
        game.init(3, "p1", true, 0,1.0, 100, 0);

        game.play();

        //loop
        while(game.getStatus() != MyGame.DONE){
            Character key = game.getKeyChar();
            if(key != null) {
                int dir = -1;
                if (key == 'w' || key == 'W') dir = MyGame.UP;
                else if (key == 's' || key == 'S') dir = MyGame.DOWN;
                else if (key == 'd' || key == 'D') dir = MyGame.RIGHT;
                else if (key == 'a' || key == 'A') dir = MyGame.LEFT;

                if (dir != -1) {
                    game.move(dir);
                }
            }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
