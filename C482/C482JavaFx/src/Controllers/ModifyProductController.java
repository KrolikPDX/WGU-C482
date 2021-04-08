package Controllers;

import Classes.Inventory;
import Classes.Part;
import Classes.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class used by ModifyProductPage.fxml
 */
public class ModifyProductController implements Initializable {


//region Variable Declaration
    int id; //Temp ID used to store oldPartsID (shouldn't change)
    Product selectedProduct; //Used to  store selected product we are changing
    ObservableList<Part> associatedParts = FXCollections.observableArrayList(); //Temp list of associatedParts we want / current have in our temp product

    //Objects from ModifyProductPage.fxml
    @FXML private Label errorLabel;
    @FXML private TextField searchAvailablePartsField;
    @FXML private TextField searchAssociatedPartsField;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField inventoryField;
    @FXML private TextField priceField;
    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private AnchorPane anchorPane;

    //TableViews (Objects from ModifyProductPage.fxml for the two tables)
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

//endregion Variable Declaration

    /** Initialize method for TableViews and Columns
     * @param resourceBundle ResourceBundle
     * @param url URL */
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        //Setup the partTableView & Columns
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTableView.setItems(Inventory.getAllParts());

        //Setup associatedPartTableView & Columns
        associatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** Initialize method used to set Fields and Temp variables
     * @param selectedProduct Product which we will be modifying */
     public void initData(Product selectedProduct) {
        associatedParts.addAll(selectedProduct.getAllAssociatedParts()); //Setup our temp associatedParts list with all associated parts in selectedProduct
        associatedPartTableView.setItems(associatedParts); //Set the tableview with those parts

        //Set TextFields:
        idField.setText(Integer.toString(selectedProduct.getId()));
        nameField.setText(selectedProduct.getName());
        inventoryField.setText(Integer.toString(selectedProduct.getStock()));
        priceField.setText(Double.toString(selectedProduct.getPrice()));
        minField.setText(Integer.toString(selectedProduct.getMin()));
        maxField.setText(Integer.toString(selectedProduct.getMax()));

        //Set local variables
        id = selectedProduct.getId();
        this.selectedProduct = selectedProduct; //Need to reference this selectedProduct later
        anchorPane.requestFocus();
    }

    /** When we press the MODIFY button in ModifyProductPage.fxml
     * @param event The source of the button press */
    @FXML public void modifyButtonClicked(ActionEvent event) throws IOException {
        try{
            String name = nameField.getText().strip(); //Get name
            double price = Double.parseDouble(priceField.getText().strip()); //Get price
            int stock = Integer.parseInt(inventoryField.getText().strip()); //Get stock
            int max = Integer.parseInt(maxField.getText().strip()); //Get max
            int min = Integer.parseInt(minField.getText().strip()); //Get min
            if (max < min) //If user entered a max value which is greater than min value
                throw new Exception("minGreater"); //Throw a manual exception called minGreater
            if (stock < min || stock > max) //If user entered an inventory value which isn't between minimum and maximum
                throw new Exception("Stock"); //Throw a manual exception called Stock

            //If we made it this far we can set the product
            Product product = new Product(id, name, price, stock, min, max); //Create temp product to replace old product
            for (int i=0; i<associatedParts.size(); i++){ //Add all associated parts in our temp list to temp product
                product.addAssociatedPart(associatedParts.get(i)); }
            Inventory.updateProduct(Inventory.getAllProducts().indexOf(selectedProduct), product); //Replace old product with new product
            returnToMain(event); //We are done and can return to main
        } catch (Exception exception){
            if (exception.getMessage().equals("minGreater")) //Manual exception when min > max
                errorLabel.setText("Minimum should be less than Maximum!"); //Update UI to show user that min should be > max
            else if (exception.getMessage().equals("Stock")) //Manual exception when inventory isn't between min and max
                errorLabel.setText("Inventory should be in between Minimum and Maximum"); //Update UI to show user that inventory # should be between min & max
            else //Otherwise we failed to parse a variable (i.e. they input a string that can't be parsed into an integer)
                errorLabel.setText("Failed to give appropriate data types!"); //Update UI to show user that they failed to input the correct data type
        }


    }

    /** When we press the ADDSELECTEDPART button in ModifyProductPage.fxml*/
    @FXML private void addSelectedButtonClicked(){
        Part part = partTableView.getSelectionModel().getSelectedItem(); //Get part selected (returns null if nothing is selected)
        if (part != null && !associatedParts.contains(part)){ //If we actually selected a part and its not in the list already...
            associatedParts.add(part); //Add the part to the list
            associatedPartTableView.setItems(associatedParts); //Update the tableview
        }
    }

