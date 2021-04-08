package Classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        for (int i=0; i<getAllAssociatedParts().size(); i++){
            if (associatedParts.get(i) == selectedAssociatedPart){
                associatedParts.remove(i);
                return true;
            }
        }
        return false;
    }

    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }

    //============SETTERS AND GETTERS============//
    //ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    //Name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //Price
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    //Inventory / Stock
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }

    //Minimums
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }

    //Maximums
    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
}
