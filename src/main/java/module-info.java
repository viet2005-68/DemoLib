module org.example.demodb {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires okhttp3;
    requires org.json;
    requires kotlin.stdlib;
    requires mysql.connector.j;

    opens org.example.demodb to javafx.fxml;
    exports org.example.demodb;
}