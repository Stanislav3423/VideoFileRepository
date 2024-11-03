package com.example.videofilerepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/*
 * Клас-контроллер головного вікна програми
 * Реалізує виклик нових вікон, описує функціонал елементів головного вікна.
 * Використовується для виводу інформації зі списків у таблиці, виклику функцій редагування списків
 */
public class Controller implements Initializable {
    // Створення елементів таблиці для виводу основного списку
    @FXML
    public TableView<VideoFile> Table;
    @FXML
    public TableColumn<VideoFile, String> NameColumn;
    @FXML
    public TableColumn<VideoFile, String> PathColumn;
    @FXML
    public TableColumn<VideoFile, String> FormatColumn;
    @FXML
    public TableColumn<VideoFile, Long> DurationColumn;
    @FXML
    public TableColumn<VideoFile, String> VCodecColumn;
    @FXML
    public TableColumn<VideoFile, String> ACodecColumn;
    @FXML
    public TableColumn<VideoFile, String> SubtitlesColumn;
    @FXML
    public TableColumn<VideoFile, Long> SizeColumn;
    @FXML
    public TableColumn<VideoFile, String> PlayerColumn;
    ObservableList<VideoFile> data;

    // Створення елементів таблиці, що виводить проміжкові результати редагування
    @FXML
    public TableView<VideoFile> ContextTable;
    @FXML
    private TableColumn<VideoFile, String> NameColumn1;
    @FXML
    private TableColumn<VideoFile, String> PathColumn1;
    @FXML
    private TableColumn<VideoFile, String> FormatColumn1;
    @FXML
    private TableColumn<VideoFile, Long> DurationColumn1;
    @FXML
    private TableColumn<VideoFile, String> VCodecColumn1;
    @FXML
    private TableColumn<VideoFile, String> ACodecColumn1;
    @FXML
    private TableColumn<VideoFile, String> SubtitlesColumn1;
    @FXML
    private TableColumn<VideoFile, Long> SizeColumn1;
    @FXML
    private TableColumn<VideoFile, String> PlayerColumn1;
    ObservableList<VideoFile> contextData;

    /* Створення панелей з елементами для контролю виконання функцій */

    // Початкова панель
    private int currentPane;
    @FXML
    private Pane startControlPanel;
    @FXML
    private Button writeButton;
    @FXML
    private Label controlLabel;

    // Панель з інформацією про знайдені елементи функції пошуку елементів за тривалістю та шляхом
    @FXML
    private Pane findControlPanel;
    @FXML
    public TextField findDataTF;
    @FXML
    public TextField findFormatTF;
    @FXML
    public Label findNumberLabel;

    // Панель з інформацією про групування файлів за кодеком та їх сортування
    @FXML
    private Pane groupControlPanel;
    @FXML
    public TextField groupFormatTF;

    // панель з інформацією про найдовші файли у списку
    @FXML
    private Pane findLongestControlPanel;
    @FXML
    public TextField findLongestSubtitlesTF;
    @FXML
    public TextField findLongestDurationTF;
    @FXML
    public Label findLongestNumberLabel;

    // Панель з інформацією про знаходження мінімального та максимального значення у списку
    @FXML
    private Pane extremumControlPanel;
    @FXML
    public TextField extremumFormatTF;
    @FXML
    public TextField maxFormatTF;
    @FXML
    public TextField minFormatTF;

    // Панель з інформацією про успішність сорутвання списку за допомогою counting sort
    @FXML
    private Pane sortControlPanel;
    @FXML
    public TextField sortSubtitlesTF;
    @FXML
    public TextField timeSortTF;
    private Image icon;

    // Створення основних списків
    public FileListManager videoFileList;
    public FileListManager findByDurationList;
    public FileListManager findByPathList;
    public FileListManager longestFilesList;
    public FileListManager extremumList;
    public FileListManager sortList;

