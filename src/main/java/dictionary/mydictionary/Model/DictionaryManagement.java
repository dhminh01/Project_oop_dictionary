package dictionary.mydictionary.Model;

import java.io.*;
import java.util.*;

public class DictionaryManagement extends Dictionary {
    private static final String IN_PATH = "src/main/resources/dictionary/myevdictionary/data/oldData/dictionaries.txt";
    private static final String OUT_PATH = "src/main/resource/dictionary/myevdictionary/data/oldData/dictionaries_out.txt";
    private static List<String> keys = new ArrayList<>(words.keySet());

    public static List<String> getKeys() {
        return keys;
    }

    public static void setKeys(List<String> keys) {
        DictionaryManagement.keys = keys;
    }

    public static void insertFromCommandLine() {
        Scanner intInput = new Scanner(System.in);
        Scanner stringInput = new Scanner(System.in);
        int wordsNum = intInput.nextInt();

        for (int i = 0; i < wordsNum; i++) {
            String word = stringInput.nextLine();
            String def = stringInput.nextLine();
            Word temp = new Word(word, def);
            words.put(word, temp);
        }
    }

    public static void insertFromFile() throws IOException {
        File file = new File(IN_PATH);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            String[] wordsInLine = line.split(",");
            Word temp = new Word(wordsInLine[0], wordsInLine[1]);
            words.put(wordsInLine[0], temp);
        }

        TreeMap<String, Word> sorted = new TreeMap<>(words);
        words = (Map<String, Word>) sorted;
    }

    public static void exportFromWordToFile() throws IOException {
        File file = new File(OUT_PATH);
        OutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

        String format = "%-15s %-15s%n";

        for (Map.Entry<String, Word> word : words.entrySet()) {
            bufferedWriter.write(String.format(format, word.getKey(), word.getValue().getDef()));
        }

        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public static void updateWordToFile() throws IOException {
        FileWriter fileWriter = new FileWriter(IN_PATH);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (Map.Entry<String, Word> word : words.entrySet()) {
            bufferedWriter.write(word.getKey() + "," + word.getValue().getDef() + "\n");
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public static void addWord(String word, String def) throws IOException {
        word = word.toLowerCase();
        def = def.toLowerCase();
        int posAddWord = binaryCheck(0, words.size(), word);
        if (posAddWord == -1) {
            System.out.println("Từ bạn thêm đã tồn tại. Hãy chọn chức năng sửa đổi!");
            return;
        }
        Word newWord = new Word();
        newWord.setWord(word);
        newWord.setDef(def);
        words.put(word,newWord);
        updateWordToFile();
    }

    public static void removeWord(String word) throws IOException {
        word = word.toLowerCase();
        int index = Collections.binarySearch(keys, word);
        String removedKey = keys.get(index);
        if (index >= 0) {
            words.remove(removedKey);
        } else {
            System.out.println("Từ bạn cần xoá không có trong từ điển!");
        }
        updateWordToFile();
    }

    public static void modifyWord(String word, String def) throws IOException {
        word = word.toLowerCase();
        def = def.toLowerCase();
        int pos = -1;
        pos = Collections.binarySearch(keys, word);
        if (pos >= 0) {
            words.get(word).setDef(def);
        } else {
            System.out.println("Không tìm thấy từ bạn muốn sửa đổi!");
        }
        updateWordToFile();
    }

    public static int isContain(String str1, String str2) {
        for (int i = 0; i < Math.min(str1.length(), str2.length()); i++) {
            if (str1.charAt(i) > str2.charAt(i)) {
                return 1;
            } else if (str1.charAt(i) < str2.charAt(i)) {
                return -1;
            }
        }
        if (str1.length() > str2.length()) {
            return 1;
        }
        return 0;
    }

    public static int binaryCheck(int start, int end, String word) {
        if (end < start) {
            return -1;
        }
        int mid = start + (end - start) / 2;
        int compareNext = word.compareTo(keys.get(mid));
        if (mid == 0) {
            if (compareNext < 0) {
                return 0;
            } else if (compareNext > 0) {
                return binaryCheck(mid + 1, end, word);
            } else {
                return -1;
            }
        } else {
            int comparePrevious = word.compareTo(keys.get(mid - 1));
            if (comparePrevious > 0 && compareNext < 0) {
                return mid;
            } else if (comparePrevious < 0) {
                return binaryCheck(start, mid - 1, word);
            } else if (compareNext > 0) {
                if (mid == words.size() - 1) {
                    return words.size();
                }
                return binaryCheck(mid + 1, end, word);
            } else {
                return -1;
            }
        }
    }

    public static int binaryLookup(int start, int end, String word) {
        if (end < start) {
            return -1;
        }
        int mid = start + (end - start) / 2;
        int compare = isContain(word, keys.get(mid));
        if (compare == -1) {
            return binaryLookup(start, mid - 1, word);
        } else if (compare == 1) {
            return binaryLookup(mid + 1, end, word);
        } else {
            return mid;
        }
    }

    public static void showWordLookup(String word, int index) {
        if (index < 0) {
            return;
        }
        ArrayList<Word> listWordSearching = new ArrayList<Word>();
        int j = index;
        while (j >= 0) {
            if (isContain(word, keys.get(j)) == 0) {
                j--;
            } else {
                break;
            }
        }
        for (int i = j + 1; i <= index; i++) {
            Word temp = new Word(keys.get(i), words.get(keys.get(i)).getDef());
            listWordSearching.add(temp);
        }

        for (int i = index + 1; i < words.size(); i++) {
            if (isContain(word, keys.get(i)) == 0) {
                Word temp = new Word(keys.get(i), words.get(keys.get(i)).getDef());
                listWordSearching.add(temp);
            } else {
                break;
            }
        }
        for (Word wordSearching : listWordSearching) {
            System.out.println(wordSearching.getWord());
        }
    }

    public static void dictionaryLookUp() throws IOException {
        Scanner getInput = new Scanner(System.in);
        String word = getInput.nextLine().toLowerCase();
        int index = binaryLookup(0, words.size(), word);
        if (index < 0) {
//            Spelling corrector = new Spelling("src/resource/vocab/spelling.txt");
//            word = corrector.correct(word);
            index = binaryLookup(0, words.size(), word);
        }
        showWordLookup(word, index);
    }
}