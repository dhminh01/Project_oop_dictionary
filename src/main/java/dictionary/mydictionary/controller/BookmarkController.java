package dictionary.mydictionary.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.io.IOException;

public class BookmarkController extends GeneralController{

    @FXML
    protected VBox bookmarkContent;

    @FXML
    protected ListView<String> bookmarkListView;

    @FXML
    protected AnchorPane bookmarkPane;

    @FXML
    protected VBox bookmarkSearch;

    @FXML
    protected TextField bookmarkTextField;

    @FXML
    protected WebView bookmarkDefinitionView;

    @FXML
    protected Button bookmarkDeleteButton;

    @FXML
    protected Label bookmarkLabel;

    public void clearAllBookmarkWord(){
        getCurrentDic().getBookmarkNewWords().clear();
        try {
            getCurrentDic().updateWordToFile(getCurrentDic().getBOOKMARK_PATH(), getCurrentDic().getBookmarkNewWords());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bookmarkListView.getItems().clear();
    }

    public void init(){
        setListView(this.bookmarkListView);
        setDefinitionView(this.bookmarkDefinitionView);
        setTextField(this.bookmarkTextField);
        setLabel(this.bookmarkLabel);
        initComponents(this.bookmarkPane, getCurrentDic().getBookmarkNewWords(), "#bookmarkListView", "#bookmarkDefinitionView");
        try {
            readData(getCurrentDic().getBOOKMARK_PATH(), getCurrentDic().getBookmarkNewWords());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loadWordList(getCurrentDic().getBookmarkNewWords());
        enterKeyPressed(getCurrentDic().getBookmarkNewWords());
        textFieldInput(getCurrentDic().getBookmarkNewWords());
    }
}