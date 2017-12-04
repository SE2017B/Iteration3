/*
* Software Engineering 3733, Worcester Polytechnic Institute
* Team H
* Code produced for Iteration1
* Original author(s): Travis Norris, Andrey Yuzvik, Oluchukwu Okafor
* The following code
*/

package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import exceptions.InvalidNodeException;
import javafx.animation.Transition;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.MoveTo;
import javafx.util.Duration;
import map.FloorNumber;
import map.HospitalMap;
import map.Node;
import map.Path;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.animation.PathTransition;

import ui.*;

import java.util.*;

public class PathController implements ControllableScreen, Observer{
    private ScreenController parent;
    private HospitalMap map;
    private String startType = "";
    private String startFloor = "";
    private String endType =  "";
    private String endFloor = "";

    private MapViewer mapViewer;
    private FloorNumber currentFloor;// the current floor where the kiosk is.
    private PathViewer currentPath;
    private ArrayList<FloorNumber> floors; //list of floors available
    private ArrayList<PathViewer> paths;
    private ArrayList<Shape> shapes;
    private Pane arrow;
    private PathTransition pathTransition;
    private NodeSearcher searcher;
    private Pane mapPane;


    @FXML
    private ChoiceBox<Node> startNodeChoice;
    @FXML
    private ChoiceBox<Node> endNodeChoice;

    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private TitledPane textDirectionsPane;

    private ScrollPane mapScrollPane;
    @FXML
    private JFXSlider slideBarZoom;
    @FXML
    private AnchorPane buttonHolderPane;
    @FXML
    private MenuButton startTypeMenu;
    @FXML
    private MenuButton startFloorMenu;
    @FXML
    private MenuButton endTypeMenu;
    @FXML
    private MenuButton endFloorMenu;
    @FXML
    private JFXListView<String> directionsList;
    @FXML
    private JFXComboBox<Node> startTextSearch;
    @FXML
    private JFXComboBox<Node> endTextSearch;

    //Methods start here
    public void init() {
        map = HospitalMap.getMap();
        shapes = new ArrayList<Shape>();
        paths=new ArrayList<PathViewer>();
        currentFloor = FloorNumber.FLOOR_ONE;
        currentPath= new PathViewer(new Path());
        mapViewer = new MapViewer(this, parent);
        mapPane = mapViewer.getMapPane();
        mapScrollPane = mapViewer.getMapScrollPane();
        mapScrollPane.setPannable(true);
        //set up floor variables
        floors = new ArrayList<FloorNumber>();
        mapViewer.setScale(2);

        mainAnchorPane.getChildren().add(0,mapViewer.getMapViewerPane());

        int arrowSize = 20;
        arrow = new Pane();
        arrow.setPrefSize(arrowSize,arrowSize);
        Image arrowImage = new Image("images/arrow.png");
        ImageView arrowView = new ImageView(arrowImage);
        arrowView.setFitHeight(arrowSize);
        arrowView.setFitWidth(arrowSize);
        arrow.setVisible(true);
        arrow.getChildren().add(arrowView);

        pathTransition = new PathTransition();
        searcher = new NodeSearcher(map.getNodeMap());

        //add listeners
        startTextSearch.getJFXEditor().textProperty().addListener((obs, oldText, newText) -> {
            startTextSearch.getItems().clear();//remove all previous items
            List<Node> ans = map.getNodesByText(newText);
            if(ans.size()==1){
                startTextSearch.setValue(ans.get(0));//set that to the answer if their is one possible value
                //startTextSearch.hide();//hide the options
            }
            else if(ans.size()>1){
                startTextSearch.getItems().addAll(ans);
                startTextSearch.show();
            }
            else{
                //startTextSearch.hide();
            }

            System.out.println("Current value is "+startTextSearch.getValue());
        });
        //do the same thing for end node
        endTextSearch.getJFXEditor().textProperty().addListener((obs, oldText, newText) -> {
            endTextSearch.getItems().clear();//remove all previous items
            List<Node> ans = map.getNodesByText(newText);
            if(ans.size()==1){
                endTextSearch.setValue(ans.get(0));//set that to the answer if their is one possible value
                //endTextSearch.hide();//hide the options
            }
            else if(ans.size()>1){
                int nos =0; //to remove annoying error message
                endTextSearch.getItems().addAll(ans);
                endTextSearch.show();
            }
            else{
                //endTextSearch.hide();
            }

            System.out.println("Current value is "+startTextSearch.getValue());
        });

    }

