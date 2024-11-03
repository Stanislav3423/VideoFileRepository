package com.example.videofilerepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/*
 * Контролер віка групування елементів
 */
public class GroupSortWindowController implements Initializable {
    // Оголошення елментів вікна
    @FXML
    private ChoiceBox<String> formatsChoice;
    // Можливі варіанти вибору типу кодеків
    private final String[] formatsStr = {"Video", "Audio"};
    private int formatInt;
    // Копія списку з головного вікна
    private FileListManager videoFileList;
    Map<String, List<VideoFile>> groupedItems;
    private Controller mainController;

    // Початкова ініціалізація значень полів та елементів вікна
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formatInt = -1;
        formatsChoice.getItems().addAll(formatsStr);
    }

    // Сетери для списка та контролерів
    public void setVideoFileList(FileListManager fileManager) {
        this.videoFileList = fileManager;
    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    // Метод вибору типу кодеків (результат натиску на кнопу Choose)
    @FXML
    void onChooseButtonClick(ActionEvent event) {
        if (videoFileList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "List is empty");
            return;
        }
        String selectedFormat = formatsChoice.getValue();
        if (selectedFormat != null) {
            if (selectedFormat.equals(formatsStr[0])) {
                formatInt = 0;
            } else if (selectedFormat.equals(formatsStr[1])) {
                formatInt = 1;
            }
            mainController.groupFormatTF.setText(formatsStr[formatInt] + "");
        } else {
            mainController.showErrorAlert("Choose Error", "Choose format of grouping");
        }
    }

    // Метод групування кодеків за вибраним типом (результат натиску на кнопу Group)
    @FXML
    void onGroupButtonClick(ActionEvent event) {
        if (videoFileList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "List is empty");
            return;
        }
        if (formatInt < 0) {
            mainController.showErrorAlert("Error", "Choose format and input value");
            return;
        }
        try {
            groupedItems = videoFileList.groupItemsByCodec(videoFileList, formatInt);
            List<VideoFile> flattenedList = videoFileList.mapToListConvert(groupedItems);
            videoFileList.setList(flattenedList);
            mainController.updateTableView();
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Метод сортування груп кодеків (результат натиску на кнопу Sort)
    @FXML
    void onSortButtonClick(ActionEvent event) {
        if (videoFileList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "List is empty");
            return;
        }
        if (formatInt < 0) {
            mainController.showErrorAlert("Error", "Choose format and input value");
            return;
        }
        if (groupedItems == null) {
            mainController.showErrorAlert("Error", "First group elements");
            return;
        }

        try {
            videoFileList.sortGroupedElements(groupedItems);
            List<VideoFile> flattenedList = videoFileList.mapToListConvert(groupedItems);
            videoFileList.setList(flattenedList);
            mainController.updateTableView();
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}