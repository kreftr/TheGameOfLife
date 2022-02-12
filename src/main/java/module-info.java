module pl.kreft.rafal.thegameoflife {
    requires javafx.controls;
    requires javafx.fxml;


    opens pl.kreft.rafal.thegameoflife to javafx.fxml;
    exports pl.kreft.rafal.thegameoflife;
}