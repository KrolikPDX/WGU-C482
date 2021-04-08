package Controllers;

import Classes.Inventory;
import Classes.Part;
import Classes.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller used by AddProductPage.fxml
 */
public class AddProductController implements Initializable {

//region Variable Declaration
    Stage stage;
    Parent scene;
    ObservableList<Part> associatedParts = FXCollections.observableArrayList(); //Temp list of associated parts

    @FXML private TextField searchAvailablePartsField;
    @FXML private TextField searchAssociatedPartsField;
    @FXML private TextField nameField;
    @FXML private TextField inventoryField;
    @FXML private TextField priceField;
    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private Label errorLabel;
//endregion

//region TableViews setup
    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partIDColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partStockColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;

    @FXML private TableView<Part> associatedPartTableView;
    @FXML private TableColumn<Part, Integer> associatedPartIDColumn;
    @FXML private TableColumn<Part, String> associatedPartNameColumn;
    @FXML private TableColumn<Part, Integer> associatedPartStockColumn;
    @FXML private TableColumn<Part, Double> associatedPartPriceColumn;
//endregion

    /** Initialize method for TableViews and Columns
     * @param resourceBundle ResourceBundle
     * @param url URL*/
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        //Setup partTableView and columns
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTableView.setItems(Inventory.getAllParts());

        //Setup associatedPartTableView colums
        associatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** When we press the ADD button inside AddProductPage.fxml
     * @param event The source of the button press */
    @FXML private void addButtonClicked(ActionEvent event) throws IOException{
        int id = 0; //Declare ID
        for (int i=0; i<Inventory.getAllProducts().size(); i++){ //How we find a new generated ID - finds the highest ID in Inventory and add 1 on top
            if (id <= Inventory.getAllProducts().get(i).getId())
                id = Inventory.getAllProducts().get(i).getId()+1;
        }
        try{ //We will try to parse all the entered fields into our associated variables - if a parse fails we will throw an exception
            String name = nameField.getText().strip(); //Get nameField
            double price = Double.parseDouble(priceField.getText().strip()); //Get priceField
            int stock = Integer.parseInt(inventoryField.getText().strip()); //Get inventoryField
            int max = Integer.parseInt(maxField.getText().strip()); //Get maxField
            int min = Integer.parseInt(minField.getText().strip()); //Get minField

            if (max < min) //If user entered a max value which is greater than min value
                throw new Exception("minGreater"); //Throw a manual exception called minGreater
            if (stock < min || stock > max) //If user entered an inventory value which isn't between minimum and maximum
                throw new Exception("Stock"); //Throw a manual exception called Stock

            Product product = new Product(id, name, price, stock, min, max); //If we made it this far we are all set to create the product
            for (int i=0; i < associatedParts.size(); i++){ //Add all parts in our associatedParts list to our product
                product.addAssociatedPart(associatedParts.get(i));
            }
            Inventory.addProduct(product); //Finally add the product to our inventory
            goToMain(event); //Go to main page, we are done adding product
        } catch (Exception exception) {
            if (exception.getMessage().equals("minGreater")) //Manual exception when min > max
                errorLabel.setText("Minimum should be less than Maximum!"); //Update UI to show user that min should be > max
            else if (exception.getMessage().equals("Stock")) //Manual exception when inventory isn't between min and max
                errorLabel.setText("Inventory should be in between Minimum and Maximum"); //Update UI to show user that inventory # should be between min & max
            else //Otherwise we failed to parse a variable (i.e. they input a string that can't be parsed into an integer)
                errorLabel.setText("Failed to give appropriate data types!"); //Update UI to show user that they failed to input the correct data type
        }
    }

    /** When we press the CANCEL button inside AddProductPage.fxml
     * @param event The source of the button press */
    @FXML private void cancelButtonClicked(ActionEvent event) throws IOException { goToMain(event); }

    /** When we press the ADDSELECTEDPART button inside AddProductPage.fxml
     * @param event The source of the button press */
    @FXML private void addSelectedButtonClicked(ActionEvent event) throws IOException{
        Part part = partTableView.getSelectionModel().getSelectedItem(); //Get the selected part - will return null if nothing is selected
        if (part != null && !associatedParts.contains(part)){ //If we actually selected something and our part isn't in associatedParts list already
            associatedParts.add(part); //Add the part to associatedParts
            associatedPartTableView.setItems(associatedParts); //Update associatedPartTableView with associatedParts list
        }
    }

