package Model.td;

/**
 * Abstract class person with it's attributes
 */
abstract class Person {

    private String name;
    private String gender;
    private String id;

    /**
     * Empty constructor person
     */
    public Person() {

    }

    /**
     * Constructor Persosn with attributes as parameters
     *
     * @param name
     * @param gender
     * @param id
     */
    public Person(String name, String gender, String id) {
        this.name = name;
        this.gender = gender;
        this.id = id;
    }

    /**
     * Get name person
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set name person
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get gender person
     *
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Set gender person
     *
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Get id person
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Set id person
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the person's attributes
     *
     * @return format
     */
    @Override
    public String toString() {
        return "----- OPERADOR " + " -----" + "\n" +
                "ID: " + this.id + "\n" +
                "Nome: " + this.name + "\n" +
                "Sexo: " + this.gender + "\n";
    }
}
