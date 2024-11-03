package com.example.videofilerepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/*
 * Контролер віка додавання нових елементів
 */
public class AddWindowController implements Initializable {
    // Оголошення елментів вікна
    @FXML
    private ChoiceBox<String> audioCodecsChoice;

    @FXML
    private Button duplicateButton;

    @FXML
    private TextField durationTextField;

    @FXML
    private ChoiceBox<String> formatsChoice;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField pathTextField;

    @FXML
    private ChoiceBox<String> playersChoice;

    @FXML
    private TextField sizeTextField;

    @FXML
    private CheckBox subtitleCheckBox;

    @FXML
    private ChoiceBox<String> videoCodecsChoice;
    private Controller mainController;

    // Копія списку з головного вікна
    private FileListManager videFileList;

    // Поточний елемент, використовується для створення копії новоствореного елемента
    private VideoFile currentNode;

    // Рахунок копій для зміни назв наступних
    private int copyCounter;

    // Доступні значення полів (використовуються у Choice Box)
    private final String[] formatsStr = {"MP4", "MOV", "FLV", "WMV"};
    private final String[] videoCodecsStr = {"H.264/AVC", "H.265/HEVC", "AV1", "VP9", "H.266/VVC"};
    private final String[] audioCodecsStr = {"G.711", "FLAC", "AMR", "ALAC", "AAC"};
    private final String[] playersStr = {"GOM.Player", "Kodi", "PotPlayer", "VLC.MediaPlayer"};

    // Сетери для списку елементів та головного контролера програми (для доступу до головного вікна)
    public void setVideFileList(FileListManager fileManager) {
        this.videFileList = fileManager;
    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    // Початкова ініціалізація значень полів та елементів вікна
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formatsChoice.getItems().addAll(formatsStr);
        videoCodecsChoice.getItems().addAll(videoCodecsStr);
        audioCodecsChoice.getItems().addAll(audioCodecsStr);
        playersChoice.getItems().addAll(playersStr);
        duplicateButton.setOpacity(0.5);
    }

    // Метод додавання нових елементів
    @FXML
    void onButtonAddClick(ActionEvent event) {
        if (fieldsIsEmpty()) {
            mainController.showErrorAlert("Error Input", "There are empty fields");
            return;
        }
        try {
            copyCounter = 0;
            VideoFile newNode = new VideoFile();
            videFileList.validateCyrillicInput(nameTextField.getText());
            newNode.setName(nameTextField.getText());
            newNode.setFilePath(pathTextField.getText());
            if (Double.parseDouble(durationTextField.getText()) < 0 ||
                    Double.parseDouble(durationTextField.getText()) > videFileList.getMaxSize()) {
                throw new NumberFormatException("In line '" + durationTextField.getText() + "', value must be 0 - 999999");
            }
            newNode.setDurationInSeconds(Double.parseDouble(durationTextField.getText()));
            newNode.setFormat(formatsChoice.getValue());
            newNode.setVideoCodec(videoCodecsChoice.getValue());
            newNode.setAudioCodec(audioCodecsChoice.getValue());
            if (subtitleCheckBox.isSelected()) {
                newNode.setSubtitles(true);
            } else {
                newNode.setSubtitles(false);
            }
            if (Double.parseDouble(sizeTextField.getText()) < 0 ||
                    Double.parseDouble(sizeTextField.getText()) > videFileList.getMaxSize()) {
                throw new NumberFormatException("In line '" + sizeTextField.getText() + "', value must be 0 - 999999");
            }
            newNode.setSize(Double.parseDouble(sizeTextField.getText()));
            newNode.setPlayer(playersChoice.getValue());
            videFileList.getList().add(newNode);
            // Ініціалізація вузла для дублювання
            currentNode = new VideoFile(newNode);
            //clearFields();
        } catch (NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
            mainController.showErrorAlert("Error Input", e.getMessage() + "\nincorrect input");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            mainController.showErrorAlert("Error Input", e.getMessage() + "\nincorrect input");
        }
        mainController.updateTableView();
        duplicateButton.setOpacity(1);
        clearFields();
    }

    // Очищення полів після додавання значення
    public void clearFields() {
        nameTextField.clear();
        pathTextField.clear();
        durationTextField.clear();
        subtitleCheckBox.setSelected(false);
        sizeTextField.clear();
        formatsChoice.setValue(null);
        videoCodecsChoice.setValue(null);
        audioCodecsChoice.setValue(null);
        playersChoice.setValue(null);
    }

    // Перевірка на пусті поля
    public boolean fieldsIsEmpty() {
        if (nameTextField.getText().isEmpty())
            return true;
        if (pathTextField.getText().isEmpty())
            return true;
        if (durationTextField.getText().isEmpty())
            return true;
        if (formatsChoice.getItems().isEmpty())
            return true;
        if (videoCodecsChoice.getItems().isEmpty())
            return true;
        if (audioCodecsChoice.getItems().isEmpty())
            return true;
        if (sizeTextField.getText().isEmpty())
            return true;
        if (playersChoice.getItems().isEmpty())
            return true;
        return false;
    }

    // Метод дублювання значення
    @FXML
    void onDuplicateButtonClick(ActionEvent event) {
        copyCounter++;
        if (currentNode == null) {
            mainController.showErrorAlert("Error copy", "First, add a new element");
            return;
        }
        VideoFile newNode = new VideoFile(currentNode);
        String name = newNode.getName();
        name = name + copyCounter;
        newNode.setName(name);
        videFileList.getList().add(newNode);
        System.out.println(newNode.getName());
        mainController.updateTableView();
    }
}