    /** When we press the REMOVESELECTEDPART button inside AddProductPage.fxml
     * @param event The source of the button press*/
    @FXML private void removeSelectButtonClicked(ActionEvent event){
        Part part = associatedPartTableView.getSelectionModel().getSelectedItem(); //Get the selected part - will return null if nothing is selected
        if (part != null){ //If we actually selected a part to remove
            associatedParts.remove(part);  //Remove the part from the list
            associatedPartTableView.setItems(associatedParts); //Update the associatedPartTableView
        }
    }

    /** When we click enter in the searchField under parts */
    @FXML private void searchAvailablePartsEnter(){
        String text = searchAvailablePartsField.getText().trim(); //Get the inputted string and trim off white spaces
        if (text == "") //If the string is empty repopulate the tableView with all available parts
            partTableView.setItems(Inventory.getAllParts());
        else{ //Otherwise we have to try to search by ID or by matching text
            try{ //If we can convert text to integer
                int id = Integer.parseInt(text); //Try to parse the text to integer format
                ObservableList<Part> part = FXCollections.observableArrayList(); //Create temp list
                part.add(Inventory.lookupPart(id)); //Add the part with the ID to the temp list (will return null if part DNE)
                if (part.get(0) != null) //If the lists first element (should only have one element) isn't null
                    partTableView.setItems(part); //Update the partTableView with the matching part
                else{ //Otherwise we didn't find a matching part with the ID
                    errorLabel.requestFocus(); //Redirect focus away from searchField
                    searchAvailablePartsField.setText(""); //Reset searchField
                    searchAvailablePartsField.setPromptText("Part does not exist!"); //Update prompt so user can see that the part DNE
                }
                return; //We successfully parsed to integer so no need to keep searching for parts that match the text
            }catch (Exception e){}

            //Otherwise we failed to parse the text to integer and got this far (need to search for parts which contain the text in their name)
            if (Inventory.lookupPart(text).size() == 0) { //If we didn't find any parts that contain the text
                errorLabel.requestFocus(); //Redirect focus away from searchField
                searchAvailablePartsField.setText(""); //Reset searchField
                searchAvailablePartsField.setPromptText("Part does not exist!"); //Update prompt so user can see that the part DNE
            }
            else //Otherwise we found at least one part that matched our text
                partTableView.setItems(Inventory.lookupPart(text)); //Update the partTableView to contain all parts that matched
        }
    }

    /** When we click enter in the searchField under associatedPartsTableView */
    @FXML private void searchAssociatedPartsEnter(){
        String text = searchAssociatedPartsField.getText().trim();
        if (text == "")
            associatedPartTableView.setItems(associatedParts);
        else{
            try{
                int id = Integer.parseInt(text);
                ObservableList<Part> part = FXCollections.observableArrayList();
                //Instead of search Inventory, we have to search through our temp associatedParts list
                for (int i=0; i<associatedParts.size(); i++){ //Go through the list's elements
                    if (associatedParts.get(i).getId() == id){ //If an element's ID matches the ID we are search for
                        part.add(associatedParts.get(i)); //Add the element to our part temp list
                        break; //Break out of the for loop
                    }
                }
                if (part.get(0) != null)
                    associatedPartTableView.setItems(part);
                else{
                    errorLabel.requestFocus();
                    searchAssociatedPartsField.setText("");
                    searchAssociatedPartsField.setPromptText("Part does not exist!");
                }
                return;
            }catch (Exception e){}

            //Else the text isn't a parsable integer
            ObservableList<Part> parts = FXCollections.observableArrayList();
            for (int i=0; i<associatedParts.size(); i++){
                if (associatedParts.get(i).getName().toLowerCase().contains(text))
                    parts.add(associatedParts.get(i));
            }
            if (parts.size() == 0){
                errorLabel.requestFocus();
                searchAssociatedPartsField.setText("");
                searchAssociatedPartsField.setPromptText("Part does not exist!");
            }
            else
                associatedPartTableView.setItems(parts);
        }
    }

    /** Used to go back to our MainPage.fxml
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
