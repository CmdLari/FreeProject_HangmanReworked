import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GamePreparations {

    static GameLogic choseLanguage() throws IOException {
        Scanner choseLang = new Scanner(System.in);
        System.out.println("\nDo you wanna get a German or English word? [g/e]");
        String answer = choseLang.next();
        if(Character.toLowerCase(answer.charAt(0))=='g'){
            return new GameLogic("german");
        } else if (Character.toLowerCase(answer.charAt(0))=='e') {
            return new GameLogic();
        }
        else return choseLanguage();
    }

    static ArrayList<String> createWordlistGER() throws IOException {
        BufferedReader readWordsFile = new BufferedReader(new FileReader("wordsource_GER.txt"));
        ArrayList<String> wordList = new ArrayList<>();
        String line;
        while ((line = readWordsFile.readLine()) != null){
            wordList.add(line);
        }

        return wordList;
    }

    /**
     * Get a pool of words for the game
     * @return the list of potential words
     */
    static ArrayList<String> createWordlist() throws IOException {

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
    static String chooseWord(ArrayList<String> wordList) {
        Random random = new Random();
        return wordList.get(random.nextInt(wordList.toArray().length-1));
    }

    /**
     * create the placeholder
     * @param word - the word to be guessed
     * @return the placeholder array
     */
    static ArrayList<Character> placeholder(String word){
        ArrayList<Character> placeholder = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            placeholder.add('_');
        }
        return placeholder;
    }

}
