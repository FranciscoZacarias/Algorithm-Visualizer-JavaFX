/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;

import Model.Grid;
import Model.Tile;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author frank
 */
public class View implements Observer
{
    // Window dimensions
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    
    // Tile Dimensions
    // Tile amount must be odd numbers! 
    private final int X_TILES = 49;//49;
    private final int Y_TILES = 33; //33;
    private final int TILE_SIZE = 20; //20
    
    // JavaFX Scene Nodes
    private final ComboBox tbNodeBox;
    private final Button tbBtnRun;
    private final Button tbBtnClear;
    private final Button tbBtnExit;
    private final Button tbBtnAddWeights;
    private final Button tbBtnMaze;
    private final ComboBox tbAlgorithmBox;
    private final ComboBox tbMazeGenBox;
    
    // Grid
    private final VBox leftPane;
    private final Pane paneGrid;
    
    // View-Model
    private final Grid model;
    private final Scene scene;
    
    // Attributes
    private final double leftPanelSize = 0.20;
    private final Font defaultFont = Font.font("Verdana", 14);
    private final String defaultHboxStyle = "-fx-padding: 10;" 
        + "-fx-border-style: solid inside;"
        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
        + "-fx-border-radius: 4;" + "-fx-border-color: lightgray;";
    
    public View(Grid model)
    {
        this.model = model;
        this.paneGrid = new Pane();
        
        // Region: LeftPane
        this.leftPane = new VBox();
        this.leftPane.setPadding(new Insets(5, 5, 5, 5));
        this.leftPane.setSpacing(10);
        
        HBox hboxCreateGrid = new HBox(5);
        /** TODO: add x and y tile amount, tile size and create grid button */
        hboxCreateGrid.getChildren().addAll();
        
        HBox hboxNodeBox = new HBox(5);
        hboxNodeBox.setAlignment(Pos.CENTER);
        hboxNodeBox.setStyle(defaultHboxStyle);
        Text txtNodeBox = new Text("Tile Picker: ");
        txtNodeBox.setFont(defaultFont);
        tbNodeBox = new ComboBox(FXCollections.observableArrayList(Tile.Type.values()));
        tbNodeBox.getItems().remove(Tile.Type.VISITED);
        tbNodeBox.getItems().remove(Tile.Type.PATH);
        tbNodeBox.getItems().remove(Tile.Type.HIGHLIGHT);
        tbNodeBox.getSelectionModel().selectFirst();
        tbNodeBox.setTooltip(new Tooltip("Tile Type picker"));
        hboxNodeBox.getChildren().addAll(txtNodeBox, tbNodeBox);
        
        HBox hboxAlgorithmBox = new HBox(5);
        hboxAlgorithmBox.setAlignment(Pos.CENTER);
        hboxAlgorithmBox.setStyle(defaultHboxStyle);
        tbAlgorithmBox = new ComboBox(FXCollections.observableArrayList(Grid.Algorithms.values()));
        tbAlgorithmBox.getSelectionModel().selectFirst();
        tbAlgorithmBox.setTooltip(new Tooltip("Algorithm picker"));
        tbBtnRun = new Button("RUN");
        tbBtnRun.setTooltip(new Tooltip("Run Pathfinding Algorithm"));
        hboxAlgorithmBox.getChildren().addAll(tbAlgorithmBox, tbBtnRun);
        
        HBox hboxAddWeights = new HBox(5);
        hboxAddWeights.setAlignment(Pos.CENTER);
        hboxAddWeights.setStyle(defaultHboxStyle);
        tbBtnAddWeights = new Button("ADD RANDOM WEIGHTS");
        tbBtnAddWeights.setTooltip(new Tooltip("Adds random weights to all tiles"));
        hboxAddWeights.getChildren().add(tbBtnAddWeights);
        
        HBox hboxMazeGen = new HBox(5);
        hboxMazeGen.setAlignment(Pos.CENTER);
        hboxMazeGen.setStyle(defaultHboxStyle);
        tbMazeGenBox = new ComboBox(FXCollections.observableArrayList(Grid.MazeGen.values()));
        tbMazeGenBox.getSelectionModel().selectFirst();
        tbMazeGenBox.setTooltip(new Tooltip("Maze generation algorithm picker"));
        tbBtnMaze = new Button("MAZE GEN");
        tbBtnMaze.setTooltip(new Tooltip("GENERATES A RANDOM MAZE"));
        hboxMazeGen.getChildren().addAll(tbMazeGenBox, tbBtnMaze);
        
        HBox hboxUtilBtns = new HBox(5);
        hboxUtilBtns.setAlignment(Pos.CENTER);
        hboxUtilBtns.setStyle(defaultHboxStyle);
        tbBtnClear = new Button("CLEAR");
        tbBtnClear.setTooltip(new Tooltip("Resets all tiles to empty and no weight"));
        tbBtnExit = new Button("EXIT");
        tbBtnExit.setTooltip(new Tooltip("Exits the application"));
        hboxUtilBtns.getChildren().addAll(tbBtnClear, tbBtnExit);
        
        leftPane.getChildren().addAll(hboxCreateGrid, hboxNodeBox, hboxAlgorithmBox, hboxAddWeights, hboxMazeGen, hboxUtilBtns);
        // EndRegion: RightPane

        // Initializes the grid
        model.gridInit(X_TILES, Y_TILES, TILE_SIZE);
        
        //  Create scene
        this.scene = new Scene(initComponents(), WIDTH, HEIGHT);
    }
    
