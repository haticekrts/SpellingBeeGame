package com.spellingbee.views;

import com.spellingbee.GameManager;
import com.spellingbee.controllers.BaseController;
import com.spellingbee.controllers.GameWindowController;
import com.spellingbee.controllers.MainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ViewFactory {
    private ArrayList<Stage> activeStages;
    private GameManager gameManager;

    public ViewFactory(GameManager gameManager){
        this.gameManager=gameManager;
        activeStages = new ArrayList<Stage>();
    }

    public void showGameWindow() {
        BaseController controller = new GameWindowController(gameManager, this, "GameWindow.fxml");
        initializeStage(controller);
    }
    public void showMainWindow(){
        BaseController controller = new MainWindowController(gameManager, this, "MainWindow.fxml");
        initializeStage(controller);
    }

    public  void closeStage(Stage stageToClose){
        stageToClose.close();
        activeStages.remove(stageToClose);
    }


    private void initializeStage(BaseController baseController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(baseController.getFxmlName()));
        fxmlLoader.setController(baseController);
        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Spelling Bee");
        stage.setScene(scene);
        stage.setOnCloseRequest(baseController.closeStageEventHandler);
        stage.setOnShowing(baseController.showingStageEventHandler);
        baseController.setStage(stage);
        stage.show();

        activeStages.add(stage);
    }
}
