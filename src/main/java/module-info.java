module com.example.videofilerepository {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.videofilerepository to javafx.fxml;
    exports com.example.videofilerepository;
}