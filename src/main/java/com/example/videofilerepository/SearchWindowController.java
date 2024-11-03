package com.example.videofilerepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/*
 * Контролер віка пошуку елементів за шляхом та тривалістю
 */
public class SearchWindowController implements Initializable {
    // Оголошення елментів вікна
    @FXML
    private Pane durationPane;

    @FXML
    private TextField durationTextField;

    @FXML
    private ChoiceBox<String> formatsChoice;

    @FXML
    private Pane pathPane;

    @FXML
    private TextField pathTextField;

    private Controller mainController;

    // Можливі варіанти пошуку елементів
    private final String[] formatsStr = {"By duration", "By path"};

    private int formatInt;

    // Копія списку з головного вікна
    private FileListManager videoFileList;

    // Списки знайдених елементів
    private FileListManager findByDurationList;
    private FileListManager findByPathList;

    // Сетери для списків та контролерів
    public void setVideoFileList(FileListManager fileManager) {
        this.videoFileList = fileManager;
    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    public void setFindByDurationList(FileListManager findByDurationList) {
        this.findByDurationList = findByDurationList;
    }

    public void setFindByPathList(FileListManager findByPathList) {
        this.findByPathList = findByPathList;
    }

    // Початкова ініціалізація значень полів та елементів вікна
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        durationPane.setVisible(false);
        pathPane.setVisible(false);
        formatsChoice.getItems().addAll(formatsStr);
    }

    // Метод вибору способу знаходження елементів (результат натиску на кнопу Choose)
    @FXML
    void onChooseButtonClick(ActionEvent event) {
        if (videoFileList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "List is empty");
            return;
        }

        String selectedFormat = formatsChoice.getValue();
        if (selectedFormat != null) {
            if (selectedFormat.equals(formatsStr[0])) {
                pathPane.setVisible(false);
                durationPane.setVisible(true);
                formatInt = 0;
            } else if (selectedFormat.equals(formatsStr[1])) {
                durationPane.setVisible(false);
                pathPane.setVisible(true);
                formatInt = 1;
            }
        } else {
            mainController.showErrorAlert("Choose Error", "Choose format of finding");
        }
    }

    // Метод знаходження елементів відповідно до вибраного способу (результат натиску на кнопу Find)
    @FXML
    void onFindButtonClick(ActionEvent event) {
        if (videoFileList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "List is empty");
            return;
        }
        if (formatInt < 0) {
            mainController.showErrorAlert("Error", "Choose format and input value");
            return;
        }

        if (formatInt == 0) {
            double duration;
            try {
                if (durationTextField.getText().isEmpty()) {
                    throw new EmptyFieldException("Field is empty");
                }
                duration = Double.parseDouble(durationTextField.getText());
                if (duration < 1 || duration > videoFileList.getMaxSize()) {
                    throw new NumberFormatException("Input problem");
                }
            } catch (NumberFormatException | EmptyFieldException e) {
                System.out.println("Error: " + e.getMessage());
                mainController.showErrorAlert("Error Input", e.getMessage() +
                        "\nincorrect input (must be number > 0 and < " + videoFileList.getMaxSize() + ")");
                durationTextField.clear();
                return;
            }

            try {
                findByDurationList = videoFileList.findByDuration(duration);
            } catch (VideoNotFoundException e) {
                System.out.println("Video not found: " + e.getMessage());
                mainController.showErrorAlert("Video not found", e.getMessage());
            }
            mainController.updateContextTableView(findByDurationList);
            mainController.findFormatTF.setText(formatsStr[0]);
            mainController.findDataTF.setText(durationTextField.getText());
            mainController.findNumberLabel.setText(findByDurationList.getList().size() + "");
            mainController.setFindByDurationList(findByDurationList);
            durationTextField.clear();
        } else if (formatInt == 1) {
            String path;
            try {
                if (pathTextField.getText().isEmpty()) {
                    throw new EmptyFieldException("Field is empty");
                }
                path = pathTextField.getText();
            } catch (NumberFormatException | EmptyFieldException e) {
                System.out.println("Error: " + e.getMessage());
                mainController.showErrorAlert("Error Input", e.getMessage());
                durationTextField.clear();
                return;
            }

            try {
                findByPathList = videoFileList.findByPath(path);
            } catch (VideoNotFoundException e) {
                System.out.println("Video not found: " + e.getMessage());
                mainController.showErrorAlert("Video not found", e.getMessage());
            }
            mainController.updateContextTableView(findByPathList);
            mainController.findFormatTF.setText(formatsStr[1]);
            mainController.findDataTF.setText(pathTextField.getText());
            mainController.findNumberLabel.setText(findByPathList.getList().size() + "");
            mainController.setFindByPathList(findByPathList);
            pathTextField.clear();
        }
    }
}
