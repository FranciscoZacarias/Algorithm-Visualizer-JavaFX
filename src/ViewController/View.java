/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;

import Model.Grid;
import Model.PathfindingStatistics;
import Model.Tile;
import Strategy.MazeGenerationStrategy.MazeGenerationStrategy;
import Strategy.PathfindingStrategy.AStarStrategy;
import Strategy.PathfindingStrategy.PathfindingStrategy;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    private final int WIDTH = 1380;
    private final int HEIGHT = 720;
    
    // JavaFX Scene Nodes
    private final TextField txtXTiles;
    private final TextField txtYTiles;
    private final TextField txtTileSize;
    // REGION: Stats
    private final Separator separatorStats;
    private final Separator separatorAlgo;
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
    private final Text txtStatsWallAmount;
    private final Text txtStatsWallAmountValue;
    private final Text txtStatsElapsedTime;
    private final Text txtStatsElapsedTimeValue;
    // ENDREGION: Stats
    private final Text txtAlgorithms;
    private final Text txtAlgorithmsHeuristic;
    private final Text txtObstacles;
    private final Text txtMaze;
    private final Button btnRun;
    private final Button btnClear;
    private final Button btnExit;
    private final Button btnAddWeights;
    private final Button btnAddWalls;
    private final Button btnMaze;
    private final Button btnCreateGrid;
    private final ComboBox cbAlgorithmBox;
    private final ComboBox cbHeuristicBox;
    private final ComboBox cbMazeGenBox;
    private final ComboBox cbNodeBox;
    private final CheckBox ckbShowCoords;
    private final CheckBox ckbSetTileStroke;
    
    // Grid
    private final VBox leftPane;
    private final Pane parentGridPane;
    private Pane gridPane;
    
    // View-Model
    private final Grid model;
    private final Scene scene;
    
    // Attributes
    private final int padding = 2;
    private final String defaultXSize = "33";//"51";
    private final String defaultYSize = "21";//"31";
    private final String defaultTileSize = "32";//"20";
    private final double leftPanelSize = 0.20;
    private final Font defaultFont = Font.font("Courier New", 14);
    private final String defaultHboxStyle = "-fx-padding: 10;" 
        + "-fx-border-style: solid inside;"
        + "-fx-border-width: 2;" + "-fx-border-insets: " + padding + ";"
        + "-fx-border-radius: 4;" + "-fx-border-color: lightgray;";
    
    public View(Grid model)
    {
        this.model = model;
        this.parentGridPane = new Pane();
        this.gridPane = null;
        
        // Region: LeftPane
        this.leftPane = new VBox();
        this.leftPane.setPadding(new Insets(padding, padding, padding, padding));
        this.leftPane.setSpacing(10);
                
        // Create Grid Pane
        VBox vboxCreateGrid = new VBox(padding);
        vboxCreateGrid.setStyle(defaultHboxStyle);
        GridPane createPane = new GridPane();
        createPane.setHgap(5);
        createPane.setPadding(new Insets(padding, padding, padding, padding));
        createPane.add(new Text("X: "), 0, 0);
        txtXTiles = new TextField(defaultXSize);
        createPane.add(txtXTiles, 1, 0);
        createPane.add(new Text("Y: "), 2, 0);
        txtYTiles = new TextField(defaultYSize);
        createPane.add(txtYTiles, 3, 0);
        createPane.add(new Text("Size: "), 4, 0);
        txtTileSize = new TextField(defaultTileSize); 
        createPane.add(txtTileSize, 5, 0);
        HBox hboxCreateBtn = new HBox(padding);
        hboxCreateBtn.setAlignment(Pos.CENTER);
        btnCreateGrid = new Button("CREATE NEW GRID");
        btnCreateGrid.setTooltip(new Tooltip("Overrides previous grid"));
        hboxCreateBtn.getChildren().addAll(btnCreateGrid);
        HBox hboxShowCoords = new HBox(padding);
        hboxShowCoords.setAlignment(Pos.CENTER);
        ckbShowCoords = new CheckBox("Tile coordinates");
        ckbShowCoords.setTooltip(new Tooltip("Toggle tiles to show their coordinates"));
        ckbSetTileStroke = new CheckBox("Tile Border");
        ckbSetTileStroke.setTooltip(new Tooltip("Toggles tiles to show their border"));
        ckbSetTileStroke.setSelected(true);
        hboxShowCoords.getChildren().addAll(ckbShowCoords, ckbSetTileStroke);
        vboxCreateGrid.getChildren().addAll(createPane, hboxCreateBtn, hboxShowCoords);
        
        //Tile Picker Pane
        HBox hboxNodeBox = new HBox(padding);
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
        separatorAlgo = new Separator(Orientation.VERTICAL);
        VBox vboxAlgorithmBox = new VBox(padding);
        vboxAlgorithmBox.setAlignment(Pos.CENTER);
        vboxAlgorithmBox.setStyle(defaultHboxStyle);
        HBox hboxAlgorithmTxt = new HBox(padding);
        hboxAlgorithmTxt.setAlignment(Pos.BASELINE_CENTER);
        txtAlgorithms = new Text("Algorithms");
        txtAlgorithms.setFont(defaultFont);
        txtAlgorithmsHeuristic = new Text("Heuristics");
        txtAlgorithmsHeuristic.setFont(defaultFont);
        hboxAlgorithmTxt.getChildren().addAll(txtAlgorithms, separatorAlgo, txtAlgorithmsHeuristic);
        HBox hboxcbAlgorithmBox = new HBox(padding);
        hboxcbAlgorithmBox.setAlignment(Pos.CENTER);
        cbAlgorithmBox = new ComboBox(FXCollections.observableArrayList(PathfindingStrategy.Algorithms.values()));
        cbAlgorithmBox.getSelectionModel().selectFirst();
        cbAlgorithmBox.setTooltip(new Tooltip("Algorithm picker"));
        cbHeuristicBox = new ComboBox(FXCollections.observableArrayList(AStarStrategy.Heuristic.values()));
        cbHeuristicBox.getSelectionModel().selectFirst();
        cbHeuristicBox.setTooltip(new Tooltip("Heuristic for A* algorithm"));
        cbHeuristicBox.setDisable(true);
        hboxcbAlgorithmBox.getChildren().addAll(cbAlgorithmBox, cbHeuristicBox);
        vboxAlgorithmBox.getChildren().addAll(hboxAlgorithmTxt, hboxcbAlgorithmBox);
        
        // Add Weights & Walls Pane
        VBox vboxObstacles = new VBox(padding);
        vboxObstacles.setStyle(defaultHboxStyle);
        HBox hboxObstacles = new HBox(padding);
        hboxObstacles.setAlignment(Pos.CENTER);
        txtObstacles = new Text("ADD RANDOM OBSTACLES");
        txtObstacles.setFont(defaultFont);
        hboxObstacles.getChildren().add(txtObstacles);
        HBox hboxAddWeights = new HBox(padding);
        hboxAddWeights.setAlignment(Pos.CENTER);
        btnAddWeights = new Button("WEIGHTS");
        btnAddWeights.setTooltip(new Tooltip("Adds random weights to all tiles"));
        btnAddWalls = new Button("WALLS");
        btnAddWalls.setTooltip(new Tooltip("Adds random walls to the grid"));
        hboxAddWeights.getChildren().addAll(btnAddWalls, btnAddWeights);
        vboxObstacles.getChildren().addAll(hboxObstacles, hboxAddWeights);
        
        // Maze Generation Pane
        VBox vboxMaze = new VBox(padding);
        vboxMaze.setAlignment(Pos.CENTER);
        vboxMaze.setStyle(defaultHboxStyle);
        HBox hboxMaze = new HBox(padding);
        hboxMaze.setAlignment(Pos.CENTER);
        txtMaze = new Text("GENERATE MAZE");
        txtMaze.setFont(defaultFont);
        hboxMaze.getChildren().add(txtMaze);
        HBox hboxMazeGen = new HBox(padding);
        hboxMazeGen.setAlignment(Pos.CENTER);
        cbMazeGenBox = new ComboBox(FXCollections.observableArrayList(MazeGenerationStrategy.MazeGen.values()));
        cbMazeGenBox.getSelectionModel().selectFirst();
        cbMazeGenBox.setTooltip(new Tooltip("Maze generation algorithm picker"));
        btnMaze = new Button("MAZE GEN");
        btnMaze.setTooltip(new Tooltip("GENERATES A RANDOM MAZE"));
        hboxMazeGen.getChildren().addAll(cbMazeGenBox, btnMaze);
        vboxMaze.getChildren().addAll(hboxMaze, hboxMazeGen);
        
        // Util buttons Pane
        HBox hboxUtilBtns = new HBox(padding);
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
        VBox vboxStats = new VBox(padding);
        vboxStats.setAlignment(Pos.CENTER_LEFT);
        vboxStats.setStyle(defaultHboxStyle);
        HBox hboxStatsTitle = new HBox(padding);
        hboxStatsTitle.setAlignment(Pos.CENTER);
        txtStatsTitle = new Text("STATISTICS");
        txtStatsTitle.setFont(Font.font(defaultFont.getName(), FontWeight.BOLD, 20));
        txtStatsTitleValue = new Text("");
        hboxStatsTitle.getChildren().addAll(txtStatsTitle, txtStatsTitleValue);
        HBox hboxStatsWalls = new HBox(padding);
        txtStatsWallAmount = new Text("Total Walls: ");
        txtStatsWallAmountValue = new Text("");
        hboxStatsWalls.getChildren().addAll(txtStatsWallAmount, txtStatsWallAmountValue);
        HBox hboxStatsTotal = new HBox(padding);
        txtStatsTilesTotal = new Text("Total Tiles: ");
        txtStatsTilesTotalValue = new Text("");
        hboxStatsTotal.getChildren().addAll(txtStatsTilesTotal, txtStatsTilesTotalValue);
        HBox hboxStatsTilesVisited = new HBox(padding);
        txtStatsTilesVisited = new Text("Visited Tiles: ");
        txtStatsTilesVisitedValue = new Text("");
        hboxStatsTilesVisited.getChildren().addAll(txtStatsTilesVisited, txtStatsTilesVisitedValue);
        HBox hboxStatsPathFound = new HBox(padding);
        txtStatsPathFound = new Text("Path Found: ");
        txtStatsPathFoundValue = new Text("");
        hboxStatsPathFound.getChildren().addAll(txtStatsPathFound, txtStatsPathFoundValue);
        HBox hboxStatsPathCost = new HBox(padding);
        txtStatsPathCost = new Text("Path Cost: ");
        txtStatsPathCostValue = new Text("");
        hboxStatsPathCost.getChildren().addAll(txtStatsPathCost, txtStatsPathCostValue);
        HBox hboxStatsElapsedTime = new HBox(padding);
        txtStatsElapsedTime = new Text("Elapsed Time:");
        txtStatsElapsedTimeValue = new Text("");
        hboxStatsElapsedTime.getChildren().addAll(txtStatsElapsedTime, txtStatsElapsedTimeValue);
        vboxStats.getChildren().addAll(hboxStatsTitle, separatorStats, hboxStatsTotal, hboxStatsWalls, hboxStatsTilesVisited, hboxStatsPathFound, hboxStatsPathCost, hboxStatsElapsedTime);
        
        leftPane.getChildren().addAll(vboxCreateGrid, hboxNodeBox, vboxAlgorithmBox, vboxObstacles, vboxMaze, vboxStats, hboxUtilBtns);
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
        
        // Locks heuristic if algorithm is not A*
        cbAlgorithmBox.setOnAction((event) ->
        {
            cbHeuristicBox.setDisable(!(cbAlgorithmBox.getSelectionModel().getSelectedItem().toString().contains("AStar")));
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
        
        // Adds random walls
        btnAddWalls.setOnAction((event) -> 
        {
            controller.doAddRandomWalls();
        });
        
        // Generates a random maze
        btnMaze.setOnAction((event) ->
        {
            FXCollections.observableArrayList(MazeGenerationStrategy.MazeGen.values()).stream().filter((item) -> (cbMazeGenBox.getValue().toString().equals(item.toString()))).forEachOrdered((item) ->
            {
                if(gridPane != null)
                    controller.doGenerateMaze(item);
            });
        });
        
        // Initialized the grid
        btnCreateGrid.setOnAction((event) ->
        {
            // Makes sure maze is only generated with odd x and y
            int x = Integer.valueOf(txtXTiles.getText());
            x = (x % 2 == 0) ? x - 1 : x; 
            int y = Integer.valueOf(txtYTiles.getText());
            y = (y % 2 == 0) ? y - 1 : y;
            int size = Integer.valueOf(txtTileSize.getText());
            
            if(parentGridPane.getChildren().contains(gridPane))
                parentGridPane.getChildren().remove(gridPane);
            
            // Initializes the grid
            model.gridInit(x, y, size);
            this.fillGrid(model.getGrid());
        });
        
        // Toggles tile coordinates to show on screen
        ckbShowCoords.setOnAction((event) ->
        {
            controller.doToggleTileCoords(ckbShowCoords.isSelected());
        });
        
        ckbSetTileStroke.setOnAction((event) -> 
        {
            controller.doToggleTileBorder(ckbSetTileStroke.isSelected());
        });
        
        // Run pathfinding algorithms
        btnRun.setOnAction((event) -> 
        {
            PathfindingStrategy.Algorithms algorithm = null;
            AStarStrategy.Heuristic heuristic = null;
            
            for(PathfindingStrategy.Algorithms algo : FXCollections.observableArrayList(PathfindingStrategy.Algorithms.values()))
            {
                if(algo == cbAlgorithmBox.getValue())
                {
                    algorithm = algo;
                }
            }
            
            for(AStarStrategy.Heuristic heur : FXCollections.observableArrayList(AStarStrategy.Heuristic.values()))
            {
                if(heur == cbHeuristicBox.getValue())
                {
                    heuristic = heur;
                }
            }
            
            boolean success;
            if(algorithm != null && heuristic != null)
            {
                try 
                {
                    success = controller.doShortestPathAlgorithm(algorithm, heuristic);
                } 
                catch (InterruptedException ex) 
                {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
                this.txtStatsWallAmountValue.setText(String.valueOf(stats.getWallSize()));
                this.txtStatsTilesVisitedValue.setText(String.valueOf(stats.getTilesVisited()));
                this.txtStatsPathFoundValue.setText((stats.isPathFound()) ? "Yes" : "No");
                this.txtStatsPathCostValue.setText(String.valueOf(stats.getPathCost()));
                this.txtStatsElapsedTimeValue.setText(String.format("%.4f Milliseconds", stats.getElapsedTime() * Math.pow(10, -6)));
            }
        }
    }
}
