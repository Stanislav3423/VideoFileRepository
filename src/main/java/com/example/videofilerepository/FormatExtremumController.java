package com.example.videofilerepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

/*
 * Контролер віка знаходження мінімального та максимального значення
 */
public class FormatExtremumController implements Initializable {
    // Оголошення елментів вікна
    @FXML
    private ChoiceBox<String> formatsChoice;

    // Можливі варіанти вибору формату
    private String[] formatsStr = {"MP4", "MOV", "FLV", "WMV"};

    // Оголошення списків та контролера головного вікна
    private FileListManager videoFileList;
    private Controller mainController;
    public FileListManager extremumList;
    private String format;

    // Початкова ініціалізація значень полів та елементів вікна
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formatsChoice.getItems().addAll(formatsStr);
    }

    // Сетери для списків та контролерів
    public void setVideoFileList(FileListManager fileManager) {
        this.videoFileList = fileManager;
    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    public void setExtremumList(FileListManager extremumList) {
        this.extremumList = extremumList;
    }

    // Метод вибору формату (результат натиску на кнопу Choose)
    @FXML
    void onChooseButtonClick(ActionEvent event) {
        if (videoFileList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "List is empty");
            return;
        }
        String selectedFormat = formatsChoice.getValue();
        if (selectedFormat != null) {
            format = selectedFormat;
            mainController.extremumFormatTF.setText(format);
        } else {
            mainController.showErrorAlert("Choose Error", "Choose format");
        }
    }

    // Метод групування елеменнтів за вибраним форматом (результат натиску на кнопу Group)
    @FXML
    void onGroupButtonClick(ActionEvent event) {
        if (format == null) {
            mainController.showErrorAlert("Choose Error", "Choose format");
            return;
        }
        if (videoFileList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "List is empty");
            return;
        }
        formatsChoice.setValue(null);
        extremumList = videoFileList.groupByFormat(format);
        mainController.updateContextTableView(extremumList);
        mainController.setExtremumList(extremumList);
    }

    // Метод знаходження списку з мінімального та максимального значення (результат натиску на кнопу Find min/max)
    @FXML
    void onFindButtonClick(ActionEvent event) {
        if (videoFileList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "List is empty");
            return;
        }
        if (extremumList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "Group is empty");
            return;
        }
        extremumList = extremumList.findMinMaxSize();
        mainController.updateContextTableView(extremumList);
        mainController.maxFormatTF.setText(extremumList.getList().get(0).getSize() + "");
        mainController.minFormatTF.setText(extremumList.getList().get(1).getSize() + "");
        mainController.setExtremumList(extremumList);
    }
}
