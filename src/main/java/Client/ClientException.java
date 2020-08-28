package Client;

public class ClientException extends Exception {
    private ClientErrorCode clientErrorCode;

    public ClientException(ClientErrorCode clientErrorCode){
        this.clientErrorCode = clientErrorCode;
    }

    public ClientErrorCode getClientErrorCode() {
        return clientErrorCode;
    }
}
