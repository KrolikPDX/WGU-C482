package Classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory class which holds a static list of Parts and Products
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList(); //List of all parts we have in our inventory
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList(); //List of all products we have in our inventory

    /** Adds a part to our list
     * @param newPart Part to be added to allParts ObservableList */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /** Adds a product to our list
     * @param newProduct Product  to be added to allParts ObservableList*/
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

     /** Returns the part corresponding to the ID, returns null if the part DNE
      * @param partID ID we will look up
      * @return Returns the part we are looking for */
    public static Part lookupPart(int partID){
        for (int i=0; i<allParts.size(); i++){
            if (allParts.get(i).getId() == partID)
                return allParts.get(i);
        }
        return null;
    }

     /** Returns the product corresponding to the ID, returns null if the product DNE
      * @param productID ID we will look up
      * @return Returns the product we are looking for */
    public static Product lookupProduct(int productID){
        for (int i=0; i<allProducts.size(); i++){
            if (allProducts.get(i).getId() == productID)
                return allProducts.get(i);
        }
        return null;
    }

     /** Returns a list with all the parts that contain the given partName
      * @param partName String we are searching for in other parts
      * @return Returns a List of Parts that contain partName*/
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> parts = FXCollections.observableArrayList();
        for (int i=0; i<getAllParts().size(); i++){
            if(getAllParts().get(i).getName().toLowerCase().contains(partName.toLowerCase())){
                parts.add(getAllParts().get(i));
            }
        }
        return parts;
    }

    /** Returns a list with all the products that contain the given productName
     * @param productName String we are searching for in other products
     * @return Returns a List of Products that contain productName */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> products = FXCollections.observableArrayList();
        for (int i=0; i<getAllProducts().size(); i++){
            if(getAllProducts().get(i).getName().toLowerCase().contains(productName.toLowerCase())){
                products.add(getAllProducts().get(i));
            }
        }
        return products;
    }

     /** Simply replaces the part at the given index with the selectedPart
      * @param index Index of the part we are replacing
      * @param selectedPart The new part that will be replacing the old */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

     /** Simply replaces the product at the given index with the selectedProduct
      * @param index Index of the product we are replacing
      * @param selectedProduct The new product that will be replacing the old */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

     /** Simply returns the results if we successfully removed the selectedPart from our list
      * @param selectedPart The part we will delete from the allParts list
      * @return Returns true if selectedPart was removed successfully */
    public static boolean deletePart(Part selectedPart){ return allParts.remove(selectedPart); }

     /** Returns the results if we successfully removed the selectedProduct from our list
      * @param selectedProduct The product we will delete from the allProducts list
      * @return Returns true if selectedProduct was removed successfully */
    public static boolean deleteProduct(Product selectedProduct){ return allProducts.remove(selectedProduct); }

    /** Returns the ObservableList of all our parts
     * @return Returns list of allParts*/
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /** Returns the ObservableList of all our products
     * @return Returns list of allProducts */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
