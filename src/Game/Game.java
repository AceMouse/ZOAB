package Game;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class Game {
    private InputStream in;
    private OutputStream out;
    private Random rand;

    private int[] board;

    private int foursProbability = 25, square, score;
    public Game(InputStream in, OutputStream out, int square){
        this.in = in;
        this.out = out;
        board = new int[square*square];
        rand = new Random();
        this.square = square;
        score = 0;
    }

    public void run() throws IOException {
        boolean gameOver = false;
        while (!gameOver) {
            addRandomTile();
            printScore();
            printBoard();
            if (!(validMove(0) || validMove(1) || validMove(2) || validMove(3))) {
                gameOver = true;
                break;
            }
            int move;
            do {
                move = in.read()-'0';
            } while (!validMove(move));
            move(move);
            for (int i = 0; i < board.length; i++) {
                out.write(board[i]);
                out.write(' ');
            }
        }
        System.out.println("Game Over!\nYour Score: " + score);
        out.write('t');
        out.write(' ');
        out.write(score);
        out.write(' ');
    }

    private void move(int dir){
        switch (dir){
            case 0:
                for (int i = 0; i < square; i++) {
                    int merged = Integer.MAX_VALUE;
                    for (int j = square-2; j >= 0 ; j--) {
                        int idx = i*square+j;
                        int to = idx+1;
                        while (board[to] == 0 && to <i*square+(square-1) && to + 1 < merged)
                            to++;
                        if (board[idx] == 0)
                            continue;
                        else if (board[to] == 0) {
                            board[to] = board[idx];
                            board[idx] = 0;
                        } else if (board[to] == board[idx]) {
                            board[to] *= 2;
                            board[idx] = 0;
                            score += board[to];
                            merged = to;
                        } else{
                            if (to > idx) {
                                to--;
                                board[to] = board[idx];
                                if (to != idx)
                                    board[idx] = 0;
                            }
                        }
                    }
                }
                break;
            case 1:
                for (int j = 0; j<square; j++) {
                    int merged = Integer.MAX_VALUE;
                    for (int i = square-2; i >= 0; i--) {
                        int idx = i*square+j;
                        int to = idx+square;
                        while (board[to] == 0 && to < square*(square-1) && to + square < merged)
                            to+=square;
                        if (board[idx] == 0)
                            continue;
                        else if (board[to] == 0) {
                            board[to] = board[idx];
                            board[idx] = 0;
                        } else if (board[to] == board[idx]) {
                            board[to] *= 2;
                            board[idx] = 0;
                            score += board[to];
                            merged = to;
                        } else{
                            if (to > square-1) {
                                to -= square;
                                board[to] = board[idx];
                                if (to != idx)
                                    board[idx] = 0;
                            }
                        }
                    }
                }
                break;
            case 2:
                for (int i = 0; i < square; i++) {
                    int merged = Integer.MIN_VALUE;
                    for (int j = 1; j < square ; j++) {
                        int idx = i*square+j;
                        int to = idx-1;
                        while (board[to] == 0 && to > i*square && merged < to -1)
                            to--;
                        if (board[idx] == 0)
                            continue;
                        else if (board[to] == 0) {
                            board[to] = board[idx];
                            board[idx] = 0;
                        } else if (board[to] == board[idx]) {
                            board[to] *= 2;
                            board[idx] = 0;
                            score += board[to];
                            merged = to;
                        } else{
                            if (to < idx) {
                                to += 1;
                                board[to] = board[idx];
                                if (to != idx)
                                    board[idx] = 0;
                            }
                        }
                    }
                }
                break;
            case 3:
                for (int j = 0; j<square; j++) {
                    int merged = Integer.MIN_VALUE;
                    for (int i = 1; i < square; i++) {
                        int idx = i*square+j;
                        int to = idx-square;
                        while (board[to] == 0 && to > square-1 && merged < to - square)
                            to-=square;
                        if (board[idx] == 0)
                            continue;
                        else if (board[to] == 0) {
                            board[to] = board[idx];
                            board[idx] = 0;
                        } else if (board[to] == board[idx]) {
                            board[to] *= 2;
                            board[idx] = 0;
                            score += board[to];
                            merged = to;
                        } else {
                            if (to < square*(square-1)-1) {
                                to += square;
                                board[to] = board[idx];
                                if (to != idx)
                                    board[idx] = 0;
                            }
                        }
                    }
                }
                break;
        }
    }
    private boolean validMove(int dir){
        switch (dir){
            case 0:
                for (int i = 0; i < square; i++) {
                    for (int j = 0; j < square-1; j++) {
                        int idx = i*square+j;
                        if(board[idx] != 0 && (board[idx+1] == 0 || board[idx] == board[idx+1]))
                            return true;
                    }
                }
                return false;
            case 1:
                for (int i = 0; i < square-1; i++) {
                    for (int j = 0; j < square; j++) {
                        int idx = i*square+j;
                        if(board[idx] != 0 && (board[idx+square] == 0 || board[idx] == board[idx+square]))
                            return true;
                    }
                }
                return false;
            case 2:
                for (int i = 0; i < square; i++) {
                    for (int j = 1; j < square; j++) {
                        int idx = i*square+j;
                        if(board[idx] != 0 && (board[idx-1] == 0 || board[idx] == board[idx-1]))
                            return true;
                    }
                }
                return false;
            case 3:
                for (int i = 1; i < square; i++) {
                    for (int j = 0; j < square; j++) {
                        int idx = i*square+j;
                        if(board[idx] != 0 && (board[idx-square] == 0 || board[idx] == board[idx-square]))
                            return true;
                    }
                }
                return false;
        }
        return false;
    }
    private void addRandomTile(){
        int cnt = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) cnt++;
        }
        int tile = rand.nextInt(cnt);
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                if (tile == 0) {
                    if (rand.nextInt(100) < foursProbability)
                        board[i] = 4;
                    else
                        board[i] = 2;
                    break;
                } else
                    tile--;
            }
        }
    }
    private void printScore(){
        System.out.println("Score: " + score);
    }
    private void printBoard(){
        for (int i = 0; i < square; i++) {
            for (int j = 0; j < square; j++) {
                System.out.print("--------");
            }
            System.out.println('-');
            for (int j = 0; j < square; j++) {
                System.out.print("|       ");
            }
            System.out.println('|');
            for (int j = 0; j < square; j++) {
                System.out.print("|       ");
            }
            System.out.println('|');
            for (int j = 0; j < square; j++) {
                int x = board[i*square+j];
                System.out.print("| ");
                if (x != 0)
                    System.out.print(x);
                for (int k = 0; k < 5; k++) {
                    if (x == 0)
                        System.out.print(' ');
                    x/=10;
                }
                System.out.print(' ');
            }
            System.out.println('|');
            for (int j = 0; j < square; j++) {
                System.out.print("|       ");
            }
            System.out.println('|');
            for (int j = 0; j < square; j++) {
                System.out.print("|       ");
            }
            System.out.println('|');
        }
        for (int j = 0; j < square; j++) {
            System.out.print("--------");
        }
        System.out.println('-');
    }
}