    /** When we press the REMOVESELECTEDPART button in ModifyProductPage.fxml
     * @param event The source of the button press */
    @FXML private void removeSelectedButtonClicked(ActionEvent event){
        Part part = associatedPartTableView.getSelectionModel().getSelectedItem(); //Get selected part (returns null if nothing is selected)
        if (part != null && associatedParts.contains(part)){ //If we actually selected something && our list contains that part (always should be true)
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL); //Create a YES and CANCEL alert
            alert.initModality(Modality.APPLICATION_MODAL); //Set Modality of alert
            alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow()); //Set Owner of alert
            alert.setHeaderText("Are you sure you want to remove " + part.getName()); //Setup Header of alert
            Optional<ButtonType> result = alert.showAndWait(); //Show the alert and wait for user input
            if (result.get() == ButtonType.YES){ //If user selected YES, remove the part
                associatedParts.remove(part); //Remove the selected part from the list
                associatedPartTableView.setItems(associatedParts); //Update the table view
            }
        }
    }

    /** If we press the CANCEL button in ModifyProductPage.fxml
     * @param event The source of the button press */
    @FXML private void cancelButtonClicked(ActionEvent event) throws IOException { returnToMain(event); }

    /** If we press enter in the SearchPartsField */
    @FXML private void searchAvailablePartsEnter(){
        String text = searchAvailablePartsField.getText().trim(); //Get the entered string and trim whitespaces
        if (text.matches("")){  //If we got empty input / string
            partTableView.setItems(Inventory.getAllParts()); //Repopulate partTableView will all parts in Inventory
            searchAvailablePartsField.setPromptText("Search by ID or Name"); //Setup the prompt
            anchorPane.requestFocus(); //Focus away from the field so user can see prompt
        }
        else{ //Otherwise we got a string we can use to search for a part
            try{ //Try to parse the string into an integer
                int id = Integer.parseInt(text); //If we fail here it will break out of the try statement
                ObservableList<Part> part = FXCollections.observableArrayList(); //Temp list
                part.add(Inventory.lookupPart(id)); //Look for the part with the ID and add it to temp list (returns null if part DNE)
                if (part.get(0) != null) //If we actually found the part
                    partTableView.setItems(part); //Show the part in the tableView
                else{ //otherwise we didnt find the part
                    anchorPane.requestFocus(); //Shift focus away from searchField
                    searchAvailablePartsField.setText(""); //Reset searchField
                    searchAvailablePartsField.setPromptText("Part does not exist!"); //Show user prompt in search field that the part DNE
                }
                return; //If we parsed the string to int successfully return out of the function
            }catch (Exception e){}

            //If we get this far we couldn't parse the text to integer && Lookup parts that contain the text
            if (Inventory.lookupPart(text).size() == 0){ //If there are no parts that match the text...
                anchorPane.requestFocus(); //Shift focus away from searchField
                searchAvailablePartsField.setText(""); //Reset searchField
                searchAvailablePartsField.setPromptText("Part does not exist!"); //Show user prompt in search field that the part DNE
            }
            else //Otherwise we found at least one part that contains our text
                partTableView.setItems(Inventory.lookupPart(text)); //Populate the partTableView with parts that match
        }
    }

    /** If we press enter in the SearchAssociatedPartsField */
    @FXML private void searchAssociatedPartsEnter(){
        String text = searchAssociatedPartsField.getText().trim();
        if (text.matches("")){ //We have an empty string
            associatedPartTableView.setItems(associatedParts);
            searchAssociatedPartsField.setPromptText("Search by ID or Name");
            searchAssociatedPartsField.setText("");
            anchorPane.requestFocus();
        }
        else {  //We don't have an empty string
            try{
                int id = Integer.parseInt(text);
                ObservableList<Part> part = FXCollections.observableArrayList();
                for (int i=0; i<associatedParts.size(); i++){
                    if (associatedParts.get(i).getId() == id){
                        part.add(associatedParts.get(i));
                        break;
                    }
                }
                if (part.get(0) != null)
                    associatedPartTableView.setItems(part);
                else{
                    anchorPane.requestFocus();
                    searchAssociatedPartsField.setText("");
                    searchAssociatedPartsField.setPromptText("Part does not exist!");
                }
                return;
            }catch (Exception e){}
            if (associatedParts.size() == 0){
                anchorPane.requestFocus();
                searchAssociatedPartsField.setText("");
                searchAssociatedPartsField.setPromptText("No parts to search for...");
            }
            else {
                ObservableList<Part> parts = FXCollections.observableArrayList();
                for (int i=0; i<associatedParts.size(); i++){
                    if (associatedParts.get(i).getName().toLowerCase().contains(text))
                        parts.add(associatedParts.get(i));
                }
                if (parts.size() == 0){
                    anchorPane.requestFocus();
                    searchAssociatedPartsField.setPromptText("Part does not exist!");
                    searchAssociatedPartsField.setText("");
                }
                else
                    associatedPartTableView.setItems(parts);
            }
        }
    }

    /** Used to return to MainPage.fxml
     * @param event The source of the original button press*/
    void returnToMain(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/MainPage.fxml"));
        Parent parent = loader.load();
        Scene mainPage = new Scene(parent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainPage);
    }



}
