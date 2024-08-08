package com.spellingbee;

import com.spellingbee.views.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private GameManager gameManager = new GameManager("resources/sozluk_v2.txt");

    @Override
    public void start(Stage stage) throws Exception {
        ViewFactory viewFactory = new ViewFactory(gameManager);
        viewFactory.showMainWindow();
    }
}
