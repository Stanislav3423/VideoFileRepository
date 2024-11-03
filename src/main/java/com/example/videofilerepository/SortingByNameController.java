package com.example.videofilerepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/*
 * Контролер віка сортування елементів за іменем
 */
public class SortingByNameController {
    // Оголошення елментів вікна
    @FXML
    private CheckBox subtitlesCheckBox;

    // Оголошення списків та контролера головного вікна
    private FileListManager videoFileList;
    private Controller mainController;
    public FileListManager sortList;

    // Сетери для списків та контролерів
    public void setVideoFileList(FileListManager fileManager) {
        this.videoFileList = fileManager;
    }
    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }
    public void setSortList(FileListManager sortList) {
        this.sortList = sortList;
    }

    // Метод групування елеменнтів за наявністю субтитрів(результат натиску на кнопу Group)
    @FXML
    void onGroupButtonClick(ActionEvent event) {
        if (videoFileList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "List is empty");
            return;
        }
        boolean subtitlesExists;
        if (subtitlesCheckBox.isSelected()) {
            subtitlesExists=true;
            mainController.sortSubtitlesTF.setText("Yes");
        } else {
            subtitlesExists=false;
            mainController.sortSubtitlesTF.setText("No");
        }
        subtitlesCheckBox.setSelected(false);
        sortList = videoFileList.groupBySubtitles(subtitlesExists);
        mainController.updateContextTableView(sortList);
        mainController.setSortList(sortList);
    }

    // Метод сортування елементів Counting Sort за іменем (результат натиску на кнопу Sort)
    @FXML
    void onSortButtonClick(ActionEvent event) {
        if (videoFileList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "List is empty");
            return;
        }
        if (sortList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "Group is empty");
            return;
        }
        int charSizeSort = Integer.MAX_VALUE;
        for (VideoFile vf : sortList.getList()) {
            charSizeSort = Math.min(charSizeSort, vf.getName().length());
        }
        if (charSizeSort > 3) {
            charSizeSort = 3;
        }
        System.out.println("\nMin char size: " + charSizeSort);
        long startTime = System.currentTimeMillis();
        sortList = sortList.countingSort(charSizeSort);
        long estimatedTime = System.currentTimeMillis() - startTime;
        mainController.timeSortTF.setText(estimatedTime + " msec");
        mainController.updateContextTableView(sortList);
        mainController.setSortList(sortList);
    }

}