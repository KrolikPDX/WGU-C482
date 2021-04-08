package Controllers;

import Classes.InHouse;
import Classes.Inventory;
import Classes.OutSourced;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class used by AddPartPage.fxml
 */
public class AddPartController{

//region Objects from AddPartPage.fxml
    @FXML private Label typeLabel;
    @FXML private Label errorLabel;
    @FXML private RadioButton inHouse;
    @FXML private RadioButton outSourced;
    @FXML private TextField nameField;
    @FXML private TextField inventoryField;
    @FXML private TextField priceField;
    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private TextField typeField;
//endregion

    /** When we click on In-House radio
     * @param event The source of the button press */
    @FXML public void inHouseRadioSelected(Event event){
        typeLabel.setText("Machine ID"); //Set the typeLabel
        typeField.setPromptText("Machine ID"); //Set the promptText
    }

    /** When we click on OutSourced radio
     * @param event The source of the button press*/
    @FXML public void outSourcedRadioSelected(Event event){
        typeLabel.setText("Company Name"); //Set the typeLabel
        typeField.setPromptText("Company Name"); //Set the promptText
    }

    /** When we click on the ADD button in AddPartPage.fxml
     * @param event The source of the button press */
    @FXML public void addButtonClicked(ActionEvent event) throws IOException{
        int id = 0; //Declare ID
        for (int i=0; i<Inventory.getAllParts().size(); i++){ //Goes through all parts in Inventory and sets our ID to the highest ID+1
            if (id <= Inventory.getAllParts().get(i).getId())
                id = Inventory.getAllParts().get(i).getId()+1;
        }
        try{ //Try to parse all fields to their appropriate types
            String name = nameField.getText().strip(); //Get nameField.text
            double price = Double.parseDouble(priceField.getText().strip()); //Get priceField
            int stock = Integer.parseInt(inventoryField.getText().strip()); //Get inventoryField
            int max = Integer.parseInt(maxField.getText().strip()); //Get maxField
            int min = Integer.parseInt(minField.getText().strip()); //Get minFIeld

            if (max < min) //If user inputted a min value which is greater than max value
                throw new Exception("minGreater"); //Throw manual exception called minGreater
            if (stock < min || stock > max) //If user inputted an inventory value which doesn't fall in between min and max
                throw new Exception("stock"); //Throw manual exception called stock

            //We got this far so now we can try to create a part and add it to inventory
            if (inHouse.isSelected()){ //If the In-House radio is currently selected
                int machineID = Integer.parseInt(typeField.getText().strip()); //Get the machine ID
                InHouse newInHouse = new InHouse(id, name, price, stock, min, max, machineID); //Create an In-House part with the obtained values
                Inventory.addPart(newInHouse); //Add the created part to the Inventory
            }
            else if (outSourced.isSelected()){ //If the OutSourced radio is currently selected
                String companyName = typeField.getText().strip(); //Get the name
                OutSourced newOutSourced = new OutSourced(id, name, price, stock, min, max, companyName); //Create an OutSourced part with the obtained values
                Inventory.addPart(newOutSourced); //Add the created part to the Inventory
            }
            goToMain(event); //We are done adding the part, we can go back to main

        } catch (Exception exception){
            if (exception.getMessage().equals("minGreater")) //If minGreater exception is thrown
                errorLabel.setText("Minimum should be less than Maximum!"); //Let user know they failed to input appropriate values
            else if (exception.getMessage().equals("stock")) //If stock exception is thrown
                errorLabel.setText("Inventory should be in between Minimum and Maximum"); //Let user know they failed to input appropriate values
            else //Otherwise we failed to correctly parse into a datatype we need (i.e. string failed to parse to Integer)
                errorLabel.setText("Failed to give appropriate data types!"); //Let user know they failed to input appropriate values
        }
    }

    /** When we click on the CANCEL button in AddPartPage.fxml, simply go back to MainPage.fxml
     * @param event The source of the button press*/
    @FXML public void cancelButtonClicked(ActionEvent event) throws IOException { goToMain(event); }

    /** Used by other functions / buttons to go back to MainPage.fxml
     * @param event The source of the original button press */
    void goToMain(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/MainPage.fxml"));
        Parent parent = loader.load();
        Scene mainPage = new Scene(parent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainPage);
    }
}
