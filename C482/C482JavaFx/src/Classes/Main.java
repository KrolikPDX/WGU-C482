package Classes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class creates an app which will help manage inventory
 */
public class Main extends Application {

    /** Setup Stage and load MainPage.fxml */
    @Override public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
        primaryStage.setTitle("C482");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }

    /** Main method which creates parts and products to add to inventory for testing purposes
     * @param args Used to start main method */
    public static void main(String[] args){
        InHouse part = new InHouse(1, "Tire", 5, 1,1,1, 5);
        OutSourced part1 = new OutSourced(2, "Brakes", 20, 1,1,2, "Sherlock");
        OutSourced part2 = new OutSourced(3, "Air", 20, 4,3,6, "Benz");
        OutSourced part3 = new OutSourced(4, "Pipes", 20, 6,1,8, "Init");
        InHouse part4 = new InHouse(5, "Stick", 5, 1,1,5, 5);
        Product product = new Product(1, "Bike", 500, 3,1,4);
        Product product1 = new Product(2, "Bench", 10, 2, 1, 4);
        Inventory.addPart(part);
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);
        product.addAssociatedPart(part1);
        product.addAssociatedPart(part);
        product1.addAssociatedPart(part2);
        product1.addAssociatedPart(part3);
        product1.addAssociatedPart(part4);
        Inventory.addProduct(product);
        Inventory.addProduct(product1);

        launch(args);


    }
}
