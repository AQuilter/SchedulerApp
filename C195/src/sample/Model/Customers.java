package sample.Model;

public class Customers {

    private final int customerId;
    private final String name;
    private final String address;
    private final String postalCode;
    private final String phoneNumber;
    private final String country;

    // not part of entity:
    private final int divisionID;
    private final String divisionName;


    /** Constructor
     * @param customerId The customer ID.
     * @param name The customer name.
     * @param address The customer address.
     * @param postalCode The customer postal code.
     * @param phoneNumber The customer phone number.
     * @param divisionID The division number.
     * */
    public Customers(int customerId, String name, String address, String postalCode, String phoneNumber, String country,
                     int divisionID, String divisionName) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.divisionID = divisionID;
        this.divisionName = divisionName;
    }

    /**
     * @return Returns the customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @return Returns the customer's name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the customer's address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return Returns the customer's postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @return Returns the customer's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return Returns the division ID.
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @return Returns the division name.
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * @return Returns the country name.
     */
    public String getCountry() {
        return country;
    }
}
