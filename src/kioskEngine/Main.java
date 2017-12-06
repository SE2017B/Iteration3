/*
* Software Engineering 3733, Worcester Polytechnic Institute
* Team H
* Code produced for Iteration1
* Original author(s): Erica Snow, Vojta Mosby, Tyrone Patterson
* The following code
*/

package kioskEngine;

import controllers.ScreenController;
import database.edgeDatabase;
import database.nodeDatabase;
import database.staffDatabase;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Scene scene = new Scene(root);
        root.minWidth(800);
        root.minHeight(400);


        ScreenController myScreenController = new ScreenController();
        root.getChildren().addAll(myScreenController);
        myScreenController.prefHeightProperty().bind(scene.heightProperty());
        myScreenController.prefWidthProperty().bind(scene.widthProperty());


        myScreenController.loadScreen(ScreenController.AddNodeID, ScreenController.AddNodeFile);
        myScreenController.loadScreen(ScreenController.LogoutID, ScreenController.LogoutFile);
        myScreenController.loadScreen(ScreenController.MainID, ScreenController.MainFile);
        myScreenController.loadScreen(ScreenController.PathID, ScreenController.PathFile);
        myScreenController.loadScreen(ScreenController.RequestID, ScreenController.RequestFile);
        myScreenController.loadScreen(ScreenController.LoginID, ScreenController.LoginFile);
        myScreenController.loadScreen(ScreenController.FeedbackID, ScreenController.FeedbackFile);
//
//        //mini fxml files
//        myScreenController.loadScreen(ScreenController.TranslationID, ScreenController.TranslationFile);
//        myScreenController.loadScreen(ScreenController.TransportID, ScreenController.TransportFile);
//        myScreenController.loadScreen(ScreenController.FoodDeliveryID, ScreenController.FoodDeliveryFile);
//        myScreenController.loadScreen(ScreenController.SanitationID, ScreenController.SanitationFile);

        myScreenController.setScreen(ScreenController.MainID);
        myScreenController.saveState();
        String  style= getClass().getResource("/fxml/SceneStyle.css").toExternalForm();
        scene.getStylesheets().add(style);
        primaryStage.setScene(scene);
        primaryStage.setWidth(1280);
        primaryStage.setHeight(800);

        EventHandler reset = new EventHandler<InputEvent>() {
            @Override
            public void handle(InputEvent event){
                myScreenController.resetTimeout();
            }};
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, reset);
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, reset);
        scene.addEventFilter(KeyEvent.KEY_TYPED, reset);
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        edgeDatabase.deleteEdgeTable();
        nodeDatabase.deleteNodeTable();
        staffDatabase.deleteStaffTable();

        nodeDatabase.createNodeTable();
        edgeDatabase.createEdgeTable();
        staffDatabase.createStaffTable();

        nodeDatabase.readNodeCSV("src/csv/MapAnodes.csv");
        nodeDatabase.readNodeCSV("src/csv/MapBnodes.csv");
        nodeDatabase.readNodeCSV("src/csv/MapCnodes.csv");
        nodeDatabase.readNodeCSV("src/csv/MapDnodes.csv");
        nodeDatabase.readNodeCSV("src/csv/MapEnodes.csv");
        nodeDatabase.readNodeCSV("src/csv/MapFnodes.csv");
        nodeDatabase.readNodeCSV("src/csv/MapGnodes.csv");
        nodeDatabase.readNodeCSV("src/csv/MapHnodes.csv");
        nodeDatabase.readNodeCSV("src/csv/MapInodes.csv");
        nodeDatabase.readNodeCSV("src/csv/MapWnodes.csv");

        edgeDatabase.readEdgesCSV("src/csv/MapAedges.csv");
        edgeDatabase.readEdgesCSV("src/csv/MapBedges.csv");
        edgeDatabase.readEdgesCSV("src/csv/MapCedges.csv");
        edgeDatabase.readEdgesCSV("src/csv/MapDedges.csv");
        edgeDatabase.readEdgesCSV("src/csv/MapEedges.csv");
        edgeDatabase.readEdgesCSV("src/csv/MapFedges.csv");
        edgeDatabase.readEdgesCSV("src/csv/MapGedges.csv");
        edgeDatabase.readEdgesCSV("src/csv/MapHedges.csv");
        edgeDatabase.readEdgesCSV("src/csv/MapIedges.csv");
        edgeDatabase.readEdgesCSV("src/csv/MapWedges.csv");

        nodeDatabase.insertNodesFromCSV();
        edgeDatabase.insertEdgesFromCSV();

        nodeDatabase.cntNodes();

        staffDatabase.readStaffCSV("src/csv/staffMembers.csv");
        staffDatabase.insertStaffFromCSV();

        staffDatabase.queryAllStaff();

        launch(args);

        nodeDatabase.outputNodesCSV();
        edgeDatabase.outputEdgesCSV();
        staffDatabase.outputStaffCSV();
        staffDatabase.outputStaffEncCSV();
    }
}
