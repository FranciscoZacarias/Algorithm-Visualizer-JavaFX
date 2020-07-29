/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;

import Model.Grid;
import Model.PathfindingStatistics;
import Model.Tile;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author frank
 */
public class View implements Observer
{
    // Window dimensions
    private final int WIDTH = 1285;
    private final int HEIGHT = 702;
    
    // JavaFX Scene Nodes
    private final TextField txtXTiles;
    private final TextField txtYTiles;
    private final TextField txtTileSize;
    // REGION: Stats
    private final Separator separatorStats;
    private final Text txtStatsTitle;
    private final Text txtStatsTitleValue;
    private final Text txtStatsTilesTotal;
    private final Text txtStatsTilesTotalValue;
    private final Text txtStatsTilesVisited;
    private final Text txtStatsTilesVisitedValue;
    private final Text txtStatsPathFound;
    private final Text txtStatsPathFoundValue;
    private final Text txtStatsPathCost;
    private final Text txtStatsPathCostValue;
    private final Text txtStatsElapsedTime;
    private final Text txtStatsElapsedTimeValue;
    // ENDREGION: Stats
    private final Text txtAlgorithms;
    private final Button btnRun;
    private final Button btnClear;
    private final Button btnExit;
    private final Button btnAddWeights;
    private final Button btnAddWalls;
    private final Button btnMaze;
    private final Button btnCreateGrid;
    private final ComboBox cbAlgorithmBox;
    private final ComboBox cbMazeGenBox;
    private final ComboBox cbNodeBox;
    
    // Grid
    private final VBox leftPane;
    private final Pane parentGridPane;
    private Pane gridPane;
    
    // View-Model
    private final Grid model;
    private final Scene scene;
    
    // Attributes
    private final String defaultXSize = "51";
    private final String defaultYSize = "35";
    private final String defaultTileSize = "20";
    private final double leftPanelSize = 0.20;
    private final Font defaultFont = Font.font("Courier New", 14);
    private final String defaultHboxStyle = "-fx-padding: 10;" 
        + "-fx-border-style: solid inside;"
        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
        + "-fx-border-radius: 4;" + "-fx-border-color: lightgray;";
    
