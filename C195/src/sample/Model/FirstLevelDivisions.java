package sample.Model;

import java.time.LocalDateTime;

public class FirstLevelDivisions {

    private int divisionId;
    private String divisionName;
    private int countryId; // not part of the entity

    /** Constructor
     * @param divisionId the division ID.
     * @param divisionName the division name.
     * @param countryId the country ID.
     */
    public FirstLevelDivisions(int divisionId, String divisionName, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }


    /**
     * @return Returns the division ID.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * @return Returns the division name.
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * @return Returns the country ID.
     */
    public int getCountryId() {
        return countryId;
    }
}
