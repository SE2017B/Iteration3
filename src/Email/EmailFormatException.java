package Email;

public class EmailFormatException extends Exception {
    public EmailFormatException(){
        super("Email is not formatted correctly");
    }
}
