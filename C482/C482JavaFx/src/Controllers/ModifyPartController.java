package Controllers;

import Classes.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class used by ModifyPartPage.fxml
 */
public class ModifyPartController {

//region Variable Declaration
    boolean isHouseSelected = true; //Used to determine which radio is selected
    int partIndex; //Used to later replace old part with new part at the same index
    //Objects from ModifyPartPage.fxml
    @FXML private Label errorLabel;
    @FXML private AnchorPane anchorPane;
    @FXML private Label typeLabel;
    @FXML private RadioButton inHouseRadio;
    @FXML private RadioButton outSourcedRadio;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField inventoryField;
    @FXML private TextField priceField;
    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private TextField typeField;
//endregion


    /** When we initially run the ModifyPartController
     * @param part Part we are modifying */
    public void initData(Part part){ //When we first load up the controller, we need to know which part we are modifying - Passed from MainController
        partIndex = Inventory.getAllParts().indexOf(part); //Set partIndex to what the old parts index is in Inventory
        if (part instanceof InHouse) { //If we are looking at an In-House part
            inHouseRadio.setSelected(true); //Set inHouseRadio selected to true
            outSourcedRadio.setSelected(false); //Set outSourcedRadio select to false
            typeLabel.setText("Machine ID"); //Correct the label text
            typeField.setText(((InHouse) part).getMachineID()); //Setup the MachineID in the typeField
            typeField.setPromptText("Machine ID"); //Change promptText
            isHouseSelected = true; //Bool changed to appropriate value
        }
        else if (part instanceof OutSourced){ //If the part we are modifying is an OutSourced part
            inHouseRadio.setSelected(false); //Set inHouseRadio selected to false
            outSourcedRadio.setSelected(true); //Set outSourcedRadio selected to ture
            typeField.setPromptText("Company Name"); //Correct prompt text
            typeField.setText(((OutSourced) part).getCompanyName()); //Set the typeField to parts current companyName
            typeLabel.setText("Company Name"); //Correct typeLabel
            isHouseSelected = false; //Bool changed to appropriate value
        }
        //Now that we know what part we have all the other fields will be set regardless of the type of part
        idField.setText(Integer.toString(part.getId())); //Set our ID field
        nameField.setText(part.getName()); //Set our Name field
        inventoryField.setText(Integer.toString(part.getStock())); //Set our inventory field
        priceField.setText(Double.toString(part.getPrice())); //Set our price field
        maxField.setText(Integer.toString(part.getMax())); //Set our max field
        minField.setText(Integer.toString(part.getMin())); //Set our min field

        anchorPane.requestFocus(); //To get focus off of nameField
    }

     /** If we select In-House radio
      * @param event The source of the button press */
    @FXML public void inHouseRadioSelect(Event event){
        if (!isHouseSelected){ //If we aren't currently selected
            isHouseSelected = true; //Now we are currently selected
            typeLabel.setText("Machine ID"); //Correct typeLabel
            typeField.setPromptText("Machine ID"); //Correct prompt
            typeField.setText(""); //Reset typeField.text
        }
    }

    /** If we select OutSourced radio
     * @param event The source of the button press */
    @FXML public void outSourcedRadioSelect(Event event){
        if (isHouseSelected){ //If we aren't currently selected
            isHouseSelected = false; //Now we are currently selected
            typeLabel.setText("Company Name"); //Correct typeLabel
            typeField.setPromptText("Company Name"); //Correct prompt
            typeField.setText(""); //Reset typeField.text
        }
    }

    /** If we press on the MODIFY button in ModifyPartPage.fxml
     * @param event The source of the button press*/
    public void modifyButtonPressed(Event event) throws IOException {
        try{
            int id = Integer.parseInt(idField.getText()); //Get ID (shouldn't change)
            String name = nameField.getText().strip(); //Get name
            double price = Double.parseDouble(priceField.getText().strip()); //Get price
            int stock = Integer.parseInt(inventoryField.getText().strip()); //Get inventory
            int max = Integer.parseInt(maxField.getText().strip()); //Get max
            int min = Integer.parseInt(minField.getText().strip()); //Get min
            if (max < min) //If user entered a max value which is greater than min value
                throw new Exception("minGreater"); //Throw a manual exception called minGreater
            if (stock < min || stock > max) //If user entered an inventory value which isn't between minimum and maximum
                throw new Exception("Stock"); //Throw a manual exception called Stock

            //If we get this far that means we parsed everything correctly and can modify / create part
            if (isHouseSelected){ //If the In-House radio is selected
                int machineID = Integer.parseInt(typeField.getText()); //Get the machineID
                InHouse newPart = new InHouse(id, name, price, stock, min, max, machineID); //Create temp InHouse part with data
                Inventory.updatePart(partIndex, newPart); //Replace old part with new part in Inventory
            }
            else { //Otherwise we are creating OutSourced part
                String companyName = typeField.getText(); //Get the companyName
                OutSourced newPart = new OutSourced(id, name, price, stock, min, max, companyName); //Create temp outSourced part
                Inventory.updatePart(partIndex, newPart); //Replace old part with the new part in Inventory
            }
            returnToMain(event); //We are done modifying and can return to main
        } catch (Exception exception){
            if (exception.getMessage().equals("minGreater")) //Manual exception when min > max
                errorLabel.setText("Minimum should be less than Maximum!"); //Update UI to show user that min should be > max
            else if (exception.getMessage().equals("Stock")) //Manual exception when inventory isn't between min and max
                errorLabel.setText("Inventory should be in between Minimum and Maximum"); //Update UI to show user that inventory # should be between min & max
            else //Otherwise we failed to parse a variable (i.e. they input a string that can't be parsed into an integer)
                errorLabel.setText("Failed to give appropriate data types!"); //Update UI to show user that they failed to input the correct data type
        }

    }

    /** When we press cancel button return to MainPage.fxml
     * @param event The source of the button press */
    @FXML public void cancelButtonPress(Event event) throws IOException { returnToMain(event); }

    /**  Used to return to MainPage.fxml
     * @param event The source of the original button press */
    void returnToMain(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/MainPage.fxml"));
        Parent parent = loader.load();
        Scene mainPage = new Scene(parent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainPage);
    }



}
