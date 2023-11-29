package dictionary.mydictionary.controller;

import dictionary.mydictionary.Model.DictionaryManagement;
import dictionary.mydictionary.Model.NewDictionary;
import dictionary.mydictionary.Model.TextToSpeech;
import dictionary.mydictionary.Model.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class GeneralController extends MainController implements Initializable {
    private static final String EV_FILE_PATH = "src/main/resources/dictionary/mydictionary/data/E_V.txt";
    private static final String EV_HISTORY_PATH = "src/main/resources/dictionary/mydictionary/data/E_V_Recent.txt";
    private static final String EV_BOOKMARK_PATH = "src/main/resources/dictionary/mydictionary/data/E_V_Bookmark.txt";
    private static final String VE_FILE_PATH = "src/main/resources/dictionary/mydictionary/data/V_E.txt";
    private static final String VE_HISTORY_PATH = "src/main/resources/dictionary/mydictionary/data/V_E_Recent.txt";
    private static final String VE_BOOKMARK_PATH = "src/main/resources/dictionary/mydictionary/data/V_E_Bookmark.txt";

    protected final ObservableMap<String, String> searchMap = FXCollections.observableHashMap();
    @FXML
    protected Label searchLabel;
    @FXML
    protected AnchorPane searchPane;

    @FXML
    protected Button transLangEV;

    @FXML
    protected Button transLangVE;
    @FXML
    protected TextField searchTextField;
    @FXML
    protected ListView<String> listView;
    @FXML
    private WebView definitionView;
    @FXML
    private TextField textField;
    @FXML
    private Label label;

    protected static NewDictionary evDic = new NewDictionary(EV_FILE_PATH, EV_HISTORY_PATH, EV_BOOKMARK_PATH);

    protected static NewDictionary veDic = new NewDictionary(VE_FILE_PATH, VE_HISTORY_PATH, VE_BOOKMARK_PATH);
    public static final String SPLITTING_CHARACTERS = "<html>";

    private static boolean isEVDic = true;

    public void setListView(ListView<String> listView) {
        this.listView = listView;
    }

    public void setDefinitionView(WebView definitionView) {
        this.definitionView = definitionView;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public void initComponents(AnchorPane view, Map<String, Word> temp, String searchListViewId, String definitionViewId) {
        this.definitionView = (WebView) view.lookup(definitionViewId);
        this.listView = (ListView<String>) view.lookup(searchListViewId);
        this.listView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        Word word = temp.get(newValue.trim());
                        if (word != null) {
                            String definition = word.getDef();
                            this.definitionView.getEngine().loadContent(definition, "text/html");
                            label.setText(word.getWord());
                        } else {
                            // Handle the case where the Word object is null
                            System.out.println("Selected word is null");
                        }
                    } else {
                        // Handle the case where newValue is null
                        System.out.println("Selected value is null");
                    }
                }
        );
    }

    public void loadWordList(Map<String, Word> temp) {
        this.listView.getItems().addAll(temp.keySet());
    }

    public void readData(String path, Map<String, Word> temp) throws IOException {
        FileReader fis = new FileReader(path);
        BufferedReader br = new BufferedReader(fis);
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(SPLITTING_CHARACTERS);
            String word = parts[0];
            String definition = SPLITTING_CHARACTERS + parts[1];
            Word wordObj = new Word(word, definition);
            temp.put(word, wordObj);
        }
        TreeMap<String, Word> sorted = new TreeMap<>(temp);
        temp.clear();
        temp.putAll(sorted);
    }

    private boolean isContain(String str1, String str2){
        if (DictionaryManagement.isContain(str1, str2) >= 0){
            return true;
        }
        return false;
    }

    public Set<String> searching(String searchWords, Map<String, Word> temp) {
        Set<String> wordSet = new TreeSet<>(temp.keySet());
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return wordSet.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word -> isContain(word.toLowerCase(), input.toLowerCase()));
        }).collect(Collectors.toSet());
    }

    public void enterKeyPressed(Map<String, Word> temp) {
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (textField.getText() == null) {
                    loadWordList(temp);
                }
                this.listView.getItems().clear();
                this.listView.getItems().addAll(searching(textField.getText(), temp));
            }
        });
    }

    public void textFieldInput(Map<String, Word> temp) {
        textField.setOnKeyPressed(event -> {
            if (textField.getText() == null) {
                loadWordList(temp);
            }
            this.listView.getItems().clear();
            this.listView.getItems().addAll(searching(textField.getText(), temp));
        });
    }

    public void pressedSpeaker() {
        if (isEVDic) {
            TextToSpeech.playSoundGoogleTranslateEnToVi(searchLabel.getText());
        } else {
            TextToSpeech.playSoundGoogleTranslateViToEn(searchLabel.getText());
        }
    }

    public void setLanguage() {
        if (!isEVDic) {
            transLangEV.setVisible(false);
            transLangVE.setVisible(true);
        } else {
            transLangEV.setVisible(true);
            transLangVE.setVisible(false);
        }
    }

    @FXML
    public void clickTransBtn() throws IOException {
        isEVDic = !isEVDic;
        setLanguage();
        this.listView.getItems().clear();
        loadWordList(getCurrentDic().getNewWords());
    }

    public NewDictionary getCurrentDic() {
        if (isEVDic)
            return evDic;
        else return veDic;
    }

    public void saveWordToFile(String path, Map<String, Word> temp, String word, String def) throws IOException {
        int index = Collections.binarySearch(getCurrentDic().initKeys(temp), word);
        if (index >= 0) {
            temp.get(word).setDef(def);
            getCurrentDic().updateWordToFile(path, temp);
        }
    }

    public void showWarningAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Không có từ nào được chọn!");
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}