    public void onShow(){
        startNodeChoice.setItems(FXCollections.observableArrayList(
                map.getKioskLocation()));
        //set the default start location to be the kiosk
        startNodeChoice.setValue(map.getKioskLocation());
        //remove any previous paths from the display
        clearPaths();

        startNodeChoice.setValue(map.getKioskLocation());
        startNodeChoice.setDisable(true);
        startFloorMenu.setText(map.getKioskLocation().getFloor().getDbMapping());
        startFloorMenu.setDisable(true);
        startTypeMenu.setText("Type");

        endNodeChoice.setDisable(true);
        endNodeChoice.setValue(null);
        endFloorMenu.setDisable(true);
        endFloorMenu.setText("Floor");
        endTypeMenu.setText("Type");

        directionsList.setItems(null); //function implementation

        mapViewer.resetView();
    }

    public void setParentController(ScreenController parent){
        this.parent = parent;
    }

    private Path getPath(){
        Node s= startTextSearch.getValue();
        Node e = endTextSearch.getValue();
        if(s==null){
            s=startNodeChoice.getValue();
        }
        if(e==null){
            endNodeChoice.getValue();
        }
        return map.findPath(s,e);
    }


    private void controlScroller(PathViewer p){

        double x = p.getCenter().get(0)/mapViewer.getScale();
        double y = p.getCenter().get(1)/mapViewer.getScale();
        System.out.println("Center X: " + x + " Center Y: " + y);
        //height
        double h = mapScrollPane.getContent().getBoundsInLocal().getHeight();
        double v = mapScrollPane.getViewportBounds().getHeight();
        //width
        double w = mapScrollPane.getContent().getBoundsInLocal().getWidth();
        double H = mapScrollPane.getViewportBounds().getWidth();
        mapScrollPane.setVvalue(((y - 0.5 * v) / (h - v)));
        mapScrollPane.setHvalue(((x - 0.5 * H) / (w - H)));
    }

    private void SetPaths(Path path){
        clearPaths();
        Node node = null;
        FloorNumber current=null; // pointer to the current node of the floor
        //ArrayList<Path> savePaths = new ArrayList<>() ;
        for(int i=0;i<path.getPath().size();i++){
            //get first floor
            node = path.getPath().get(i);
            if(node.getFloor()!=current){
                paths.add(new PathViewer(new Path()));
                //savePaths.add(new Path());//create new path for floor
                //create a new path for floors
                current = node.getFloor();//get new floor
                floors.add(current);
                if(i==0){
                    currentFloor=current;
                }
                //add to path
                paths.get(paths.size()-1).addNode(node);
                //add point for current node
            }
            else if(path.getPath().get(i).getFloor()==current){
                //add to path
                paths.get(paths.size()-1).addNode(node);
            }
        }
        //set current Path
        if(paths.size()>0){
            currentPath=paths.get(0);
            currentFloor=floors.get(0);
        }
    }

    //-----------------------ANIMATIONS START--------------------------//
    private Circle getPoint(int x, int y){
        Circle c = new AnimatedCircle();
        c.setCenterX(x/mapViewer.getScale());
        c.setCenterY(y/mapViewer.getScale());
        c.setVisible(true);
        c.setRadius(7/mapViewer.getScale());
        return c;
    }

    private void clearShapes(){
        for(Shape s : shapes){
            s.setVisible(false);
            mapPane.getChildren().remove(s);
        }
        pathTransition.stop();
        arrow.setVisible(false);
        mapPane.getChildren().remove(arrow);
        shapes = new ArrayList<>();
        System.out.println("Clearing away these shapes");
    }

    private void displayPath(PathViewer path){
        //clear path
        //display an entire path if available
        if(path.getNodes().size()>0){
            animatePath(path);
        }
    }