    public void setTriggers(Controller controller)
    {
        // Changes type of tile on click
        tbNodeBox.setOnAction((event) -> 
        {
            FXCollections.observableArrayList(Tile.Type.values()).stream().filter((item) -> (tbNodeBox.getValue().toString().equals(item.toString()))).forEachOrdered((item) ->
            {
                controller.doChangeClickType(item);
            });
        });
        
        // Clear button clears the grid
        tbBtnClear.setOnAction((event) ->
        {
            this.enableButtons();
            controller.doClearGrid();
        });
        
        // Exits the application
        tbBtnExit.setOnAction((event) -> 
        {
            Platform.exit();
        });
        
        // Adds random weights to all Tiles
        tbBtnAddWeights.setOnAction((event) -> 
        {
            controller.doAddRandomWeights();
        });
        
        // Generates a random maze
        tbBtnMaze.setOnAction((event) ->
        {
            FXCollections.observableArrayList(Grid.MazeGen.values()).stream().filter((item) -> (tbMazeGenBox.getValue().toString().equals(item.toString()))).forEachOrdered((item) ->
            {
                controller.doGenerateMaze(item);
            });
        });
        
        tbBtnRun.setOnAction((event) -> 
        {
            FXCollections.observableArrayList(Grid.Algorithms.values()).stream().filter((item) -> (tbAlgorithmBox.getValue().toString().equals(item.toString()))).forEachOrdered((item) ->
            {
                try
                {
                    boolean success = controller.doShortestPathAlgorithm(item);
                    if(success) lockButtons();
                } 
                catch (InterruptedException ex)
                {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
    }
    
    /**
     * Returns this view's scene
     * @return Scene
     */
    public Scene getScene()
    {
        return this.scene;
    }
    
    /**
     * Disables all buttons in toolBar, except CLEAR button,
     * which is the only way to enable them back
     */
    private void lockButtons()
    {
        for(Node node : this.leftPane.getChildren())
        {
            if(node instanceof Button)
            {
                Button btn = (Button)node;
                if(btn == tbBtnClear) continue;
                btn.setDisable(true);
            }
        }
    }
    
    /**
     * Enables all buttons in the tool bar
     */
    private void enableButtons()
    {
        for(Node node : this.leftPane.getChildren())
        {
            if(node instanceof Button)
            {
                Button btn = (Button)node;
                if(btn == tbBtnClear) continue;
                btn.setDisable(false);
            }
        }
    }
    
    /**
     * Initializes the components of the view. Meant to be called once in constructor
     * @return Pane with components initialized
     */
    private SplitPane initComponents()
    {
        VBox root = new VBox();
        
        fillGrid(model.getGrid());
        root.getChildren().add(this.paneGrid);
        
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(this.leftPane, root);
        
        this.leftPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(this.leftPanelSize));
        this.leftPane.minWidthProperty().bind(splitPane.widthProperty().multiply(this.leftPanelSize));
        
        return splitPane;
    }
    
    /**
     * Build the grid with the model values
     * @param tiles 
     */
    private void fillGrid(Tile[][] tiles)
    {
        for(Tile[] row : tiles)
        {
            for(Tile tile: row)
            {
                paneGrid.getChildren().add(tile.getStackPane());
            }
        }
    }

    /**
     * Listens for changes in the model
     * @param o Observable object
     * @param arg Object argument
     */
    @Override
    public void update(Observable o, Object arg)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
