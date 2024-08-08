package com.spellingbee.controllers;

import com.spellingbee.GameManager;
import com.spellingbee.views.ViewFactory;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.util.Set;

public abstract class BaseController {
    public EventHandler<WindowEvent> closeStageEventHandler;
    public EventHandler<WindowEvent> showingStageEventHandler;
    protected GameManager gameManager;
    protected ViewFactory viewFactory;
    private String fxmlName;
    private Stage stage;
    public BaseController(GameManager gameManager, ViewFactory viewFactory, String fxmlName) {
        this.gameManager = gameManager;
        this.viewFactory = viewFactory;
        this.fxmlName = fxmlName;
    }
    public Stage getStage(){
        return stage;
    }
    public void setStage(Stage stage){
        this.stage=stage;
    }
    public String getFxmlName() {
        return fxmlName;
    }


    public Set<Node> querySelectorAll(String selector){
        return stage.getScene().getRoot().lookupAll(selector);
    }

    public Node querySelector(String selector){
        return stage.getScene().getRoot().lookup(selector);
    }

}
