package dictionary.mydictionary.Model;

import java.io.*;
import java.util.*;

public class NewDictionary {
    private final String PATH;
    private static final String SPLITTING_PATTERN = "<html>";
    private final String HISTORY_PATH;
    private final String BOOKMARK_PATH;

    private final TreeMap<String, Word> newWords = new TreeMap<>();
    private final Map<String, Word> historyNewWords = new HashMap<>();
    private final Map<String, Word> bookmarkNewWords = new HashMap<>();

    public NewDictionary(String path, String historyPath, String bookmarkPath) {
        PATH = path;
        HISTORY_PATH = historyPath;
        BOOKMARK_PATH = bookmarkPath;
        loadDataFromHTMLFile(PATH, newWords);
        loadDataFromHTMLFile(HISTORY_PATH, historyNewWords);
        loadDataFromHTMLFile(BOOKMARK_PATH, bookmarkNewWords);
    }

    public String getPATH() {
        return PATH;
    }

    public String getHISTORY_PATH() {
        return HISTORY_PATH;
    }

    public String getBOOKMARK_PATH() {
        return BOOKMARK_PATH;
    }

    public TreeMap<String, Word> getNewWords() {
        return newWords;
    }

    public Map<String, Word> getHistoryNewWords() {
        return historyNewWords;
    }

    public Map<String, Word> getBookmarkNewWords() {
        return bookmarkNewWords;
    }

    public List<String> initKeys(Map<String, Word> temp){
        List<String> keys = new ArrayList<>();
        keys.addAll(temp.keySet());
        return keys;
    }

    public void loadDataFromHTMLFile(String path, Map<String, Word> temp) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(SPLITTING_PATTERN);
                String word = parts[0];
                String definition = SPLITTING_PATTERN + parts[1];
                Word wordObj = new Word(word, definition);
                temp.put(word, wordObj);
            }
            TreeMap<String, Word> sorted = new TreeMap<>(temp);
            temp = sorted;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDataFromHistoryFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(HISTORY_PATH));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(SPLITTING_PATTERN);
            String word = parts[0];
            String definition = SPLITTING_PATTERN + parts[1];
            Word wordObj = new Word(word, definition);
            historyNewWords.put(word, wordObj);
        }
    }

    public void exportWordToHTMLFile(String path, String word) throws IOException {
        File file = new File(path);
        FileWriter fileWriter = new FileWriter(file, true);

        int index = Collections.binarySearch(initKeys(getNewWords()), word);
        String selectedWord = initKeys(getNewWords()).get(index);
        String def = getNewWords().get(selectedWord).getDef();
        fileWriter.write(word + def + "\n");
        fileWriter.flush();
        fileWriter.close();
    }

    public void addWordToFile(String word, String def, String path) throws IOException {
        File file = new File(path);
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write(word + def + "\n");
        fileWriter.flush();
        fileWriter.close();
    }

    public void updateWordToFile(String path, Map<String, Word> temp) throws IOException {
        File file = new File(path);
        FileWriter fileWriter = new FileWriter(file);
        for (Map.Entry<String, Word> entry : temp.entrySet()) {
            fileWriter.write(entry.getValue().getWord() + entry.getValue().getDef() + "\n");
        }
        fileWriter.flush();
        fileWriter.close();
    }

    public boolean addWord(String word, String def) throws IOException {
        word = word.toLowerCase();
        int posAddWord = binaryCheck(0, newWords.size(), word);
        if (posAddWord == -1) {
            return false;
        }
        Word wordObj = new Word();
        wordObj.setWord(word);
        wordObj.setDef(def);
        newWords.put(word, wordObj);

        updateWordToFile(PATH, newWords);
        return true;
    }

    public void removeWord(String word, String path, Map<String, Word> temp) throws IOException {
        word = word.toLowerCase();
        int index = Collections.binarySearch(initKeys(getNewWords()), word);
        String removedKey = initKeys(getNewWords()).get(index);
        if (index >= 0) {
            temp.remove(removedKey);
        } else {
            return;
        }
        updateWordToFile(path, temp);
    }

    public void modifyWord(String word, String def) throws IOException {
        word = word.toLowerCase();
        def = def.toLowerCase();
        int pos = -1;
        pos = Collections.binarySearch(initKeys(getNewWords()), word);
        String selectedWord = initKeys(getNewWords()).get(pos);
        if (pos >= 0) {
            getNewWords().get(selectedWord).setDef(def);
        } else {
            System.out.println("Không tìm thấy từ bạn muốn sửa đổi!");
        }
        updateWordToFile(PATH, newWords);
    }

    public int binaryCheck(int start, int end, String word) {
        if (end < start) {
            return -1;
        }
        int mid = start + (end - start) / 2;
        int compareNext = word.compareTo(initKeys(getNewWords()).get(mid));
        if (mid == 0) {
            if (compareNext < 0) {
                return 0;
            } else if (compareNext > 0) {
                return binaryCheck(mid + 1, end, word);
            } else {
                return -1;
            }
        } else {
            int comparePrevious = word.compareTo(initKeys(getNewWords()).get(mid - 1));
            if (comparePrevious > 0 && compareNext < 0) {
                return mid;
            } else if (comparePrevious < 0) {
                return binaryCheck(start, mid - 1, word);
            } else if (compareNext > 0) {
                if (mid == getNewWords().size() - 1) {
                    return getNewWords().size();
                }
                return binaryCheck(mid + 1, end, word);
            } else {
                return -1;
            }
        }
    }

    public int binaryLookup(int start, int end, String word, Map<String, Word> temp) {
        List<String> tempKeys = new ArrayList<>(temp.keySet());
        if (end < start) {
            return -1;
        }
        int mid = start + (end - start) / 2;
        int compare = DictionaryManagement.isContain(word, tempKeys.get(mid));
        if (compare == -1) {
            return binaryLookup(start, mid - 1, word, temp);
        } else if (compare == 1) {
            return binaryLookup(mid + 1, end, word, temp);
        } else {
            return mid;
        }
    }
}