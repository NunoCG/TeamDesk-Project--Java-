package Model.td;



import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Scanner;

/**
 * Class Main without attributes where we call every method needed to completely run the full program
 */
public class Main2 {
    static Scanner sc = new Scanner(System.in);

    /**
     * Main method, where we call all the methods to run the full completed program
     *
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {

    }

    /**
     * Prints the question we want to ask, than returns an integer number
     *
     * @param text
     * @return return an integer number
     */
    public static int readInt(String text) {
        try {
            System.out.println(text);
            return Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Insira um valor válido");
            return readInt(text);
        }
    }


    /**
     * Verifys if the integer value inserted is true or false
     *
     * @param text     Question
     * @param numTrue  Number that is considered true
     * @param numFalse Number that is considered false
     * @return
     */
    public static boolean readIntTrueFalse(String text, int numTrue, int numFalse) {
        int num = readInt(text);
        if (numTrue == num) return true;
        if (numFalse == num) return false;
        System.out.println("Valor inválido!");
        return readIntTrueFalse(text, numTrue, numFalse);
    }

    /**
     * Retorns a bollean
     *
     * @param text
     * @return bollean
     */
    public static double readDouble(String text) {
        try {
            System.out.println(text);
            return Double.parseDouble(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Insira um valor válido");
            return readDouble(text);
        }
    }

    /**
     * Prints the question we want to ask, than returns a String
     *
     * @param txt
     * @return String
     */
    public static String readString(String txt) {
        System.out.println(txt);
        String text = sc.nextLine().trim();
        if (!text.equals("")) {
            return text;
        }
        return readString(txt);
    }

    /**
     * Prints the question we want to ask, than returns a boolean
     *
     * @param text
     * @return boolean
     */
    public static boolean readBoolean(String text) {
        System.out.println(text);
        return sc.nextBoolean();
    }
}