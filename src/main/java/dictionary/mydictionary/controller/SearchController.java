package dictionary.mydictionary.controller;


import dictionary.mydictionary.Model.TextToSpeech;
import dictionary.mydictionary.Model.Word;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.util.*;


public class SearchController extends GeneralController implements Initializable {

    @FXML
    protected Label searchLabel;

    @FXML
    protected VBox searchContent;

    @FXML
    protected AnchorPane searchPane;

    @FXML
    protected ListView<String> searchListView;

    @FXML
    protected VBox search;

    @FXML
    protected TextField searchTextField;

    @FXML
    protected WebView searchDefinitionView;

    @FXML
    protected HTMLEditor editDefinition;

    @FXML
    protected Button bookmarkFalseButton;
    @FXML
    protected Button bookmarkTrueButton;
    @FXML
    protected Button deleteButton;
    @FXML
    protected Button editButton;
    @FXML
    protected Button saveChangeButton;

    protected boolean isOnEditDefinition = false;


    public void addBookmark(Word word) throws IOException {
        bookmarkFalseButton.setVisible(false);
        bookmarkTrueButton.setVisible(true);
        getCurrentDic().getBookmarkNewWords().put(word.getWord(), word);
        getCurrentDic().addWordToFile(word.getWord(), word.getDef(), getCurrentDic().getBOOKMARK_PATH());
    }

    protected void removeBookmark(Word word) throws IOException {
        bookmarkFalseButton.setVisible(true);
        bookmarkTrueButton.setVisible(false);
        getCurrentDic().getBookmarkNewWords().remove(word.getWord(), word);
        getCurrentDic().updateWordToFile(getCurrentDic().getBOOKMARK_PATH(), getCurrentDic().getBookmarkNewWords());
    }

    @FXML
    public void handleClickBookmarkButton() throws IOException {
        String word = searchLabel.getText();
        if (word.equals("")) {
            showWarningAlert();
            return;
        }
        int _index = Collections.binarySearch(getCurrentDic().initKeys(getCurrentDic().getNewWords()), word);
        int index = Collections.binarySearch(getCurrentDic().initKeys(getCurrentDic().getBookmarkNewWords()), word);
        if (index < 0) {
            addBookmark(getCurrentDic().getNewWords().get(getCurrentDic().initKeys(getCurrentDic().getNewWords()).get(Math.abs(_index))));
        } else {
            removeBookmark(getCurrentDic().getBookmarkNewWords().get(getCurrentDic().initKeys(getCurrentDic().getBookmarkNewWords()).get(index)));
        }
    }

    public void pressedSpeaker(){
        TextToSpeech.playSoundGoogleTranslateEnToVi(searchLabel.getText());
    }

    @FXML
    public void pressedEdit() {
        String word = searchLabel.getText();
        if (word.equals("")) {
            showWarningAlert();
            return;
        }
        if (isOnEditDefinition) {
            isOnEditDefinition = false;
            editDefinition.setVisible(false);
            saveChangeButton.setVisible(false);
            return;
        }
        isOnEditDefinition = true;
        saveChangeButton.setVisible(true);
        editDefinition.setVisible(true);
        String def = getCurrentDic().getNewWords().get(word).getDef();
        editDefinition.setHtmlText(def);
    }

    @FXML
    public void pressedSave() throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Sửa từ thành công!");
        alert.showAndWait();
        editDefinition.setVisible(false);
        isOnEditDefinition = false;
        saveChangeButton.setVisible(false);
        String newMeaning = editDefinition.getHtmlText().replace(" dir=\"ltr\"", "");
        String spelling = searchLabel.getText();
        saveWordToFile(getCurrentDic().getPATH(), getCurrentDic().getNewWords(), spelling, newMeaning);
        saveWordToFile(getCurrentDic().getHISTORY_PATH(), getCurrentDic().getHistoryNewWords(), spelling, newMeaning);
        saveWordToFile(getCurrentDic().getBOOKMARK_PATH(), getCurrentDic().getBookmarkNewWords(), spelling, newMeaning);
        searchDefinitionView.getEngine().loadContent(newMeaning, "text/html");
    }

    @FXML
    public void pressedDelete() throws IOException {
        String spelling = searchTextField.getText();
        if (spelling.equals("")) {
            searchController.showWarningAlert();
            return;
        }
        ButtonType yes = new ButtonType("Có", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Không", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xoá từ này không?", yes, no);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.showAndWait();

        if (alert.getResult() == yes) {
            getCurrentDic().removeWord(spelling, getCurrentDic().getPATH(), getCurrentDic().getNewWords());
            getCurrentDic().removeWord(spelling, getCurrentDic().getHISTORY_PATH(), getCurrentDic().getHistoryNewWords());
            getCurrentDic().removeWord(spelling, getCurrentDic().getBOOKMARK_PATH(), getCurrentDic().getBookmarkNewWords());
            searchLabel.setText("Nghĩa của từ");
            searchTextField.clear();
            searchDefinitionView.getEngine().loadContent("");
        }
    }

    public void init(){
        setListView(this.searchListView);
        setDefinitionView(this.searchDefinitionView);
        setTextField(this.searchTextField);
        setLabel(this.searchLabel);
        initComponents(this.searchPane, getCurrentDic().getNewWords(), "#searchListView", "#searchDefinitionView");
        try {
            readData(getCurrentDic().getPATH(), getCurrentDic().getNewWords());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loadWordList(getCurrentDic().getNewWords());
        enterKeyPressed(getCurrentDic().getNewWords());
        textFieldInput(getCurrentDic().getNewWords());
        List<String> keys = new ArrayList<>();
        this.searchListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String word = searchListView.getSelectionModel().getSelectedItem();
                String def = getCurrentDic().getNewWords().get(word).getDef();
                Word temp = new Word(word, def);
                getCurrentDic().getHistoryNewWords().put(word, temp);
                keys.add(word);

                if (keys.size() > 22) {
                    String lastWordInHistory = keys.get(0);
                    getCurrentDic().getHistoryNewWords().remove(lastWordInHistory);
                    keys.remove(0);
                }
                try {
                    getCurrentDic().updateWordToFile(getCurrentDic().getHISTORY_PATH(), getCurrentDic().getHistoryNewWords());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}