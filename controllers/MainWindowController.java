package com.spellingbee.controllers;

import com.spellingbee.GameManager;
import com.spellingbee.models.ModelState;
import com.spellingbee.views.ViewFactory;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainWindowController extends BaseController implements Initializable {
    private boolean centerCharValid = false;
    private boolean otherCharsValid = false;
    public MainWindowController(GameManager gameManager, ViewFactory viewFactory, String fxmlName) {
        super(gameManager, viewFactory, fxmlName);
    }

    @FXML
    private Label errorMessageLbl;

    @FXML
    private TextField centerCharTxt;

    @FXML
    private TextField otherCharsTxt;

    @FXML
    private Button manuelGameBtn;

    @FXML
    private Button autoGameBtn;


    @FXML
    void centerCharAction(StringProperty property, String oldValue, String newValue) {
        centerCharValid=false;
        manuelGameBtn.setDisable(true);
        newValue = newValue.toUpperCase();
        var model = ModelState.Validate(newValue);
        centerCharTxt.setText(newValue);
        errorMessageLbl.setText(model.getMessage());
        if(newValue.length()>1 || newValue.length()!=1){
            errorMessageLbl.setText("Lütfen sadece bir karakter girin !");
        }
        else{
            if(model.isValid() && !otherCharsTxt.getText().contains(centerCharTxt.getText())){
                gameManager.setCenterChar(newValue);
                centerCharValid=true;
                if(otherCharsValid)
                    manuelGameBtn.setDisable(false);
            }
        }
    }

    @FXML
    void otherCharsAction(StringProperty property, String oldValue, String newValue) {
        otherCharsValid=false;
        manuelGameBtn.setDisable(true);
        newValue = newValue.toUpperCase();
        var model = ModelState.Validate(newValue,false);
        otherCharsTxt.setText(newValue);
        errorMessageLbl.setText(model.getMessage());
        if(newValue.length()>6 || newValue.length()<1 || newValue.length()!=6){
            errorMessageLbl.setText("Lütfen sadece altı karakter girin !");
        }
        else{
            if(model.isValid() && !otherCharsTxt.getText().contains(centerCharTxt.getText())){
                gameManager.setCenterChar(newValue);
                otherCharsValid=true;
                if(centerCharValid)
                    manuelGameBtn.setDisable(false);
            }
        }
    }

    @FXML
    void manuelGameBtnAction(ActionEvent event) {
        gameManager.setCenterChar(centerCharTxt.getText());
        gameManager.setOtherChars(otherCharsTxt.getText());
        viewFactory.showGameWindow();
        viewFactory.closeStage(getStage());
    }

    @FXML
    void autoGameBtnAction(ActionEvent event) {
        String centerChar = null;
        List<Character> pangramChars = null;
        while (centerChar==null){
            pangramChars = gameManager.isAvailablePangramChars();
            centerChar = gameManager.validateSolutionAndCenterChar(pangramChars);
        }

        final String removeCenter = centerChar;
        gameManager.setCenterChar(centerChar.toUpperCase());
        String otherChars= pangramChars.stream().filter(pangram-> Character.toLowerCase(pangram)!=Character.toLowerCase(removeCenter.charAt(0)))
                .collect(Collectors.toList()).stream().map(c->String.valueOf(c).toUpperCase()).collect(Collectors.joining());
        gameManager.setOtherChars(otherChars);
        viewFactory.showGameWindow();
        viewFactory.closeStage(getStage());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMessageLbl.setTextFill(Color.color(1,0,0));
        manuelGameBtn.setDisable(true);
    }
}
