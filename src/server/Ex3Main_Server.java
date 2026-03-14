package server;
import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Ex3Main_Server {
    public static void main(String[] arg) throws IOException {
        String ascii = Files.readString(Path.of("ascii/ascii.txt"));
        MyGame game =new MyGame();
        System.out.println(ascii);
        game.init(3, "p1", true, 0,1.0, 100, 0);

        game.play();

        int timer = 50;


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
            game.tickGhosts();
            if(timer > 0) {
                timer--;
            }else {
                game.moveGhost();
            }

            game.draw();
            try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
