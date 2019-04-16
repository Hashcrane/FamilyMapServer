package dataAccess;

public class DataAccessException extends Exception {
    DataAccessException(String message)
    {
        super(message);
        System.out.println((message));
    }

    DataAccessException()
    {
        super();
    }
}