    private void animatePath(PathViewer path){
        System.out.println("Animating Path");
        //represent first and last nodes with animated circles
        Circle newp = getPoint(path.getNodes().get(0).getX(),path.getNodes().get(0).getY());
        newp.setFill(Color.RED);
        mapPane.getChildren().add(newp);
        currentPath.addShape(newp);
        shapes.add(newp);
        //add to last node
        Circle lastp = getPoint(path.getNodes().get(path.getNodes().size()-1).getX(),path.getNodes().get(path.getNodes().size()-1).getY());
        mapPane.getChildren().add(lastp);
        currentPath.addShape(lastp);
        shapes.add(lastp);

        if(path.getNodes().size() > 1) {
            //animation that moves the indicator
            pathTransition = new PathTransition();
            //path to follow
            javafx.scene.shape.Path p = new javafx.scene.shape.Path();
            p.setStroke(Color.NAVY);
            p.setStrokeWidth(4);
            //add shapes currentPath and shapes
            currentPath.addShape(p);
            mapPane.getChildren().addAll(p, arrow);
            shapes.add(p);
            arrow.setVisible(true);

            //starting point defined by MoveTo
            p.getElements().add(new MoveTo(path.getNodes().get(0).getX() / mapViewer.getScale(), path.getNodes().get(0).getY() / mapViewer.getScale()));

            //line movements along drawn lines
            for (int i = 1; i < path.getNodes().size(); i++) {
                p.getElements().add(new LineTo(path.getNodes().get(i).getX() / mapViewer.getScale(), path.getNodes().get(i).getY() / mapViewer.getScale()));
            }
            //define the animation actions
            pathTransition.setDuration(Duration.millis(p.getElements().size() * 800));//make speed constant
            pathTransition.setNode(arrow);
            pathTransition.setPath(p);
            pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pathTransition.setCycleCount(Transition.INDEFINITE);
            pathTransition.play();
        }
    }

    //method to switch between paths when toggling between floors
    private void switchPath(PathViewer path){
        clearShapes();
        //set zoom level here

        System.out.println("The zoom is "+path.getScale());
        displayPath(path);
        currentFloor=path.getFloor();
        controlScroller(path);//reposition map
    }

    public void clearPaths(){
        for(Shape s: shapes){
            s.setVisible(false);
            mapPane.getChildren().remove(s);
        }
        arrow.setVisible(false);
        mapPane.getChildren().remove(arrow);
        //clear all lines and paths
        floors= new ArrayList<>();
        shapes=new ArrayList<>();
        paths=new ArrayList<>();
        pathTransition.stop();
        arrow.setVisible(false);
        mapPane.getChildren().removeAll(arrow);
    }
    //-----------------------ANIMATIONS END--------------------------//

    //-------------------------MAP SCALE START--------------------------//
    public void setMapScale(double scale){
        //reposition map
        mapViewer.setScale(scale);
    }



    private double getZoom(Path p){
        /**
        ArrayList<Integer> Dim =getEdgeDims(p);
        int x = Math.abs(Dim.get(0)-Dim.get(2));
        int y = Math.abs(Dim.get(1)-Dim.get(3));

        double zx=(((x/5000.0)*mapViewer.getScale()*2))+1.0;
        double zy=(((y/3500.0)*mapViewer.getScale()*2))+1.0;
        double output = zx > zy ? zx : zy;
        return (Math.max(1.0,Math.min(3.0,output)));
         **/
        return 0;
    }

    //when + button is pressed zoom in map
    public void zinPressed(ActionEvent e){
        System.out.println("Zoom In Pressed");
        slideBarZoom.setValue(slideBarZoom.getValue()+0.2);
        setMapScale(4-slideBarZoom.getValue());
        //redraw animation to ensure that it is well positioned
        switchPath(currentPath);
    }

    //when - button pressed zoom out map
    public void zoutPressed(ActionEvent e){
        slideBarZoom.setValue(slideBarZoom.getValue()-0.2);
        setMapScale(4-slideBarZoom.getValue());
        //redraw animation to ensure that it is well positioned
        switchPath(currentPath);
    }
    //-------------------------MAP SCALE START--------------------------//