    public View(Grid model)
    {
        this.model = model;
        this.parentGridPane = new Pane();
        this.gridPane = null;
        
        // Region: LeftPane
        this.leftPane = new VBox();
        this.leftPane.setPadding(new Insets(5, 5, 5, 5));
        this.leftPane.setSpacing(10);
                
        // Create Grid Pane
        VBox vboxCreateGrid = new VBox(5);
        vboxCreateGrid.setStyle(defaultHboxStyle);
        GridPane createPane = new GridPane();
        createPane.setHgap(5);
        createPane.setPadding(new Insets(5, 5, 5, 5));
        createPane.add(new Text("X: "), 0, 0);
        txtXTiles = new TextField(defaultXSize);
        createPane.add(txtXTiles, 1, 0);
        createPane.add(new Text("Y: "), 2, 0);
        txtYTiles = new TextField(defaultYSize);
        createPane.add(txtYTiles, 3, 0);
        createPane.add(new Text("Size: "), 4, 0);
        txtTileSize = new TextField(defaultTileSize); 
        createPane.add(txtTileSize, 5, 0);
        HBox hboxCreateBtn = new HBox(5);
        hboxCreateBtn.setAlignment(Pos.CENTER);
        btnCreateGrid = new Button("CREATE NEW GRID");
        btnCreateGrid.setTooltip(new Tooltip("Overrides previous grid"));
        hboxCreateBtn.getChildren().add(btnCreateGrid);
        vboxCreateGrid.getChildren().addAll(createPane, hboxCreateBtn);
        
        //Tile Picker Pane
        HBox hboxNodeBox = new HBox(5);
        hboxNodeBox.setAlignment(Pos.CENTER);
        hboxNodeBox.setStyle(defaultHboxStyle);
        Text txtNodeBox = new Text("Tile Picker: ");
        txtNodeBox.setFont(defaultFont);
        cbNodeBox = new ComboBox(FXCollections.observableArrayList(Tile.Type.values()));
        cbNodeBox.getItems().remove(Tile.Type.VISITED);
        cbNodeBox.getItems().remove(Tile.Type.PATH);
        cbNodeBox.getItems().remove(Tile.Type.HIGHLIGHT);
        cbNodeBox.getItems().remove(Tile.Type.VISITED_DENSE);
        cbNodeBox.getItems().remove(Tile.Type.VISITED_LIGHT);
        cbNodeBox.getItems().remove(Tile.Type.VISITED_MAX);
        cbNodeBox.getItems().remove(Tile.Type.VISITED_MEDIUM);
        cbNodeBox.getSelectionModel().selectFirst();
        cbNodeBox.setTooltip(new Tooltip("Tile Type picker"));
        hboxNodeBox.getChildren().addAll(txtNodeBox, cbNodeBox);
        
        // Pathfinding Pane
        HBox hboxcbAlgorithmBox = new HBox(5);
        hboxcbAlgorithmBox.setAlignment(Pos.CENTER);
        hboxcbAlgorithmBox.setStyle(defaultHboxStyle);
        cbAlgorithmBox = new ComboBox(FXCollections.observableArrayList(Grid.Algorithms.values()));
        cbAlgorithmBox.getSelectionModel().selectFirst();
        cbAlgorithmBox.setTooltip(new Tooltip("Algorithm picker"));
        txtAlgorithms = new Text("Algorithms: ");
        txtAlgorithms.setFont(defaultFont);
        hboxcbAlgorithmBox.getChildren().addAll(txtAlgorithms, cbAlgorithmBox);
        
        // Add Weights & Walls Pane
        HBox hboxAddWeights = new HBox(5);
        hboxAddWeights.setAlignment(Pos.CENTER);
        hboxAddWeights.setStyle(defaultHboxStyle);
        btnAddWeights = new Button("ADD WEIGHTS");
        btnAddWeights.setTooltip(new Tooltip("Adds random weights to all tiles"));
        btnAddWalls = new Button("ADD WALLS");
        btnAddWalls.setTooltip(new Tooltip("Adds random walls to the grid"));
        hboxAddWeights.getChildren().addAll(btnAddWalls, btnAddWeights);
        
        // Maze Generation Pane
        HBox hboxMazeGen = new HBox(5);
        hboxMazeGen.setAlignment(Pos.CENTER);
        hboxMazeGen.setStyle(defaultHboxStyle);
        cbMazeGenBox = new ComboBox(FXCollections.observableArrayList(Grid.MazeGen.values()));
        cbMazeGenBox.getSelectionModel().selectFirst();
        cbMazeGenBox.setTooltip(new Tooltip("Maze generation algorithm picker"));
        btnMaze = new Button("MAZE GEN");
        btnMaze.setTooltip(new Tooltip("GENERATES A RANDOM MAZE"));
        hboxMazeGen.getChildren().addAll(cbMazeGenBox, btnMaze);
        
        // Util buttons Pane
        HBox hboxUtilBtns = new HBox(5);
        hboxUtilBtns.setAlignment(Pos.CENTER);
        hboxUtilBtns.setStyle(defaultHboxStyle);
        btnClear = new Button("CLEAR");
        btnClear.setTooltip(new Tooltip("Resets all tiles to empty and no weight"));
        btnExit = new Button("EXIT");
        btnExit.setTooltip(new Tooltip("Exits the application"));
        btnRun = new Button("RUN");
        btnRun.setTooltip(new Tooltip("Run Pathfinding Algorithm"));
        hboxUtilBtns.getChildren().addAll(btnRun, btnClear, btnExit);
        
        // Statistics Pane
        separatorStats = new Separator();
        VBox vboxStats = new VBox(5);
        vboxStats.setAlignment(Pos.CENTER_LEFT);
        vboxStats.setStyle(defaultHboxStyle);
        HBox hboxStatsTitle = new HBox(5);
        hboxStatsTitle.setAlignment(Pos.CENTER);
        txtStatsTitle = new Text("STATISTICS");
        txtStatsTitle.setFont(Font.font(defaultFont.getName(), FontWeight.BOLD, 20));
        txtStatsTitleValue = new Text("");
        hboxStatsTitle.getChildren().addAll(txtStatsTitle, txtStatsTitleValue);
        HBox hboxStatsTotal = new HBox(5);
        txtStatsTilesTotal = new Text("Total Tiles: ");
        txtStatsTilesTotalValue = new Text("");
        hboxStatsTotal.getChildren().addAll(txtStatsTilesTotal, txtStatsTilesTotalValue);
        HBox hboxStatsTilesVisited = new HBox(5);
        txtStatsTilesVisited = new Text("Visited Tiles: ");
        txtStatsTilesVisitedValue = new Text("");
        hboxStatsTilesVisited.getChildren().addAll(txtStatsTilesVisited, txtStatsTilesVisitedValue);
        HBox hboxStatsPathFound = new HBox(5);
        txtStatsPathFound = new Text("Path Found: ");
        txtStatsPathFoundValue = new Text("");
        hboxStatsPathFound.getChildren().addAll(txtStatsPathFound, txtStatsPathFoundValue);
        HBox hboxStatsPathCost = new HBox(5);
        txtStatsPathCost = new Text("Path Cost: ");
        txtStatsPathCostValue = new Text("");
        hboxStatsPathCost.getChildren().addAll(txtStatsPathCost, txtStatsPathCostValue);
        HBox hboxStatsElapsedTime = new HBox(5);
        txtStatsElapsedTime = new Text("Elapsed Time:");
        txtStatsElapsedTimeValue = new Text("");
        hboxStatsElapsedTime.getChildren().addAll(txtStatsElapsedTime, txtStatsElapsedTimeValue);
        vboxStats.getChildren().addAll(hboxStatsTitle, separatorStats, hboxStatsTotal, hboxStatsTilesVisited, hboxStatsPathFound, hboxStatsPathCost, hboxStatsElapsedTime);
        
        leftPane.getChildren().addAll(vboxCreateGrid, hboxNodeBox, hboxcbAlgorithmBox, hboxAddWeights, hboxMazeGen, hboxUtilBtns, vboxStats);
        // EndRegion: RightPane
        
        //  Create scene
        this.scene = new Scene(initComponents(), WIDTH, HEIGHT);
    }
    
