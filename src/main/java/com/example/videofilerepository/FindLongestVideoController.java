package com.example.videofilerepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/*
 * Контролер віка знаходження найдовших елементів
 */
public class FindLongestVideoController implements Initializable {
    // Оголошення елментів вікна
    @FXML
    private TextField numberTF;

    @FXML
    private Label numberLabel;

    @FXML
    private CheckBox subtitlesCheckBox;

    // Оголошення списків та контролера головного вікна
    private FileListManager videoFileList;
    private Controller mainController;
    private FileListManager longestFilesList;

    // Доступний розмір вибору кількості елементів
    private int accessSize;

    // Початкова ініціалізація значень полів та елементів вікна
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        subtitlesCheckBox.setOnAction(event -> {
            FileListManager fm;
            /* Підрахунок максимально можливої кількості вибраних елементів та
            основі кількості елементів з таким самим статусо наявності субтитрів*/
            if (subtitlesCheckBox.isSelected()) {
                fm = videoFileList.groupBySubtitles(true);
                accessSize = fm.getList().size();
            } else {
                fm = videoFileList.groupBySubtitles(false);
                accessSize = fm.getList().size();
            }

            if (accessSize == 0) {
                numberLabel.setText("No elements with this subt. status");
            } else {
                numberLabel.setText("Number (1-" + accessSize + ")");
            }
        });
    }

    // Сетери для ініціалізації списків та контролера
    public void setVideoFileList(FileListManager fileManager) {
        this.videoFileList = fileManager;
        setStartAccessSize();
    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    public void setLongestFilesList(FileListManager fileManager) {
        this.longestFilesList = fileManager;
    }

    // Визначення базової кількості елментів без наявності субтитрів
    public void setStartAccessSize() {
        FileListManager fm1;
        fm1 = videoFileList.groupBySubtitles(false);
        accessSize = fm1.getList().size();
        if (accessSize == 0) {
            numberLabel.setText("No elements with this subt. status");
        } else {
            numberLabel.setText("Number (1-" + accessSize + ")");
        }
    }

    // Метод пошуку найдовших елементів
    @FXML
    void onFindButtonClick(ActionEvent event) {
        if (videoFileList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "List is empty");
            return;
        }

        boolean subtitlesExists;
        if (subtitlesCheckBox.isSelected()) {
            subtitlesExists = true;
            mainController.findLongestSubtitlesTF.setText("Yes");
        } else {
            subtitlesExists = false;
            mainController.findLongestSubtitlesTF.setText("No");
        }
        subtitlesCheckBox.setSelected(false);

        int number;
        try {
            if (numberTF.getText().isEmpty()) {
                throw new EmptyFieldException("Field is empty");
            }
            number = Integer.parseInt(numberTF.getText());
            if (number < 1 || number > accessSize) {
                throw new NumberFormatException("Index problem");
            }
            mainController.findLongestNumberLabel.setText(number + "");
        } catch (NumberFormatException | EmptyFieldException e) {
            System.out.println("Error: " + e.getMessage());
            if (accessSize == 0) {
                mainController.showErrorAlert("Error Input", e.getMessage() +
                        "\nNo files with this subt. status");
            } else {
                mainController.showErrorAlert("Error Input", e.getMessage() +
                        "\nincorrect input (must be number 1-" + accessSize + ")");
            }
            setStartAccessSize();
            numberTF.clear();
            return;
        }

        longestFilesList = videoFileList.findLongestFilesList(subtitlesExists, number);
        mainController.updateContextTableView(longestFilesList);
        mainController.findLongestDurationTF.setText(longestFilesList.getList().get(0).getDuration() + "");
        mainController.setLongestFilesList(longestFilesList);
        numberTF.clear();
        setStartAccessSize();
    }

}