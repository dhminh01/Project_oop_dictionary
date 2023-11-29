package dictionary.mydictionary.controller;

import dictionary.mydictionary.Model.NewDictionary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.web.HTMLEditor;

import java.io.IOException;

public class AddWordController extends GeneralController{

    @FXML
    protected RadioButton addEV;

    @FXML
    protected HTMLEditor addEditor;

    @FXML
    protected TextField addText;

    @FXML
    protected RadioButton addVE;

    @FXML
    protected ToggleGroup data1;

    @FXML
    void add(ActionEvent event) throws IOException {
        String def = addEditor.getHtmlText().replace(" dir=\"ltr\"", "");
        if (addText.getText().equals("")) {
            showWarningAlert();
            return;
        }
        if (getDictionaryToAdd().addWord(addText.getText(), def)) {
            addReset();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Thêm từ thành công");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Từ bạn thêm đã tồn tại! Hãy chọn chức năng sửa đổi!");
            alert.showAndWait();
        }
    }

    @FXML
    public void addReset() {
        addEditor.setHtmlText("<html>" + addText.getText() + " /" + addText.getText() + "/"
                + "<ul><li><b><i> loại từ: </i></b><ul><li><font color='#cc0000'><b> Nghĩa thứ nhất: </b></font><ul></li></ul></ul></li></ul><ul><li><b><i>loại từ khác: </i></b><ul><li><font color='#cc0000'><b> Nghĩa thứ hai: </b></font></li></ul></li></ul></html>");
    }

    @FXML
    public void handleClickArrow() {
        if (addText.getText().equals("")) {
            showWarningAlert();
            return;
        }
        addEditor.setHtmlText("<html>" + addText.getText() + " /" + addText.getText() + "/"
                + "<ul><li><b><i> loại từ: </i></b><ul><li><font color='#cc0000'><b> Nghĩa thứ nhất: </b></font><ul></li></ul></ul></li></ul><ul><li><b><i>loại từ khác: </i></b><ul><li><font color='#cc0000'><b> Nghĩa thứ hai: </b></font></li></ul></li></ul></html>");
    }

    private NewDictionary getDictionaryToAdd() {
        if (addEV.isSelected()) {
            return evDic;
        } else {
            return veDic;
        }
    }

}