    public void setTriggers(Controller controller)
    {
        // Changes type of tile on click
        cbNodeBox.setOnAction((event) -> 
        {
            FXCollections.observableArrayList(Tile.Type.values()).stream().filter((item) -> (cbNodeBox.getValue().toString().equals(item.toString()))).forEachOrdered((item) ->
            {
                controller.doChangeClickType(item);
            });
        });
        
        // Clear button clears the grid
        btnClear.setOnAction((event) ->
        {
            controller.doClearGrid();
        });
        
        // Exits the application
        btnExit.setOnAction((event) -> 
        {
            System.exit(0);
        });
        
        // Adds random weights to all Tiles
        btnAddWeights.setOnAction((event) -> 
        {
            controller.doAddRandomWeights();
        });
        
        btnAddWalls.setOnAction((event) -> 
        {
            controller.doAddRandomWalls();
        });
        
        // Generates a random maze
        btnMaze.setOnAction((event) ->
        {
            FXCollections.observableArrayList(Grid.MazeGen.values()).stream().filter((item) -> (cbMazeGenBox.getValue().toString().equals(item.toString()))).forEachOrdered((item) ->
            {
                if(gridPane != null)
                    controller.doGenerateMaze(item);
            });
        });
        
        // Initialized the grid
        btnCreateGrid.setOnAction((event) ->
        {
            int x = Integer.valueOf(txtXTiles.getText());
            int y = Integer.valueOf(txtYTiles.getText());
            int size = Integer.valueOf(txtTileSize.getText());
            
            if(parentGridPane.getChildren().contains(gridPane))
                parentGridPane.getChildren().remove(gridPane);
            
            // Initializes the grid
            model.gridInit(x, y, size);
            this.fillGrid(model.getGrid());
        });
        
        // Run pathfinding algorithms
        btnRun.setOnAction((event) -> 
        {
            FXCollections.observableArrayList(Grid.Algorithms.values()).stream().filter((item) -> (cbAlgorithmBox.getValue().toString().equals(item.toString()))).forEachOrdered((item) ->
            {
                try
                {
                    boolean success = controller.doShortestPathAlgorithm(item);
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
     * Creates a new grid 
     * This method was created to be called in controller constructor,
     * so we don't have to manually create a new grid on load
     */
    public void createGrid()
    {
        btnCreateGrid.fire();
    }
    
    /**
     * Initializes the components of the view. Meant to be called once in constructor
     * @return Pane with components initialized
     */
    private SplitPane initComponents()
    {
        VBox root = new VBox();
        
        //fillGrid(model.getGrid());
        root.getChildren().add(this.parentGridPane);
        
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
        this.gridPane = new Pane();
        for(Tile[] row : tiles)
        {
            for(Tile tile: row)
            {
                gridPane.getChildren().add(tile.getStackPane());
            }
        }
        this.parentGridPane.getChildren().add(gridPane);
    }

    /**
     * Listens for changes in the model
     * @param o Observable object
     * @param arg Object argument
     */
    @Override
    public void update(Observable o, Object arg)
    {
        // If it gets updated by the grid
        if(o instanceof Grid)
        {
            // If grid sends the statisics
            if(arg instanceof PathfindingStatistics)
            {
                PathfindingStatistics stats = (PathfindingStatistics)arg;
                
                this.txtStatsTilesTotalValue.setText(String.valueOf(stats.getTilesTotal()));
                this.txtStatsTilesVisitedValue.setText(String.valueOf(stats.getTilesVisited()));
                this.txtStatsPathFoundValue.setText((stats.isPathFound()) ? "Yes" : "No");
                this.txtStatsPathCostValue.setText(String.valueOf(stats.getPathCost()));
                this.txtStatsElapsedTimeValue.setText(String.format("%.4f Milliseconds", stats.getElapsedTime() * Math.pow(10, -6)));
            }
        }
    }
}
