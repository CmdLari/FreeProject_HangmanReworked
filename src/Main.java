import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        gameloop();


    }
    public static void gameloop() throws IOException {
        GameLogic game = new GameLogic();
        if (!playgame(game)){
            if (askForPlayAgain()){
                gameloop();
            }
        }
    }

    public static boolean playgame(GameLogic game) throws IOException {
        boolean playing = true;
        while(playing) {
            game.drawGallows();
            System.out.println("\n");
            for (Character character : game.placeholder) {
                System.out.print(character);
            }
            System.out.printf("\n%s", game.guessedLetters);
            char input = game.getUserInput();
            game.processUserInput(input);
            playing = (game.checkLoss());
        }
        return false;
    }
    private static boolean askForPlayAgain() {
        Scanner playAgain = new Scanner(System.in);
        System.out.println("\nDO YOU WANT TO PLAY AGAIN? [y/n]\n");
        String input = playAgain.next().toLowerCase();
        if (input.charAt(0)=='n') {
            return false;}
        else if (input.charAt(0)=='y') {
            return true;}
        else {
            askForPlayAgain();
        }
        return false;
    }

}
