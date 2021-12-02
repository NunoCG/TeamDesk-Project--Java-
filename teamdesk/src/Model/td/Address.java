package Model.td;

/**
 *
 */
public class Address {

    private int id;
    private String address;
    private String postalCode;
    private String location;
    private String country;
    private boolean mainAddress;
    private int nif;

    /**
     *
     * @param id
     * @param address
     * @param postalCode
     * @param location
     * @param country
     * @param mainAddress
     * @param nif
     */
    public Address(int id, String address, String postalCode, String location, String country, boolean mainAddress, int nif) {
        this.id = id;
        this.address = address;
        this.postalCode = postalCode;
        this.location = location;
        this.country = country;
        this.mainAddress = mainAddress;
        this.nif = nif;
    }

    /**
     *
     */
    public Address() {}

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     *
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     *
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     */
    public boolean getMainAddress() {
        return mainAddress;
    }

    /**
     *
     * @param mainAddress
     */
    public void setMainAddress(boolean mainAddress) {
        this.mainAddress = mainAddress;
    }

    /**
     *
     * @return
     */
    public int getNif() {
        return nif;
    }

    /**
     *
     * @param nif
     */
    public void setNif(int nif) {
        this.nif = nif;
    }


    public String formatterAddress(){

        return String.format("%s\n%s, %s - %s",this.address,this.postalCode,this.location,this.country);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", location='" + location + '\'' +
                ", country='" + country + '\'' +
                ", mainAddress='" + mainAddress + '\'' +
                ", nif=" + nif +
                '}';
    }
}
