package dictionary.ui.controller;

import dictionary.server.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.*;

import static dictionary.server.Dictionary.words;

public class HomeViewController implements Initializable {
    private static final String DATA_FILE_PATH = "src/main/resources/dictionary/myevdictionary/data/E_V.txt";

    private static final String SPLITTING_CHARACTERS = "<html>";

    @FXML
    protected HBox content;

    @FXML
    protected AnchorPane homeViewPane;

    @FXML
    protected ListView<String> listView;

    @FXML
    protected HBox search;

    @FXML
    protected TextField searchbar;

    @FXML
    protected Button searchbtn;

    @FXML
    protected VBox vboxHome;

    @FXML
    protected WebView definitionView;

    public AnchorPane getHomeViewPane() {
        return homeViewPane;
    }

    public void setHomeViewPane(AnchorPane homeViewPane) {
        this.homeViewPane = homeViewPane;
    }

    @FXML
    public void search(MouseEvent event) {
        this.listView.getItems().clear();
//        this.listView.getItems().addAll(searchList(searchbar.getText(), words));
    }

//    private Set<Map<String, Word>> searchList(String text)

    public void initComponents(AnchorPane view) {
        this.definitionView = (WebView) view.lookup("#definitionView");
        this.listView = (ListView<String>) view.lookup("#listView");
//        HomeViewController context = this;
        this.listView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    Word selectedWord = words.get(newValue.trim());
                    String definition = selectedWord.getDef();
                    this.definitionView.getEngine().loadContent(definition, "text/html");
                }
        );
    }

    public void loadWordList() {
        this.listView.getItems().addAll(words.keySet());
    }

    public void readData() throws IOException {
        FileReader fis = new FileReader(DATA_FILE_PATH);
        BufferedReader br = new BufferedReader(fis);
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(SPLITTING_CHARACTERS);
            String word = parts[0];
            String definition = SPLITTING_CHARACTERS + parts[1];
            Word wordObj = new Word(word, definition);
            words.put(word, wordObj);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComponents(homeViewPane);

        try {
            readData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loadWordList();
    }


}
