package sample.Model;

public class Contacts {

    private int contactId;
    private String contactName;
    private String contactEmail;

    /** Constructor
     @param contactId the contact ID.
     @param contactName the contact name.
     @param contactEmail the contact email.
     */
    public Contacts(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * @return Returns the contact Id
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @return Returns the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @return Returns the contact email
     */
    public String getContactEmail() {
        return contactEmail;
    }
}
