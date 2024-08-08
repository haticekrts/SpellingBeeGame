package com.spellingbee.controllers;

import com.spellingbee.GameManager;
import com.spellingbee.models.ModelState;
import com.spellingbee.views.ViewFactory;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class GameWindowController extends BaseController implements Initializable {
    public GameWindowController(GameManager gameManager, ViewFactory viewFactory, String fxmlName) {
        super(gameManager, viewFactory, fxmlName);
    }

    @FXML
    void closeWindowAction(ActionEvent event){
        viewFactory.closeStage(getStage());
    }
    @FXML
    void openMainWindowAction(ActionEvent event){
        viewFactory.closeStage(getStage());
        viewFactory.showMainWindow();
    }

    private int score=0;

    @FXML
    private TextField inputText;
    @FXML
    private Label errorMessage;
    @FXML
    private Label scoreLbl;
    @FXML
    private ListView<String> rspList;

    @FXML
    void inputButtonAction(ActionEvent event) {
        Button button = (Button) event.getSource();
        inputText.setText(inputText.getText()+button.getText());
    }

    @FXML
    void deleteCharAction(ActionEvent event){
        var text = inputText.getText();
        if(text.length()>0)
            inputText.setText(text.substring(0,text.length()-1));
    }
    @FXML
    void refreshButtonAction(ActionEvent event){
        List<String> chars = getOthersButton().stream().map(btn->btn.getText()).collect(Collectors.toList());
        Collections.shuffle(chars);
        for (int i = 0; i < chars.size(); i++) {
            getOthersButton().get(i).setText(chars.get(i));
        }
    }

    @FXML
    void enterButtonAction(ActionEvent event){
        boolean state = true;
        var enterText = inputText.getText();
        var centerText = getCenterButton().getText();
        var othersText = getOthersButton().stream().map(btn->btn.getText()).collect(Collectors.toList());
        othersText.add(centerText);

        var model = ModelState.Validate(enterText);
        if(!model.isValid()){
            errorMessage.setText(model.getMessage());
            state=false;
        }

        if(!gameManager.isMacthedString(enterText)){
            errorMessage.setText("Sözcük sözlükte yok !");
            state=false;
        }


        if(enterText.length()<4){
            errorMessage.setText("Kelime en az dört harf içermiyor !");
            state=false;
        }

        if(!enterText.contains(centerText)){
            errorMessage.setText("Sözlüğe ortadaki harf dahil değil !");
            state=false;
        }

        for (var otherString: Arrays.stream(enterText.split("")).toList()){
            if(!othersText.stream().collect(Collectors.joining("")).contains(otherString)){
                errorMessage.setText("Kelime arı kovanında olmayan harfleri içeriyor !");
                state=false;
            }
        }

        if(rspList.getItems().contains(enterText)){
            errorMessage.setText("Kullanıcı sözlüğü zaten buldu !");
            state=false;
        }





        if(state){
            rspList.getItems().add(enterText);
            
            boolean isPangram = true;
            for (var val : othersText){
                if(!enterText.contains(val)){
                    isPangram=false;
                }
            }
            if(isPangram)
                score+=7;

            score+=enterText.length()-3;
            scoreLbl.setText(rspList.getItems().size()+" kelime buldunuz, "+score+" puan aldınız.");
            inputText.setText("");
        }

    }

    @FXML
    void keyboardEventAction(KeyEvent event) {
        if(event.getCode().equals(KeyCode.BACK_SPACE)){
            deleteCharAction(new ActionEvent());
        } else if (event.getCode().equals(KeyCode.ENTER)) {
            enterButtonAction(new ActionEvent());
        } else if (event.getText().length()>0 || event.getText().length()<2) {
            if(Character.isLetter(event.getText().charAt(0))){
                var text = event.getText().toUpperCase();
                var model = ModelState.Validate(text);
                errorMessage.setText(model.getMessage());
                inputText.setText(inputText.getText()+text);
            }
        }
    }

    @FXML
    void onInputText(StringProperty property, String oldValue, String newValue) {
        newValue = newValue.toUpperCase();
        var model = ModelState.Validate(newValue);
        inputText.setText(newValue);
        errorMessage.setText(model.getMessage());
    }

    private Button getCenterButton(){
        return (Button)querySelector(".hexagon-button-gold");
    }

    private List<Button> getOthersButton(){
        ArrayList<Button> buttons = new ArrayList<Button>();
        Button centerButton = getCenterButton();
        for (Node node : querySelectorAll(".hexagon-button")){
            if((node instanceof Button) && (Button)node != centerButton){
                buttons.add((Button) node);
            }
        }
        return buttons;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showingStageEventHandler= event->{
            gameInitialize();
        };
    }

    private void gameInitialize(){
        errorMessage.setTextFill(Color.color(1,0,0));
        inputText.requestFocus();
        getCenterButton().setText(gameManager.getCenterChar());
        var buttons = getOthersButton();
        var chars = gameManager.getOtherChars().toCharArray();
        for (int i=0;i<buttons.size();i++){
            buttons.get(i).setText(Character.toString(chars[i]));
        }
    }



}
