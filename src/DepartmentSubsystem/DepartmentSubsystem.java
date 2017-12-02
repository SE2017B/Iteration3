/*
* Software Engineering 3733, Worcester Polytechnic Institute
* Team H
* Code produced for Iteration 2
* Original author(s): Nicholas Fajardo, Meghana Bhatia
* The following code
*/

package DepartmentSubsystem;

import DepartmentSubsystem.Exceptions.PasswordException;
import DepartmentSubsystem.Exceptions.UsernameException;
import DepartmentSubsystem.Services.FoodDelivery;
import DepartmentSubsystem.Services.Sanitation;
import DepartmentSubsystem.Services.Translation;
import DepartmentSubsystem.Services.Transport;
import database.staffDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class DepartmentSubsystem {
    private Staff currentlyLoggedIn = null;
    ArrayList<Service> services;
    //Admins cannot perform a service, so they are stored outside of the services
    ArrayList<Staff> admin;
    private HashMap<String, String> loginCheck;

    private static DepartmentSubsystem singleton;
    private DepartmentSubsystem(){init();}
    private void init(){
        populateLoginCheck(staffDatabase.getStaff());

        Service translation = new Translation("Translation");
        translation.setURL("/fxml/Translation.fxml");
        services.add(translation);

        Service transport = new Transport("Transport");
        transport.setURL("/fxml/Transport.fxml");
        services.add(transport);

       // Department facilities = new Department("Facilities");
        Service sanitation = new Sanitation("Sanitation");
        sanitation.setURL("/fxml/Sanitation.fxml");
        services.add(sanitation);

        Service foodDelivery = new FoodDelivery("Food Delivery");
        foodDelivery.setURL("/fxml/FoodDelivery.fxml");
        services.add(foodDelivery);

        staffPlacement();
    }

    private void populateLoginCheck(ArrayList<Staff> members){
        this.loginCheck = new HashMap<>();
        for(Staff person: members){
            loginCheck.put(person.getUsername(), person.getPassword());
            if(person.isAdmin()){
                this.admin.add(person);
            }
        }

    }

    //Reads the DB, and puts the staff in their corresponding places
    private void staffPlacement(){
        ArrayList<Staff> allStaff = staffDatabase.getStaff();
        for(Staff person: allStaff){
            String title = person.getJobTitle();
            for(Service currentService: this.services) {
                if (title.equalsIgnoreCase("TRANSLATOR") && currentService.toString().equalsIgnoreCase("TRANSLATION SERVICE")) {
                    currentService.addEligibleStaff(person);
                }
                else if (title.equalsIgnoreCase("JANITOR") && currentService.toString().equalsIgnoreCase("SANITATION")) {
                    currentService.addEligibleStaff(person);
                }
                else if ((title.equalsIgnoreCase("CHEF") || title.equalsIgnoreCase("FOOD DELIVERY")) && currentService.toString().equalsIgnoreCase("FOOD DELIVERY SERVICE")) {
                    currentService.addEligibleStaff(person);
                }
                else if (title.equalsIgnoreCase("TRANSPORT STAFF") && currentService.toString().equalsIgnoreCase("TRANSPORT SERVICE")) {
                    currentService.addEligibleStaff(person);
                }
            }
        }
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public static DepartmentSubsystem getSubsystem(){
        if(singleton == null){
            singleton =  new DepartmentSubsystem();
        }
        return singleton;

    }

    //login function for staff members
    public boolean login(String username, String password) throws UsernameException,  PasswordException {
        if(!this.loginCheck.containsKey(username)){
            throw new UsernameException();
        }
        if(!this.loginCheck.get(username).equals(password)){
            throw new PasswordException();
        }
        return true;
    }

    public Staff getCurrentLoggedIn(){
        return this.currentlyLoggedIn;
    }

    //Staff modifiers
    public void addStaff(Service ser,String username, String password, String jobTitle, String fullName, int id, int admin){
        //staffDatabase.setStaffCounter(1);
        Staff newPerson = new Staff(username, password, jobTitle, fullName, id, admin);
        ser.addEligibleStaff(newPerson);
        staffDatabase.addStaff(newPerson);
    }

    public void modifyStaff(Staff person, String username, String password, String jobTitle, String fullName, int ID){
        person.updateCredidentials(username, password, jobTitle, fullName, ID);
        staffDatabase.modifyStaff(person);
    }

    public void deleteStaff(Service ser, String userName){
        Staff person = new Staff(userName,null, null,null, 0, 0);
        ser.removeEligibleStaff(person);

        staffDatabase.deleteStaff(person);
    }
}