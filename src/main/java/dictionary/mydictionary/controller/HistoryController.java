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

public class HistoryController extends GeneralController{

    @FXML
    protected VBox historyContent;

    @FXML
    protected WebView historyDefinitionView;

    @FXML
    protected Button historyDeleteButton;

    @FXML
    protected Label historyLabel;

    @FXML
    protected ListView<String> historyListView;

    @FXML
    protected AnchorPane historyPane;

    @FXML
    protected VBox historySearch;

    @FXML
    protected TextField historyTextField;

    public void clearAllHistoryWord(){
        getCurrentDic().getHistoryNewWords().clear();
        try {
            getCurrentDic().updateWordToFile(getCurrentDic().getHISTORY_PATH(), getCurrentDic().getHistoryNewWords());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        historyListView.getItems().clear();
    }

    public void init(){
        setListView(this.historyListView);
        setDefinitionView(this.historyDefinitionView);
        setTextField(this.historyTextField);
        setLabel(this.historyLabel);
        initComponents(this.historyPane, getCurrentDic().getHistoryNewWords(), "#historyListView", "#historyDefinitionView");
        try {
            readData(getCurrentDic().getHISTORY_PATH(), getCurrentDic().getHistoryNewWords());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loadWordList(getCurrentDic().getHistoryNewWords());
        enterKeyPressed(getCurrentDic().getHistoryNewWords());
        textFieldInput(getCurrentDic().getHistoryNewWords());
    }
}