/*
* Software Engineering 3733, Worcester Polytechnic Institute
* Team H
* Code produced for Iteration3
* Original author(s): Travis Norris, Erika Snow, Nick Fajardo, Leo Grande
* The following code
*/

package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class FeedbackController implements ControllableScreen{


    private ScreenController parent;
    private int status;
    private ArrayList<String> ImageMap;
    private ArrayList<CustomPair> descriptions;

    @FXML
    private JFXSlider starSlider;
    @FXML
    private JFXTextArea HelpDescription;
    @FXML
    private Label HelpTitle;
    @FXML
    private ImageView ImageViewer;
    @FXML
    private JFXButton Next;
    @FXML
    private JFXButton Previous;

    public FeedbackController(){
        this.status = 0;
        this.ImageMap = new ArrayList<>();
        this.descriptions = new ArrayList<>();
    }

    @Override
    public void init() {
        starSlider.setValue(0.0);
        this.status = 0;
        populateLists();
        this.HelpTitle.setText("Main Menu");
        this.HelpDescription.setText("Here is the main screen for Brigham and Women's Hospital kiosk application that " +
                "includes several features. Along the top are buttons that access staff login (login), " +
                "a feedback/help/about screen (question mark), a filter for showing helpful icons on the map (filter), " +
                "a tool to find the nearest type of location from the kiosk location (Find Nearest), " +
                "and a button that brings the user to a screen to find directions from one place to another. " +
                "Along the right side is a zoom feature, and the map can also be moved by scrolling with a " +
                "mouse or clicking and dragging. Along the bottom of the map includes an emergency button (emergency) " +
                "that shows the nearest exit along with buttons to switch between floors of the hospital.");
        this.setImageFromList(false);
    }

    @Override
    public void onShow() {
        starSlider.setValue(0);
        this.status = 0;
        populateLists();
    }

    @Override
    public void setParentController(ScreenController parent) {
        this.parent = parent;
    }


    public void returnPressed(ActionEvent e){
        parent.setScreen(ScreenController.MainID,"LEFT");
    }


    public void enterPressed(ActionEvent e) {

    }

    //---------------------FEEDBACK TAB--------------------//
    public void pressNext(){
        setImageFromList(true);
        setLables();
    }

    public void pressPrevious(){
        setImageFromList(false);
        setLables();
    }

    //////////////////////////
    // HELPER METHODS BELOW //
    //////////////////////////
    private void setImageFromList(boolean next){
        //If we move forward, we check bounds
        if(next){
            if((this.status) >= this.ImageMap.size())
                this.status = this.ImageMap.size()-1;
            else
                this.status += 1;
        }
        //If we move backwards, we check bounds
        else{
            if((this.status) <= 0)
                this.status = 0;
            else
                this.status -= 1;
        }
        //This shows the image, depending on the index we want
        String imageURL = this.ImageMap.get(this.status);
        this.ImageViewer.setImage(new Image(imageURL));
    }

    private void setLables() {
        CustomPair currentLables = this.descriptions.get(this.status);
        this.HelpDescription.setText(currentLables.getDescription());
        this.HelpTitle.setText(currentLables.getTitle());
    }

    private void populateLists(){
        CustomPair main = new CustomPair("Main Menu", "Here is the main screen for Brigham and Women's Hospital kiosk application that " +
                "includes several features. Along the top are buttons that access staff login (login), " +
                "a feedback/help/about screen (question mark), a filter for showing helpful icons on the map (filter), " +
                "a tool to find the nearest type of location from the kiosk location (Find Nearest), " +
                "and a button that brings the user to a screen to find directions from one place to another. " +
                "Along the right side is a zoom feature, and the map can also be moved by scrolling with a " +
                "mouse or clicking and dragging. Along the bottom of the map includes an emergency button (emergency) " +
                "that shows the nearest exit along with buttons to switch between floors of the hospital.");
        this.descriptions.add(main);
        this.ImageMap.add("MainScreenPicture.png");
        CustomPair filter = new CustomPair("Filter on Main Menu", "When the filter button is clicked, it displays multiple buttons " +
                "of popular areas (bathrooms, elevators, etc.) that people would " +
                "want to be able to see. Therefore, when this button is clicked (for " +
                "example in this picture bathroom was clicked), circles that grow and shrink " +
                "appear for a temporary amount of time at these areas to catch the users attention.");
        this.descriptions.add(filter);
        this.ImageMap.add("FilterScreenPictureThis.png");
        CustomPair FindNearest = new CustomPair("Find Nearest", "When the find nearest button is pressed, " +
                "it displays multiple buttons of popular areas (bathrooms, elevators, etc.) that " +
                "people would want to be able to see. Therefore, when this button is clicked (for " +
                "example in this picture bathroom was clicked), a circle appears that grows and shrinks " +
                "in size at the bathroom closest to the kiosk location.");
        this.descriptions.add(FindNearest);
        this.ImageMap.add("FindNearestPictureThis.png");
        CustomPair Feedback = new CustomPair("Feedback Menu", "When the question mark button is pressed below the " +
                "login on the main screen, it takes you to the pane seen in this image. Here, feedback " +
                "will be shown first and the user can tab between feedback, help, and about. Feedback " +
                "allows the user to score the kiosk application from 0-5 and also provide additional " +
                "comments. The user can either press enter on the screen or press enter on the keyboard " +
                "to save his/her feedback.");
        this.descriptions.add(Feedback);
        this.ImageMap.add("FeedbackPicture.png");
        CustomPair SearchText = new CustomPair("Searching by Text", "If the directions button is press on the main screen, " +
                "the user will then be brought to this screen to find directions from one place to another. " +
                "The user is still able to zoom / click and drag the map to navigate and can also see " +
                "the different floors of the map. Above, the user can click return to return to the main " +
                "screen of the map, and is also present with ways to search for places. The search method defaults to a text search, " +
                "where the user can start typing to find a place or can click the arrow to see all of the available places. " +
                "Once the start node and end node are selected, the user can then click Go! to find the path.");
        this.descriptions.add(SearchText);
        this.ImageMap.add("SearchByTextPicture.png");
        CustomPair SearchByType = new CustomPair("Searching by Type", "When the user clicks search by type, another search method is seen along the top. " +
                "A user can select the type of place, then the floor number, and then the " +
                "available areas that match the type and floor number selected. " +
                "Once the start and end areas are selected, pressing Go! will find the path.");
        this.descriptions.add(SearchByType);
        this.ImageMap.add("SearchByTypePicture.png");
        CustomPair NodeToNode = new CustomPair("Directions Menu", "When Go! is clicked, a path is found based on the start and end areas." +
                " A visual line with an animated arrow on the floor the path starts on is displayed, " +
                "and leads the user to the destination if it is on the same floor or stairs/elevators " +
                "that the user has to take to get to the next floor the path goes to. Below where the " +
                "floors of the map were are now all the floors that the path visits which the user can " +
                "click through. Furthermore, towards the top left the user can click the text directions " +
                "button which displays text directions related to the path. Lastly, towards the top right " +
                "the user can press the return button which reverses the path, making the starting area " +
                "the end area and vice versa.");
        this.descriptions.add(NodeToNode);
        this.ImageMap.add("DirectionsPicture.png");
    }

    class CustomPair{
        private String title;
        private String description;
        public CustomPair(String t,  String d){
            this.title = t;
            this.description = d;
        }
        public String getTitle() {
            return title;
        }
        public String getDescription() {
            return description;
        }
    }
}
