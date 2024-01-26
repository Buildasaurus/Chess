module chess {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.reflections;

    opens chess to javafx.fxml;
    exports chess;
}
