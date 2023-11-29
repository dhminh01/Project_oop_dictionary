package dictionary.mydictionary.Model;

import java.io.IOException;
import java.util.Map;

public class DictionaryCommandLine extends Dictionary {
    public static String showAllWords() {
        String ans = "";
        System.out.printf("%-6s%c %-15s%c %-20s%n","No", '|' ,"English", '|', "Vietnamese");
        int i = 0;
        for (Map.Entry<String, Word> entry : words.entrySet()){
            System.out.printf("%-6d%c %-15s%c %-15s%n", i += 1,'|', entry.getKey(), '|', entry.getValue().getDef());
        }
        return ans;
    }
    public static void main(String[] args) throws IOException {
        DictionaryManagement.insertFromFile();
        DictionaryManagement.dictionaryLookUp();
        System.out.println(showAllWords());
    }
}
