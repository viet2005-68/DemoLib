package org.example.demodb;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class StageMachine {
    URL resourse = null;
    private static StageMachine instance;
    private StageMachine(){}
    public static StageMachine getInstance(){
        if(instance == null) {
            instance = new StageMachine();
        }
        return instance;
    }

    public void changeScene(String file, ActionEvent event, int width, int height) throws IOException {
        URL resourse = getClass().getResource(file);
        if(resourse != null) {
            FXMLLoader loader = new FXMLLoader(resourse);
            Parent root = loader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root,width,height);
            stage.setScene(scene);
            stage.show();
        }
    }
}
