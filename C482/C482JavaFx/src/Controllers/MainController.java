package Controllers;

import Classes.*;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Controller class used by MainPage.FXML
 */
public class MainController implements Initializable {

    @FXML private AnchorPane anchorPane; //Used to direct focus away from SearchFields
    @FXML private TextField searchPartField; //SearchField to search parts in Inventory
    @FXML private TextField searchProductField; //SearchField to search products in Inventory

//region PartsTableView Declaration
    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partIDColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partStockColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;

    @FXML private TableView<Product> productTableView;
    @FXML private TableColumn<Product, Integer> productIDColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productStockColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;
//endregion

    /** Initialize method for TableViews and Columns
     * @param url URL
     * @param resourceBundle ResourceBundle*/
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {

        //Setup for the partTableView and columns
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTableView.setItems(Inventory.getAllParts());

        //Setup for the productTableView and columns
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productTableView.setItems(Inventory.getAllProducts());
    }

    /** When we click on the ADD button under the parts table, we want to open up the AddPartPage.fxml
     * @param event The source of the button press  */
    @FXML private void addPartsClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/AddPartPage.fxml"));
        Parent parent = loader.load();
        Scene addPartPageScene = new Scene(parent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addPartPageScene);
    }

    /** When we click on the MODIFY button under the parts table, we want to open up ModifyPartPage.fxml
     * @param event The source of the button press */
    @FXML private void modifyPartsClicked(ActionEvent event) throws IOException{
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem(); //Get the user selected part from the table
        if (selectedPart  != null){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/ModifyPartPage.fxml"));
            Parent parent = loader.load();
            Scene modifyPartPageScene = new Scene(parent);

            ModifyPartController controller = loader.getController(); //Setup controller that controls the ModifyPartPage
            controller.initData(selectedPart);  //Pass it the selectedPart so we know what part to modify

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(modifyPartPageScene);
        }
    }

    /** When we click on the DELETE button under the parts table, we want to confirm the deletion of selected part
     * @param event The source of the button press */
    @FXML private void deletePartsClicked(ActionEvent event){
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem(); //Get user selected part
        if (selectedPart != null) //If we actually selected something
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL); //Setup an alert with the YES and CANCEL buttons
            alert.initModality(Modality.APPLICATION_MODAL); //Set Modality of alert
            alert.initOwner(((Node) event.getSource()).getScene().getWindow()); //Setup the Owner of alert
            alert.setHeaderText("Are you sure you want to delete " + selectedPart.getName()); //Setup the header text of alert
            Optional<ButtonType> result = alert.showAndWait(); //Now we show the alert and wait for response
            if (result.get() == ButtonType.YES) { //If user clicked on the YES button, we want to delete the selected part from Inventory
                Inventory.deletePart(selectedPart); //Deleted the selected part from the list
                partTableView.setItems(Inventory.getAllParts()); //Updated the partTableView
                for (int i=0; i<Inventory.getAllProducts().size(); i++){ //Now we go through all the products and see if our deleted part is in their associated list
                    if (Inventory.getAllProducts().get(i).getAllAssociatedParts().contains(selectedPart)) //If it is...
                        Inventory.getAllProducts().get(i).deleteAssociatedPart(selectedPart); //Deleted the part from its associated list
                }
            }
        }
    }

    /** When we click on the ADD button under the product table, we want to open up AddProductPage.fxml
     * @param event The source of the button press */
    @FXML private void addProductClicked(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/AddProductPage.fxml"));
        Parent parent = loader.load();
        Scene addProductPageScene = new Scene(parent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addProductPageScene);
    }

    /** When we click on the MODIFY button under the product table, we want to open up ModifyProductPage.fxml
     * @param event The source of the button press */
    @FXML private void modifyProductClicked(ActionEvent event) throws IOException {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem(); //Get selected product from table
        if (selectedProduct  != null){ //If we actually selected a product load the scene
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/ModifyProductPage.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            ModifyProductController controller = loader.getController();
            controller.initData(selectedProduct);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
        }
    }

    /** When we click on the DELETE button under the product table, we try to delete the product from Inventory
     * @param event The source of the button press */
    @FXML private void deleteProductClicked(ActionEvent event) {
        Product product = productTableView.getSelectionModel().getSelectedItem(); //Get selected item from table
        if (product != null && product.getAllAssociatedParts().size() == 0) { //If we actually selected something and the product doesn't have any parts attached...
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL); //Setup alert with YES and CANCEL buttons
            alert.initModality(Modality.APPLICATION_MODAL); //Setup Modality of alert
            alert.initOwner(((Node) event.getSource()).getScene().getWindow()); //Setup Owner of alert
            alert.setHeaderText("Are you sure you want to delete " + product.getName()); //Setup Header Text of alert
            Optional<ButtonType> result = alert.showAndWait(); //Show the alert and wait for user input
            if (result.get() == ButtonType.YES) { //If they pressed on YES we want to delete that product from Inventory
                Inventory.deleteProduct(product); //Delete product from Inventory
                productTableView.setItems(Inventory.getAllProducts());  //Update the productTableView
            }
        }
        else if (product != null && product.getAllAssociatedParts().size() > 0 ){ //If we actually selected something but the product has parts attached
            Alert alert = new Alert(Alert.AlertType.WARNING, "Product has a part associated with it"); //Setup an alert to warn user that the product has parts attached
            alert.initModality(Modality.APPLICATION_MODAL); //Setup Modality of alert
            alert.initOwner(((Node) event.getSource()).getScene().getWindow()); //Setup Owner of alert
            alert.setHeaderText("Cannot delete " + product.getName()); //State which product we cannot delete
            alert.showAndWait(); //Show the alert and wait for user input (we don't care about user input - we are letting user know why we can't delete product)
        }

    }

    /** When we click on the EXIT button on MainPage, we want to exit the program
     * @param event The source of the button press */
    @FXML private void exitButtonClicked(ActionEvent event){ System.exit(0); }

    /** When we press enter for the searchField in the parts table
     * @param event The source of the button press */
    @FXML private void searchPartsEnter(ActionEvent event){
        String text = searchPartField.getText().trim(); //Get the string from the field and trim off white spaces

        if (text.matches("")){ //If the string is empty...
            partTableView.setItems(Inventory.getAllParts()); //Repopulate the partTableView with all parts in inventory
            searchPartField.setPromptText("Search by ID or Name"); //Setup the promp
            anchorPane.requestFocus(); //Get focus off of the searchPartField
        }
        else { //Otherwise we have a string we can use to search Inventory
            try{
                int id = Integer.parseInt(text); //Try to parse the string into an integer
                ObservableList<Part> part = FXCollections.observableArrayList(); //Create an empty temp list
                part.add(Inventory.lookupPart(id)); //Look in inventory via partID - Inventory will return null if the partID DNE
                if (part.get(0) != null) //If we found the part
                    partTableView.setItems(part); //Populate the partTableView with the one part
                else{ //Otherwise the part DNE
                    searchPartField.setText(""); //Reset the searchPartField
                    searchPartField.setPromptText("Part does not exist!"); //Set prompt showing user that the part DNE
                    anchorPane.requestFocus(); //Get focus off of the searchPartField so user can see prompt
                }
                return; //We successfully parsed the string to int and need to return regardless if we found part or not...
            }catch (Exception e){}

            //This part will only be run if haven't successfully parsed the text into an Integer
            if (Inventory.lookupPart(text).size() == 0) { //Lookup part(s) that contain the text in Inventory, if size == 0 that means we didn't any that match
                searchPartField.setText(""); //Reset searchPartField
                searchPartField.setPromptText("Part does not exist!"); //Set prompt showing user that the part DNE
                anchorPane.requestFocus(); //Get focus off of the searchPartField so user can see prompt
            }
            else //Otherwise we actually found at least one matching part
                partTableView.setItems(Inventory.lookupPart(text)); //Update partTableView to show the parts we found that match the text
        }
    }

    /** When we press enter for the searchField in the product table - Works exactly like searchPartsEnter() */
    @FXML private void searchProductEnter(){
        String text = searchProductField.getText().trim();
        if (text.matches("")){
            searchProductField.setPromptText("Search by ID or Name");
            productTableView.setItems(Inventory.getAllProducts());
            anchorPane.requestFocus();
        }
        else{
            try{
                int id = Integer.parseInt(text);
                ObservableList<Product> product = FXCollections.observableArrayList();
                product.add(Inventory.lookupProduct(id));
                if (product.get(0) != null)
                    productTableView.setItems(product);
                else{
                    searchProductField.setText("");
                    searchProductField.setPromptText("Part does not exist!");
                    anchorPane.requestFocus();
                }
                return;
            }catch (Exception e){}


            if (Inventory.lookupProduct(text).size() == 0) {
                searchProductField.setText("");
                searchProductField.setPromptText("Product does not exist!");
                anchorPane.requestFocus();
            }
            else
                productTableView.setItems(Inventory.lookupProduct(text));
        }
    }



}
