module com.example.mp3playerproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.mp3playerproject to javafx.fxml;
    exports com.example.mp3playerproject;
}