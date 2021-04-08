package Classes;

/**
 * InHouse class which inherits Part class
 */
public class InHouse extends Part {
    int machineID;

    /** Constructor
     * @param id ID of Part
     * @param machineID MachineID of Part
     * @param max Max value of Part
     * @param min Min value of Part
     * @param name Name of Part
     * @param price Price of Part
     * @param stock Inventory amount of Part */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    public void setMachineID(int machineID){
        this.machineID = machineID;
    }

    public String getMachineID(){
        return Integer.toString(this.machineID);
    }
}
