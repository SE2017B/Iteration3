package controllers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import map.FloorNumber;

import java.util.HashMap;


public class proxyImagePane extends StackPane {

    //pointers to images and their fxml files
    private String floorL1 = "images/00_thelowerlever1.png";
    private String floorL2 = "images/00_thelowerlever2.png";
    private String floorG = "images/00_thegroundfloor.png";
    private String floor1 = "images/01_thefirstfloor.png";
    private String floor2 = "images/02_thesecondfloor.png";
    private String floor3 = "images/03_thethirdfloor.png";

    private HashMap<FloorNumber,ImageView> floors = new HashMap<FloorNumber,ImageView>();

    public proxyImagePane(){
        super();
        //add all floors
        addImage(FloorNumber.fromDbMapping("L2"));
        addImage(FloorNumber.fromDbMapping("L1"));
        addImage(FloorNumber.fromDbMapping("G"));
        addImage(FloorNumber.fromDbMapping("1"));
        addImage(FloorNumber.fromDbMapping("2"));
        addImage(FloorNumber.fromDbMapping("3"));
    }

    private String getImage(FloorNumber floor){
        //return corresponding image type
        if(floor.getDbMapping().equals("L2")){
            return "images/00_thelowerlevel2.png";
        }
        if(floor.getDbMapping().equals("L1")){
            return "images/00_thelowerlevel1.png";
        }
        if(floor.getDbMapping().equals("G")){
            return "images/00_thegroundfloor.png";
        }
        if(floor.getDbMapping().equals("1")){
            return "images/01_thefirstfloor.png";
        }
        if(floor.getDbMapping().equals("2")){
            return "images/02_thesecondfloor.png";
        }
        if(floor.getDbMapping().equals("3")){
            return "images/03_thethirdfloor.png";
        }
        return null;

    }
    public ImageView getImagePane(FloorNumber floor){
        return floors.get(floor);
    }
    public boolean addImage(FloorNumber floor){
        //Todo: create new image view
        if(floor!=null){
            String name = this.getImage(floor);//get image name for floor
            System.out.println("Path is "+name);
            Image img = new Image(name); //create new image
            System.out.println("We made it baby");
            ImageView imgView = new ImageView(img); //create new image view pane
            imgView.setFitWidth(2000);
            imgView.setFitHeight(3000);
            imgView.setVisible(true);
            floors.put(floor,imgView);//add new image view to hash map floors
            //get the image pane directly instead

            return true;
        }
        return false;
    }
    public boolean setImage(FloorNumber floor){
        //Todo: Isn't set image and add image the same thing
        /**
        if(screens.containsKey(name)){
            if(!getChildren().isEmpty()){
                getChildren().remove(0);
            }
            getChildren().add(screens.get(name));
            controllers.get(name).onShow();
            return true;
        }
        System.out.println("Set Screen Failed");
        return false;
         **/
        return true;
    }
    public boolean removeImage(FloorNumber floor){
        floors.remove(floor);
        return true;
    }

}
