import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static ArrayList<Character> placeholder = new ArrayList<>();
    static ArrayList<Character> guessedWords = new ArrayList<>();

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

        ArrayList<Character> lettersInWord = lettersList(word);

        guessedWords.clear();
        placeholder = placeholder(word);

        int failCounter = 0;

        boolean playing = true;

        //PLAY
        while (playing){

            //System.out.println(word);


            for (Character character : placeholder) {
                System.out.print(character);
            }
            System.out.printf("\n%s", guessedWords);
            failCounter=processUserInput(getUserInput(alphabet), word, failCounter, gallows);
            playing = checkLoss(failCounter, gallows, word, lettersInWord);

        }
    }

    /**
     * Get a pool of words for the game
     * @return the list of potential words
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

    private static int processUserInput(Character userInput, String word, int failCounter, ArrayList<String> gallows){
        if(checkIfLetterInWord(userInput, word)){
            replacePlaceholder(userInput, word, placeholder);
        }

        else {
            failCounter++;
        }
        return drawGallows(failCounter, gallows);
    }

    private static void replacePlaceholder(Character userInput, String word, ArrayList<Character> placeholder) {
        for (int i = 0; i < word.length(); i++) {
            if(word.charAt(i)==userInput){
                placeholder.set(i, userInput);
            }
        }
    }

    private static int drawGallows(int failCounter, ArrayList<String> gallows) {
        System.out.println("\n-~°~-:_:-~°~-:_:-~°~-:_:-~°~-:_:-~°~-");
        for (int i = 0; i < failCounter; i++) {
            System.out.printf("%s \n", gallows.get(i));
        }
        return failCounter;
    }

    private static boolean checkLoss(int failcounter, ArrayList<String> gallows, String word, ArrayList<Character> lettersInWordd) throws IOException {
        if (failcounter == gallows.size()-1){
            System.out.println("\nYOU HAVE LOST!");
            System.out.printf("\nThe word you were looking for was %s", word);
            return askForPlayAgain();
        }
        else return checkWin(lettersInWordd, word);
    }

    private static boolean checkWin(ArrayList<Character> lettersInWord, String word) throws IOException {
        int countCorrect = 0;
        for (Character character : lettersInWord) {
            if (guessedWords.contains(character)) {
                countCorrect++;
            }
        }
        if (countCorrect==lettersInWord.size()){
            System.out.printf("\nThe word was %s", word.toUpperCase());
            System.out.println("\nYOU HAVE WON!");
            youWon(word);
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

    private static ArrayList<Character> lettersList(String word){
        ArrayList<Character> lettersInWord = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (!lettersInWord.contains(word.charAt(i))){
                lettersInWord.add(word.charAt(i));
            }
        }
        return lettersInWord;
    }

    private static void youWon(String word) throws IOException {
        BufferedWriter writeWinner = new BufferedWriter(new FileWriter("winners.txt", true));
        Scanner scanWinner = new Scanner(System.in);
        System.out.println("\nCONGRATULATIONS! \nLeave your name:");
        writeWinner.newLine();
        String entry = scanWinner.next();
        entry += scanWinner.nextLine();
        writeWinner.append(entry.toUpperCase());
        writeWinner.append(String.format(" guessed the word: %s", word));
        writeWinner.close();
        System.out.println("\nDo you wanna see the records? [y/n]");
        char answer = scanWinner.next().charAt(0);
        if (answer == 'y') {
            showRecord();
        }
    }

    private static void showRecord() throws IOException {
        BufferedReader records = new BufferedReader(new FileReader("winners.txt"));
        String line;
        while((line = records.readLine())!=null){
            System.out.printf("\n%s\n", line);
        }
    }
}