    public void enterPressed(ActionEvent e) throws InvalidNodeException {
        if((startNodeChoice.getValue() instanceof Node || startTextSearch.getValue() instanceof Node) &&
                (endNodeChoice.getValue() instanceof Node || endTextSearch.getValue() instanceof Node)) {
            Path thePath = getPath();

            clearPaths();
            SetPaths(thePath);
            mapViewer.setButtonsByFloor(floors);
            directionsList.setItems(FXCollections.observableList(thePath.findDirections()));
            textDirectionsPane.setVisible(true);
            textDirectionsPane.setExpanded(false);
            switchPath(paths.get(0));
            System.out.println("Enter Pressed");

            //mapScrollPane.setHvalue(startNodeChoice.getValue().getX()/5000.0);
            //mapScrollPane.setVvalue(startNodeChoice.getValue().getY()/3500.0);
        }
    }

    public void cancelPressed(ActionEvent e) {
        System.out.println("Cancel Pressed");
        clearPaths();
        parent.setScreen(ScreenController.MainID,"RIGHT");
    }

    public void update(Observable o, Object arg){
        if(arg instanceof PathID){
            PathID ID = (PathID) arg;
            if(ID.getID() != -1) {
                currentPath = paths.get(ID.getID());
                currentFloor = ID.getFloor();
                switchPath(currentPath);
            }
        }
    }

    //-----------------------NODE SELECT END--------------------------//
    public void startTypeSelected(ActionEvent e){
        startType = ((MenuItem)e.getSource()).getText();
        startTypeMenu.setText(startType);
        startFloorMenu.setDisable(false);
        if(!startFloor.equals("")){
            startChosen();
        }
    }

    public void startFloorSelected(ActionEvent e){
        startFloor = ((MenuItem)e.getSource()).getText();
        startFloorMenu.setText(startFloor);
        startChosen();
    }

    private void startChosen(){
        String filter = "";
        if(startType.equals("Restroom")){
            filter = "REST";
        }
        else if (startType.equals("Retail")){
            filter = "RETL";
        }
        else if (startType.equals("Exits")){
            filter = "EXIT";
        }
        else if (startType.equals("Stairs")){
            filter = "STAI";
        }
        else if (startType.equals("Elevators")){
            filter = "ELEV";
        }
        else if (startType.equals("Department")){
            filter = "DEPT";
        }
        else if (startType.equals("Services")){
            filter = "SERV";
        }
        else {
            filter = "INFO";
        }
        final String f = filter.toString();
        if(startFloor.equals("ALL")){
            startNodeChoice.setItems(FXCollections.observableList(map.getNodesBy( n -> n.getType().equals(f))));
        }
        else{
            FloorNumber floor = FloorNumber.fromDbMapping(startFloor);
            startNodeChoice.setItems(FXCollections.observableList(map.getNodesBy( n -> n.getType().equals(f) && n.getFloor().equals(floor))));
        }
        startNodeChoice.setDisable(false);
    }

    public void endTypeSelected(ActionEvent e){
        endType = ((MenuItem)e.getSource()).getText();
        endTypeMenu.setText(endType);
        endFloorMenu.setDisable(false);
        if(!endFloor.equals("")){
            endChosen();
        }
    }

    public void endFloorSelected(ActionEvent e){
        endFloor = ((MenuItem)e.getSource()).getText();
        endFloorMenu.setText(endFloor);
        endChosen();
    }

    private void endChosen(){
        String filter = "";
        if(endType.equals("Restroom")){
            filter = "REST";
        }
        else if (endType.equals("Retail")){
            filter = "RETL";
        }
        else if (endType.equals("Exits")){
            filter = "EXIT";
        }
        else if (endType.equals("Stairs")){
            filter = "STAI";
        }
        else if (endType.equals("Elevators")){
            filter = "ELEV";
        }
        else if (endType.equals("Department")){
            filter = "DEPT";
        }
        else if (endType.equals("Services")){
            filter = "SERV";
        }
        else {
            filter = "INFO";
        }
        final String f = filter.toString();
        if(endFloor.equals("ALL")){
            endNodeChoice.setItems(FXCollections.observableList(map.getNodesBy( n -> n.getType().equals(f))));
        }
        else{
            FloorNumber floor = FloorNumber.fromDbMapping(endFloor);
            endNodeChoice.setItems(FXCollections.observableList(map.getNodesBy( n -> n.getType().equals(f) && n.getFloor().equals(floor))));
        }
        endNodeChoice.setDisable(false);
    }
    //-----------------------NODE SELECT END--------------------------//
}