    /*
     * Перевизначена функція ініціалізації полів під час створення вікна
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableInitialize();
        contextTableInitialize();
        currentPane = controlPanelService(1);
        writeButton.setVisible(false);
        icon = new Image("D:\\Universe\\Sem3\\Cursova\\Project\\VideoFileRepository\\icon.png");
        sortList = new FileListManager();
        extremumList = new FileListManager();
        longestFilesList = new FileListManager();
        findByPathList = new FileListManager();
        findByDurationList = new FileListManager();
        videoFileList = new FileListManager();
    }

    // Реалізація сеттерів
    public void setVideoFileList(FileListManager fileManager) {
        this.videoFileList = fileManager;
    }

    public void setSortList(FileListManager fileManager) {
        this.sortList = fileManager;
    }

    public void setExtremumList(FileListManager fileManager) {
        this.extremumList = fileManager;
    }

    public void setLongestFilesList(FileListManager fileManager) {
        this.longestFilesList = fileManager;
    }

    public void setFindByPathList(FileListManager fileManager) {
        this.findByPathList = fileManager;
    }

    public void setFindByDurationList(FileListManager fileManager) {
        this.findByDurationList = fileManager;
    }

    /*
     * Функція управління відображенням активних панелей
     */
    int controlPanelService(int activePanel) {
        clearPanels();
        startControlPanel.setVisible(false);
        findControlPanel.setVisible(false);
        groupControlPanel.setVisible(false);
        findLongestControlPanel.setVisible(false);
        extremumControlPanel.setVisible(false);
        sortControlPanel.setVisible(false);
        if (activePanel > 1) {
            writeButton.setVisible(true);
        }

        switch (activePanel) {
            case 1:
                startControlPanel.setVisible(true);
                break;
            case 2:
                findControlPanel.setVisible(true);
                controlLabel.setText("Find elements");
                break;
            case 3:
                groupControlPanel.setVisible(true);
                controlLabel.setText("Group and Sort elements");
                break;
            case 4:
                findLongestControlPanel.setVisible(true);
                controlLabel.setText("Find longest files");
                break;
            case 5:
                extremumControlPanel.setVisible(true);
                controlLabel.setText("Format size extremum");
                break;
            case 6:
                sortControlPanel.setVisible(true);
                controlLabel.setText("Sorting by name");
                break;
            default:
                break;
        }
        return activePanel;
    }

