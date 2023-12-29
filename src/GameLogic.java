import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GameLogic {

    String word;
    ArrayList<Character> placeholder;
    ArrayList<Character> lettersInWord;
    ArrayList<Character> guessedLetters;
    int failCounter;
    static String alphabet = "abcdefghijklmnopqrstuvwxyz";
    static String alphabetGER = "abcdefghijklmnopqrstuvwxyzäöüß";
    ArrayList<String> gallows;

    public GameLogic() throws IOException {
        //gallows get set up
        String a = "_________";
        String b = "|/      |";
        String c = "|    (͡๏̯͡๏";
        String d = "|     /()\\";
        String e = "|    ./ \\.";
        String f = "|";
        String g = "|\\________";
        String h = " ";
        this.gallows = new ArrayList<>();
        gallows.add(a);
        gallows.add(b);
        gallows.add(c);
        gallows.add(d);
        gallows.add(e);
        gallows.add(f);
        gallows.add(g);
        gallows.add(h);

        //chose word
        this.word = GamePreparations.chooseWord(GamePreparations.createWordlist()).toUpperCase();
        //placeholder for word
        this.placeholder = GamePreparations.placeholder(word);
        //letters in word
        this.lettersInWord = lettersList(word);
        //guessed letters
        this.guessedLetters = new ArrayList<>();
        //fails
        this.failCounter = 0;
        //alphabet
        alphabet = alphabet;
    }

    public GameLogic(String german) throws IOException {
        //gallows get set up
        String a = "_________";
        String b = "|/      |";
        String c = "|    (͡๏̯͡๏";
        String d = "|     /()\\";
        String e = "|    ./ \\.";
        String f = "|";
        String g = "|\\________";
        String h = " ";
        this.gallows = new ArrayList<>();
        gallows.add(a);
        gallows.add(b);
        gallows.add(c);
        gallows.add(d);
        gallows.add(e);
        gallows.add(f);
        gallows.add(g);
        gallows.add(h);

        //chose word
        this.word = GamePreparations.chooseWord(GamePreparations.createWordlistGER()).toUpperCase();
        //placeholder for word
        this.placeholder = GamePreparations.placeholder(word);
        //letters in word
        this.lettersInWord = lettersList(word);
        //guessed letters
        this.guessedLetters = new ArrayList<>();
        //fails
        this.failCounter = 0;
        //alphabet
        alphabet=alphabetGER;
    }


    Character getUserInput(){
        Scanner userInput = new Scanner(System.in);

        System.out.println("\nPlease guess a letter.\n");
        String input = userInput.next();
        char userInputChar = input.charAt(0);

        //Check if input is letter
        ArrayList<Character> alphabetArray = new ArrayList<>();
        for (int i = 0; i < alphabet.length(); i++) {
            alphabetArray.add(alphabet.charAt(i));
        }
        if (alphabetArray.contains(input.charAt(0))){

            //check if letter was already guessed
            if (this.guessedLetters.contains(Character.toUpperCase(userInputChar))){
                System.out.println("\nYou have already guessed this letter!");
                //get new letter
                return(getUserInput());
            }

            //add letter to guessed letters
            else {
                this.guessedLetters.add(Character.toUpperCase(userInputChar));
                return Character.toUpperCase(userInputChar);
            }
        }
        else {
            return(getUserInput());
        }
    }

    private Boolean checkIfLetterInWord(Character userInput){
        for (int i = 0; i < this.word.length(); i++) {
            if (this.word.charAt(i)==userInput){
                return true;
            }
        }
        return false;
    }

    void processUserInput(Character userInput){
        if(checkIfLetterInWord(userInput)){
            replacePlaceholder(userInput);
        }

        else {
            this.failCounter++;
        }
    }

    private void replacePlaceholder(Character userInput) {
        for (int i = 0; i < this.word.length(); i++) {
            if(this.word.charAt(i)==userInput){
                this.placeholder.set(i, userInput);
            }
        }
    }

    void drawGallows() {
        System.out.println("\n-~°~-:_:-~°~-:_:-~°~-:_:-~°~-:_:-~°~-");
        for (int i = 0; i < this.failCounter; i++) {
            System.out.printf("%s \n", this.gallows.get(i));
        }
    }

    boolean checkLoss() throws IOException {
        if (this.failCounter == this.gallows.size()-1){
            System.out.println("\nYOU HAVE LOST!");
            drawGallows();
            System.out.printf("\nThe word you were looking for was %s", this.word);
            writeLoss();
            return false;
        }
        else return checkWin();
    }

    private boolean checkWin() throws IOException {
        int countCorrect = 0;
        for (Character character : this.lettersInWord) {
            if (this.guessedLetters.contains(character)) {
                countCorrect++;
            }
        }
        if (countCorrect==this.lettersInWord.size()){
            System.out.printf("\nThe word was %s", this.word.toUpperCase());
            System.out.println("\nYOU HAVE WON!");
            youWon();
            return false;
        }
        else {return true;}
    }


    private void youWon() throws IOException {
        BufferedWriter writeWinner = new BufferedWriter(new FileWriter("winners.txt", true));
        Scanner scanWinner = new Scanner(System.in);
        if (this.failCounter==0){
            System.out.println("\nFLAWLESS VICTORY!1!!");
        }
        System.out.println("\nCONGRATULATIONS! \nLeave your name:");
        writeWinner.newLine();
        String entry = scanWinner.next();
        entry += scanWinner.nextLine();
        writeWinner.append(entry.toUpperCase());
        writeWinner.append(String.format(" guessed the word: %s", this.word));
        if (this.failCounter==0){
            writeWinner.append(" || FLAWLESS ROUND!!1!");
        }
        writeWinner.close();
        System.out.println("\nDo you wanna see the records? [y/n]");
        char answer = scanWinner.next().charAt(0);
        if (answer == 'y') {
            showRecord();
        }
    }

    private void writeLoss() throws IOException {
        BufferedWriter writeLoser = new BufferedWriter(new FileWriter("winners.txt", true));
        System.out.println("\nBOO you lost! \nNo one takes credit for this...");
        Scanner scanLoser = new Scanner(System.in);
        writeLoser.newLine();
        writeLoser.append(String.format("No one took credit for: %s (Round was lost. BOO.)", this.word));
        writeLoser.close();
        System.out.println("\nDo you wanna see the records? [y/n]");
        char answer = scanLoser.next().charAt(0);
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

    private static ArrayList<Character> lettersList(String word){
        ArrayList<Character> lettersInWord = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (!lettersInWord.contains(word.charAt(i))){
                lettersInWord.add(word.charAt(i));
            }
        }
        return lettersInWord;
    }
}
