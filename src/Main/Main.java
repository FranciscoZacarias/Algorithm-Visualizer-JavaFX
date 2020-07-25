/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.Grid;
import ViewController.Controller;
import ViewController.View;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author frank
 */
public class Main extends Application
{
    private final String APP_TITLE = "Algorithm Visualizer - Francisco Zacarias";
    
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        View app = createApp();
        
        primaryStage.setScene(app.getScene());
        primaryStage.setTitle(APP_TITLE);
        primaryStage.show();
    }
    
    private View createApp()
    {
        // Create Model
        Grid model = new Grid();
        // Create View
        View view = new View(model);
        // Create Controller
        Controller controller = new Controller(model, view);
        
        return view;
    }
    
}
