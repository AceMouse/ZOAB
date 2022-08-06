package Main;

import Game.Game;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        OutputStream out = new BufferedOutputStream(new ByteArrayOutputStream());
        Game game = new Game(System.in, out, 4);
        game.run();
    }
}
