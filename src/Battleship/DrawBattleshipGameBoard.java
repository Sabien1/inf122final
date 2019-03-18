package Battleship;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DrawBattleshipGameBoard {
    static final int BOARD_WIDTH = 10;
    static final int BOARD_HEIGHT = 10;
    static final int CARRIER = 5;
    static final int BATTLESHIP = 4;
    static final int CRUISER = 3;
    static final int SUBMARINE = 2;
    static final int DESTROYER = 1;
    private GridPane boardGame;
    int rowClicked = 0, colClicked = 0;
    static Image defaultBoardCell = new Image("/Battleship/ocean-cell.png");
    static Image hitBoardCell = new Image("/Battleship/ocean-hit-cell.png");
    static Image missBoardCell = new Image("/Battleship/ocean-miss-cell.png");
    static Image cellHover = new Image("/Battleship/ocean-cell-hover.png");
    boolean clickMade = false;
    BattleshipGameLogic Logic = new BattleshipGameLogic();


    public DrawBattleshipGameBoard() {
        try {
            Stage primaryStage = new Stage();
            boardGame = new GridPane();
            initializeBoard(BOARD_WIDTH, BOARD_HEIGHT, "/Battleship/ocean-cell.png");
            primaryStage.setTitle("Battleship");
            primaryStage.initModality(Modality.WINDOW_MODAL);
            boardGame.getStylesheets().add("styles.css");
            boardGame.setGridLinesVisible(true);
            primaryStage.setScene(new Scene(boardGame, 600, 600));
            primaryStage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void initializeBoard(int rows, int cols, String imageDirectory) {
        for (int i = 0 ; i < cols ; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / cols);
            boardGame.getColumnConstraints().add(colConstraints);
        }
        for (int j = 0; j < rows; j++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / rows);
            boardGame.getRowConstraints().add(rowConstraints);
        }
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                addCell(i, j, defaultBoardCell);
            }
        }
    }


    public void addCell(int rowIndex, int colIndex, Image image){
        Pane cell = new Pane(new ImageView(image));
        cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                rowClicked = rowIndex;
                colClicked = colIndex;
                clickMade = true;
                //System.out.println(rowClicked + " _ " + colClicked);
                //BattleshipGameLogic.moveAttempted(colClicked, rowClicked);
                Logic.makeMove(rowClicked, colClicked);
                updateGameBoard(Logic);
            }

        });
        boardGame.add(cell, colIndex, rowIndex);
    }

    public void modifyCell(int colIndex, int rowIndex, Image image){
        ObservableList<Node> childrens = boardGame.getChildren();
        for (Node node : childrens) {
            if(GridPane.getRowIndex(node) == rowIndex && GridPane.getColumnIndex(node) == colIndex) {
                Pane cell = new Pane(new ImageView(image));
                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent arg0) {
                        rowClicked = rowIndex;
                        colClicked = colIndex;
                        clickMade = true;
                        //BattleshipGameLogic.moveAttempted(colClicked, rowClicked);
                        Logic.makeMove(rowClicked, colClicked);
                        updateGameBoard(Logic);
                    }

                });
                boardGame.add(cell,colIndex,rowIndex);
                boardGame.setGridLinesVisible(false);
                boardGame.setGridLinesVisible(true);
                break;
            }
        }
    }



    void updateGameBoard(BattleshipGameLogic logic) { //updates so GUI shows current player's ocean of hits
        for (int i = 0; i < logic.currentPlayer().playerOceanHits.length; i++ ) {
            for (int j = 0; j < logic.currentPlayer().playerOceanHits[i].length; j++ ) {
                if (logic.currentPlayer().playerOceanHits[i][j] == 1) {
                    modifyCell(j, i, hitBoardCell);
                } else if (logic.currentPlayer().playerOceanHits[i][j] == 2) {
                    modifyCell(j, i, missBoardCell);
                } else {
                    modifyCell(j, i, DrawBattleshipGameBoard.defaultBoardCell);
                }
            }
        }
    }
}



