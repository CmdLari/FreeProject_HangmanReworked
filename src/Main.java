import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static ArrayList<Character> placeholder = new ArrayList<>();
    static ArrayList<Character> guessedWords = new ArrayList<>();

    //Logic
    //Chose word from List
    //Draw lines
    //Get user input
    //Check if Letter is in word
    //if not: draw part of the gallows
    //if: show lines with letter
    //print list of guessed letters
    //check if word complete
    //check if gallow complete
    //ask for new round
    public static void main(String[] args) throws IOException {



        runGame();

        }

    private static void runGame() throws IOException {

        //Prepare the game
        String word = choseWord(createWordlist()).toUpperCase();
        ArrayList<Character> alphabet = new ArrayList<>();
        String alphabetString = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < alphabetString.length(); i++) {
            alphabet.add(alphabetString.charAt(i));
        }
        String a = "_________";
        String b = "|/      |";
        String c = "|       O";
        String d = "|      /|\\";
        String e = "|      / \\";
        String f = "|";
        String g = "__________";
        String h = " ";

        ArrayList<String> gallows = new ArrayList<>();
        gallows.add(a);
        gallows.add(b);
        gallows.add(c);
        gallows.add(d);
        gallows.add(e);
        gallows.add(f);
        gallows.add(g);
        gallows.add(h);

        guessedWords.clear();
        placeholder = placeholder(word);

        int failcounter = 0;

        boolean playing = true;

        //PLAY
        while (playing){

            //System.out.println(word);

            for (Character character : placeholder) {
                System.out.print(character);
            }
            failcounter=processUserInput(getUserInput(alphabet), word, failcounter, gallows);
            System.out.println(guessedWords);
            playing = checkLoss(failcounter, gallows, word);
        }
    }

    /**
     * Get a pool of words for the game
     * @return the list of potenntial words
     */
    private static ArrayList<String> createWordlist() throws IOException {

        BufferedReader readWordsFile = new BufferedReader(new FileReader("wordsource.txt"));
        ArrayList<String> wordList = new ArrayList<>();
        String line;
        while ((line = readWordsFile.readLine()) != null){
            wordList.add(line);
        }

        return wordList;
    }

    /**
     * Pick a word for the game
     * @param wordList - the pool of words
     * @return the chosen word
     */
    private static String choseWord(ArrayList<String>wordList) {
        Random random = new Random();
        return wordList.get(random.nextInt(wordList.toArray().length-1));
    }

    private static ArrayList<Character> placeholder(String word){
        ArrayList<Character> placeholder = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            placeholder.add('_');
        }
        return placeholder;
    }

    private static Character getUserInput(ArrayList<Character> alphabet){
        Scanner userInput = new Scanner(System.in);

        System.out.println("\nPlease guess a letter.\n");
        String input = userInput.next();
        char userinput = input.charAt(0);
        if (alphabet.contains(input.charAt(0))){
            if (guessedWords.contains(Character.toUpperCase(userinput))){
                System.out.println("\nYou have already guessed this letter!");
                getUserInput(alphabet);
            }
            else {
                guessedWords.add(Character.toUpperCase(userinput));
            }
            return Character.toUpperCase(userinput);
        }
        else {
            getUserInput(alphabet);
        }
        return Character.toUpperCase(userinput);
    }

    private static Boolean checkIfLetterInWord(Character userInput, String word){
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i)==userInput){
                return true;
            }
        }
        return false;
    }

    private static int processUserInput(Character userInput, String word, int failcounter, ArrayList<String> gallows){
        if(checkIfLetterInWord(userInput, word)){
            replacePlaceholder(userInput, word, placeholder);
        }

        else {
            return drawGallows(failcounter, gallows);
        }

        return failcounter;
    }

    private static void replacePlaceholder(Character userInput, String word, ArrayList<Character> placeholder) {
        for (int i = 0; i < word.length(); i++) {
            if(word.charAt(i)==userInput){
                placeholder.set(i, userInput);
            }
        }
    }

    private static int drawGallows(int failcounter, ArrayList<String> gallows) {
        for (int i = 0; i <= failcounter; i++) {
            System.out.printf("%s \n", gallows.get(i));
        }
        failcounter++;
        return failcounter;
    }

    private static boolean checkLoss(int failcounter, ArrayList<String> gallows, String word) throws IOException {
        if (failcounter == gallows.size()-1){
            System.out.println("\nYOU HAVE LOST!");
            System.out.printf("\nThe word you were looking for was %s", word);
            return askForPlayAgain();
        }
        else return checkWin(word);
    }

    private static boolean checkWin(String word) throws IOException {
        int countCorrect = 0;
        for (int i = 0; i < word.length()-1; i++) {
            if (guessedWords.contains(word.charAt(i))){
                countCorrect++;
            }
        }
        if (countCorrect==word.length()){
            System.out.println("\nYOU HAVE WON!");
            return askForPlayAgain();
        }
        return true;
    }


    private static boolean askForPlayAgain() throws IOException {
        Scanner playagain = new Scanner(System.in);
        System.out.println("\nDO YOU WANT TO PLAY AGAIN? [y/n]\n");
        char input = playagain.next().charAt(0);
        if (input=='y') {
            runGame();
        } else if (input=='n') {
            return false;
        }
        else {
            askForPlayAgain();
        }
        return true;
    }
}