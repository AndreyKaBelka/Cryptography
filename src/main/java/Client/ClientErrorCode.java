package Client;

public enum ClientErrorCode {
    NOT_CONNECTED("Can`t connect to the server!"),
    WRONG_IP_ADDRESS("Wrong IP address!"),
    NAME_NOT_ACCEPTED("The name is already taken, please change it and try again...");

    private String errorString;
    ClientErrorCode(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString(){
        return errorString;
    }
}
