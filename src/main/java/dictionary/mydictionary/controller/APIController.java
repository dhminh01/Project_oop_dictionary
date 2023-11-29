package dictionary.mydictionary.controller;

import dictionary.mydictionary.Model.TextToSpeech;
import dictionary.mydictionary.Model.TranslateAPI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class APIController implements Initializable {
    private boolean errorOccurred = false;
    String langFrom = "";
    String langTo = "vi";
    @FXML
    protected AnchorPane APIPane;
    @FXML
    protected VBox APIContent1;
    @FXML
    protected VBox APIContent2;
    @FXML
    protected HBox APIHbox1;
    @FXML
    protected HBox APIHbox2;
    @FXML
    protected HBox APIHbox3;
    @FXML
    protected HBox APIHbox4;
    @FXML
    protected HBox APIHbox5;
    @FXML
    protected HBox APIHbox6;
    @FXML
    protected Button googlebtn;
    @FXML
    protected Button langFromEng;
    @FXML
    protected Button langFromVie;
    @FXML
    protected Button langToEng;
    @FXML
    protected Button langToVie;
    @FXML
    protected Button translateBtn;
    @FXML
    protected Button helperBtn;
    @FXML
    TextArea srcText;
    @FXML
    TextArea destText;
    @FXML
    TextField text1;
    @FXML
    TextField text2;

    public AnchorPane getAPIViewPane() {
        return APIPane;
    }

    public void setAPIViewPane(AnchorPane APIPane) {
        this.APIPane = APIPane;
    }

    public APIController() {
    }

    public void resetStyleLangFrom() {
        langFromEng.getStyleClass().removeAll("active");
        langFromVie.getStyleClass().removeAll("active");
    }

    @FXML
    public void eng1() {
        resetStyleLangFrom();
        langFromEng.getStyleClass().add("active");
        langFrom = "en";
        text1.setText("English");
        if (langFrom.equals(langTo)) {
            showLanguageError(); // Hiển thị thông báo lỗi ngôn ngữ
        } else {
            hideLanguageError(); // Ẩn thông báo lỗi ngôn ngữ
        }
    }

    @FXML
    public void vie1() {
        resetStyleLangFrom();
        langFromVie.getStyleClass().add("active");
        langFrom = "vi";
        text1.setText("Tiếng Việt");
        if (langFrom.equals(langTo)) {
            showLanguageError(); // Hiển thị thông báo lỗi ngôn ngữ
        } else {
            hideLanguageError(); // Ẩn thông báo lỗi ngôn ngữ
        }
    }

    public void resetStyleLangTo() {
        langToEng.getStyleClass().removeAll("active");
        langToVie.getStyleClass().removeAll("active");
    }

    @FXML
    public void vie2() throws IOException {
        resetStyleLangTo();
        langToVie.getStyleClass().add("active");
        text2.setText("Tiếng Việt");
        langTo = "vi";
        if (!Objects.equals(srcText.getText(), "")) {
            destText.setPromptText("");
            destText.setStyle("");
            destText.setText(TranslateAPI.translate(langFrom, langTo, srcText.getText()));
            errorOccurred = false;
        } else if (langFrom.equals(langTo)) {
            showLanguageError(); // Hiển thị thông báo lỗi ngôn ngữ
        } else {
            hideLanguageError(); // Ẩn thông báo lỗi ngôn ngữ
        }
    }

    @FXML
    public void eng2() throws IOException {
        resetStyleLangTo();
        langToEng.getStyleClass().add("active");
        text2.setText("English");
        langTo = "en";
        if (!Objects.equals(srcText.getText(), "")) {
            destText.setPromptText("");
            destText.setStyle("");
            destText.setText(TranslateAPI.translate(langFrom, langTo, srcText.getText()));
            errorOccurred = false;
        } else if (langFrom.equals(langTo)) {
            showLanguageError(); // Hiển thị thông báo lỗi ngôn ngữ
        } else {
            hideLanguageError(); // Ẩn thông báo lỗi ngôn ngữ
        }
    }

    @FXML
    void translate() throws IOException {
        if (!Objects.equals(srcText.getText(), "")) {
            destText.setText(TranslateAPI.translate(langFrom, langTo, srcText.getText()));
        }
    }

    /**
     * Show error notification if user choose 2 same languages to translate.
     */
    void showLanguageError() {
        destText.setText("");
        destText.setStyle("-fx-text-fill: red;");
        destText.setText("Vui lòng chọn ngôn ngữ khác!");
        srcText.setVisible(true);
        errorOccurred = true;
    }
    /**
     * Hide error notification if user choose 2 different languages to translate.
     */
    void hideLanguageError() {
        destText.setText("");
        destText.setStyle("");
        destText.setPromptText("");
        errorOccurred = false;
    }
    @FXML
    public void speak1() throws IOException{
        if ("vi".equals(langFrom)) {
            TextToSpeech.playSoundGoogleTranslateViToEn(srcText.getText());
        } else {
            TextToSpeech.playSoundGoogleTranslateEnToVi(srcText.getText());
        }
    }
    public void speak2(){
        if ("eng".equals(langTo)) {
            TextToSpeech.playSoundGoogleTranslateEnToVi(destText.getText());
        } else {
            TextToSpeech.playSoundGoogleTranslateViToEn(destText.getText());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        langFromVie.getStyleClass().add("active");
        langToEng.getStyleClass().add("active");

    }
}