    // Ініціалізація назв стовпців головної таблиці
    private void tableInitialize() {
        NameColumn.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("Name"));
        PathColumn.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("Path"));
        FormatColumn.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("Format"));
        DurationColumn.setCellValueFactory(new PropertyValueFactory<VideoFile, Long>("Duration"));
        VCodecColumn.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("VideoCodec"));
        ACodecColumn.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("AudioCodec"));
        SubtitlesColumn.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("StrBoolSubtitles"));
        SizeColumn.setCellValueFactory(new PropertyValueFactory<VideoFile, Long>("Size"));
        PlayerColumn.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("Player"));
    }

    // Ініціалізація назв стовпців допоміжної таблиці
    private void contextTableInitialize() {
        NameColumn1.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("Name"));
        PathColumn1.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("Path"));
        FormatColumn1.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("Format"));
        DurationColumn1.setCellValueFactory(new PropertyValueFactory<VideoFile, Long>("Duration"));
        VCodecColumn1.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("VideoCodec"));
        ACodecColumn1.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("AudioCodec"));
        SubtitlesColumn1.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("StrBoolSubtitles"));
        SizeColumn1.setCellValueFactory(new PropertyValueFactory<VideoFile, Long>("Size"));
        PlayerColumn1.setCellValueFactory(new PropertyValueFactory<VideoFile, String>("Player"));
    }

    // Передача у головну таблицю значень зі списку
    public void updateTableView() {
        data = FXCollections.observableArrayList(
                videoFileList.getList()
        );
        Table.setItems(data);
    }

    // Передача у допоміжну таблицю значень зі списку
    public void updateContextTableView(FileListManager fm) {
        contextData = FXCollections.observableArrayList(
                fm.getList()
        );
        ContextTable.setItems(contextData);
    }

    // Виклик вікна з відображенням помилки
    void showErrorAlert(String message, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(message);
        alert.setContentText(content);

        alert.showAndWait();
    }

    // Метод для опису функціоналу кнопки Read з Меню
    @FXML
    void onReadSegmentInFileMenuClick(ActionEvent event) {
        // Викликає мето для зчитування інформації з файлу
        readingFile();
    }

    // Метод зчитування інформації з файлу
    public void readingFile() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose reading file");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter
                    ("TXT files (*.txt)", "*.txt");
            // Заборона зчитувань будь-яких файлів окрім .txt
            fileChooser.getExtensionFilters().add(extFilter);
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            String readFilePath = selectedFile.getAbsolutePath();
            videoFileList.readingFromFile(readFilePath);
            if (!videoFileList.getList().isEmpty()) {
                System.out.println("Finish reading");
            } else {
                showErrorAlert("Reading Error", "Incorrect position of fields or empty file");
                System.out.println("Unsuccessful reading");
            }
            updateTableView();
        } catch (NullPointerException e) {
            System.out.println("Unsuccessful reading: " + e.getMessage());
        }
    }

    // Метод для опису функціоналу кнопки Write з Меню
    @FXML
    void onWriteSegmentInFileMenuClick(ActionEvent event) {
        writingFile(videoFileList);
    }

    // Метод для опису вводу гарячих клавіш Ctrl+W для запису інформації у файл
    void writeMainClassKey() {
        // Виклик методу запису інформації у файл
        writingFile(videoFileList);
    }

    // Методу запису інформації у файл
    public void writingFile(FileListManager fm) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose writing file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter
                ("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            System.out.println("Choosed writing file: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("Choose censeled");
            return;
        }
        String writingFilePath = selectedFile.getAbsolutePath();
        boolean write = fm.writingToFile(writingFilePath);
        if (write) {
            System.out.println("Successful writing");
        } else {
            System.out.println("Unsuccessful writing");
        }
    }

    /*
     * Метод запису допоміжної інформаці у файл, залежно від останньої викликаної функції (Результат натиску на
     * кнопку Write results to file)
     */
    @FXML
    void onContextWriteFindButtonClick(ActionEvent event) {
        FileListManager listForWriting = new FileListManager();
        switch (currentPane) {
            case 2:
                if (findFormatTF.getText().equals("By duration")) {
                    listForWriting = findByDurationList;
                } else if (findFormatTF.getText().equals("By path")) {
                    writingFile(findByPathList);
                }
                break;
            case 3:
                listForWriting = videoFileList;
                break;
            case 4:
                listForWriting = longestFilesList;
                break;
            case 5:
                listForWriting = extremumList;
                break;
            case 6:
                listForWriting = sortList;
                break;
            default:
                writingFile(videoFileList);
                listForWriting = videoFileList;
                break;
        }
        if (listForWriting != null && !listForWriting.getList().isEmpty()) {
            writingFile(listForWriting);
        } else {
            showErrorAlert("Writing Error", "List for writing empty");
        }
    }

    // Виклики допоміжних вікон (Подальші методи викликаються як результат натиску на кнопки з Меню)

    // Вікно додавання нових елементів
    @FXML
    void onAddMenuButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(VideoFileRepository.class.getResource
                    ("AddWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add elements");
            stage.getIcons().add(icon);
            stage.setScene(new Scene(root1));
            AddWindowController addController = fxmlLoader.getController();
            addController.setMainController(this);
            addController.setVideFileList(videoFileList);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window");
        }
    }

    // Вікно видалення елементів
    @FXML
    void onDeleteMenuButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(VideoFileRepository.class.getResource
                    ("DeleteWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Delete elements");
            stage.getIcons().add(icon);
            stage.setScene(new Scene(root1));
            DeleteWindowController deleteController = fxmlLoader.getController();
            deleteController.setMainController(this);
            deleteController.setVideoFileList(videoFileList);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window");
        }
    }

    // Вікно пошуку файлів за тривалістю або за шляхом до них
    @FXML
    void onFindMenuButtonClick(ActionEvent event) {
        currentPane = controlPanelService(2);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(VideoFileRepository.class.getResource("SearchWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Find elements");
            stage.getIcons().add(icon);
            stage.setScene(new Scene(root1));
            SearchWindowController searchController = fxmlLoader.getController();
            searchController.setMainController(this);
            searchController.setVideoFileList(videoFileList);
            searchController.setFindByDurationList(findByDurationList);
            searchController.setFindByPathList(findByPathList);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window");
        }
    }

    // Вікно групування файлів за кодеками та їх сотрування
    @FXML
    void onGroupMenuButtonClick(ActionEvent event) {
        currentPane = controlPanelService(3);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(VideoFileRepository.class.getResource
                    ("GroupSortWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Group elements");
            stage.getIcons().add(icon);
            stage.setScene(new Scene(root1));
            GroupSortWindowController groupController = fxmlLoader.getController();
            groupController.setMainController(this);
            groupController.setVideoFileList(videoFileList);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window");
        }
    }

    // Вікно знаходження найдовших файлів з найвними субтитрами
    @FXML
    void onLongestMenuButtonClick(ActionEvent event) {
        currentPane = controlPanelService(4);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(VideoFileRepository.class.getResource
                    ("FindLongestVideoWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Find longest elements");
            stage.getIcons().add(icon);
            stage.setScene(new Scene(root1));
            FindLongestVideoController longestController = fxmlLoader.getController();
            longestController.setVideoFileList(videoFileList);
            longestController.setMainController(this);
            longestController.setLongestFilesList(longestFilesList);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window");
        }
    }

    // Вікно знаходження найбільшого та найменшого файлів за вибраним форматом
    @FXML
    void onExtremumButtonClick(ActionEvent event) {
        currentPane = controlPanelService(5);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(VideoFileRepository.class.getResource
                    ("FormatExtremumWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Format size extremum");
            stage.getIcons().add(icon);
            stage.setScene(new Scene(root1));
            FormatExtremumController extremumController = fxmlLoader.getController();
            extremumController.setMainController(this);
            extremumController.setVideoFileList(videoFileList);
            extremumController.setExtremumList(extremumList);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window");
        }
    }

    // Вікно для виклику сортування за Counting sort елементів з наявними субтитрами
    @FXML
    void onSortByNameButtonClick(ActionEvent event) {
        currentPane = controlPanelService(6);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(VideoFileRepository.class.getResource
                    ("SortingByNameWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Sorting by name");
            stage.getIcons().add(icon);
            stage.setScene(new Scene(root1));
            SortingByNameController sortController = fxmlLoader.getController();
            sortController.setMainController(this);
            sortController.setVideoFileList(videoFileList);
            sortController.setSortList(sortList);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window");
        }
    }

    // Вивід інформації у консоль
    @FXML
    void onConsoleDisplayButtonClick(ActionEvent event) {
        System.out.println("Display List: ");
        for (VideoFile node : videoFileList.getList()) {
            System.out.println(node);
        }
    }

    // Ввід інформації у консоль
    @FXML
    void onConsoleInputButtonClick(ActionEvent event) {
        System.out.println("Input data: ");
        VideoFile newNode = VideoFile.inputFromConsole();
        if (newNode != null) {
            videoFileList.getList().add(newNode);
            updateTableView();
        } else {
            System.out.println("Incorrect input!");
        }
    }

    // Метод очищення полів панелей
    public void clearPanels() {
        findDataTF.clear();
        findNumberLabel.setText("...");
        findFormatTF.clear();

        groupFormatTF.clear();

        findLongestDurationTF.clear();
        findLongestSubtitlesTF.clear();
        findLongestNumberLabel.setText("...");

        extremumFormatTF.clear();
        maxFormatTF.clear();
        minFormatTF.clear();

        sortSubtitlesTF.clear();
        timeSortTF.clear();
    }

    // Очищення головного вікна та списків
    @FXML
    void onClearButtonClick(ActionEvent event) {
        videoFileList.deleteResources();

        findByDurationList.deleteResources();
        findByPathList.deleteResources();
        longestFilesList.deleteResources();
        extremumList.deleteResources();
        sortList.deleteResources();
        sortList.deleteResources();
        updateTableView();
        updateContextTableView(videoFileList);

        controlPanelService(1);
        writeButton.setVisible(false);
        clearPanels();
    }
}