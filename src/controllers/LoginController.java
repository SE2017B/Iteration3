package controllers;

import DepartmentSubsystem.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginController implements ControllableScreen{
    public LoginController(){}
    ScreenController parent;
    private Staff member;
    private DepartmentSubsystem depSub;

    public String staffLoggedIn;

    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML

    @Override
    public void init() {
        depSub = DepartmentSubsystem.getSubsystem();
    }

    @Override
    public void onShow() {
        usernameField.setText("");
        passwordField.setText("");
    }

    @Override
    public void setParentController(ScreenController parent) {
        this.parent = parent;
    }

    public void returnPressed(ActionEvent e){
        System.out.println("Return Pressed");
        parent.setScreen(ScreenController.MainID,"LEFT");
    }

    public void enterPressed(ActionEvent e){
        System.out.println("Enter Pressed");
        //parent.setScreen(ScreenController.RequestID,"UP");
        System.out.println(usernameField.getText().toString());
        System.out.println(passwordField.getText().toString());
        //boolean result = (depSub.login(usernameField.getText().toString(),passwordField.getText().toString()));
        //System.out.println(result);
        String login = usernameField.getText();
        String passWord = passwordField.getText();
        if((depSub.login(login,passWord)))
        {
            parent.setScreen(ScreenController.RequestID,"UP");
        }
        else{
            System.out.println("Wrong Pass/Login");
        }
    }
}
