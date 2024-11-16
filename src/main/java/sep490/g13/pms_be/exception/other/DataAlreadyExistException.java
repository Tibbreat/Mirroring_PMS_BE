package sep490.g13.pms_be.exception.other;

public class DataAlreadyExistException  extends RuntimeException{
    public DataAlreadyExistException(String message){
        super(message);
    }
}
