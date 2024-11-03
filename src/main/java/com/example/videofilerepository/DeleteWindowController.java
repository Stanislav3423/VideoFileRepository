package com.example.videofilerepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/*
 * Контролер віка видалення елементів
 */
public class DeleteWindowController implements Initializable {
    // Оголошення елментів вікна
    @FXML
    private TextField nameTextField;

    @FXML
    private ChoiceBox<String> formatsChoice;

    @FXML
    private Pane indexPane;

    @FXML
    private TextField indexTextField;

    @FXML
    private Pane namePane;

    @FXML
    private Label numberTF;
    private Controller mainController;

    // Копія списку з головного вікна
    private FileListManager videoFileList;

    // Можливі варіанти видалення елементів
    private final String[] formatsStr = {"By index", "By name"};

    private int formatInt;

    // Сетери для списка та контролерів
    public void setVideoFileList(FileListManager fileManager) {
        this.videoFileList = fileManager;
    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    // Початкова ініціалізація значень полів та елементів вікна
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        namePane.setVisible(false);
        indexPane.setVisible(false);
        formatsChoice.getItems().addAll(formatsStr);
        formatInt = -1;
    }

    // Метод вибору способу видалення елемента (результат натиску на кнопу Choose)
    @FXML
    void onChooseButtonClick(ActionEvent event) {
        if (videoFileList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "List is empty");
            return;
        }

        String selectedFormat = formatsChoice.getValue();
        if (selectedFormat != null) {
            if (selectedFormat.equals(formatsStr[0])) {
                namePane.setVisible(false);
                indexPane.setVisible(true);
                numberTF.setText("Number (1-" + videoFileList.getList().size() + ")");
                formatInt = 0;
            } else if (selectedFormat.equals(formatsStr[1])) {
                indexPane.setVisible(false);
                namePane.setVisible(true);
                formatInt = 1;
            }
        } else {
            mainController.showErrorAlert("Choose Error", "Choose format of deleting");
        }
    }

    // Метод видалення елемента відповідно до способу видалення (результат натиску на кнопу Delete)
    @FXML
    void onButtonDeleteClick(ActionEvent event) {
        if (videoFileList.getList().isEmpty()) {
            mainController.showErrorAlert("Error", "List is empty");
            return;
        }
        if (formatInt < 0) {
            mainController.showErrorAlert("Error", "Choose format and input value");
            return;
        }

        if (formatInt == 0) {
            int index;
            try {
                if (indexTextField.getText().isEmpty()) {
                    throw new EmptyFieldException("Field is empty");
                }
                index = Integer.parseInt(indexTextField.getText());
                if (index < 1 || index > videoFileList.getList().size()) {
                    throw new NumberFormatException("Index problem");
                }
                numberTF.setText("Number (1-" + (videoFileList.getList().size() - 1) + ")");
            } catch (NumberFormatException | EmptyFieldException e) {
                System.out.println("Error: " + e.getMessage());
                mainController.showErrorAlert("Error Input", e.getMessage() +
                        "\nincorrect input (must be number 1-" + videoFileList.getList().size() + ")");
                indexTextField.clear();
                return;
            }
            videoFileList.getList().remove(index - 1);

            mainController.updateTableView();
            indexTextField.clear();
        } else if (formatInt == 1) {
            String name;
            try {
                if (nameTextField.getText().isEmpty()) {
                    throw new EmptyFieldException("Field is empty");
                }
                name = nameTextField.getText();
                videoFileList.validateCyrillicInput(name);
            } catch (EmptyFieldException e) {
                System.out.println("Error: " + e.getMessage());
                mainController.showErrorAlert("Field is empty", "");
                nameTextField.clear();
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                mainController.showErrorAlert("Input Error", e.getMessage());
                nameTextField.clear();
                return;
            }

            try {
                videoFileList.deleteByName(name);
            } catch (VideoNotFoundException e) {
                System.out.println("Video not found: " + e.getMessage());
                mainController.showErrorAlert("Video not found", e.getMessage());
            }
            mainController.updateTableView();
            nameTextField.clear();
        }
    }
}