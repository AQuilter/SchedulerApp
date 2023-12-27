package sample.Model;

import java.time.LocalDateTime;

public class Countries {

    private static int countryId;
    private static String countryName;

    /** Constructor
     * @param countryId The country ID.
     * @param countryName the country name.
     */
    public Countries(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    /**
     * @return Returns the country ID.
     */
    public static int getCountryId() {
        return countryId;
    }

    /**
     * @return Returns the country name.
     */
    public static String getCountryName() {
        return countryName;
    }
}
