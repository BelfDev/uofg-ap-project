/**
 * This interface ensures that classes that intend to send out
 * requests offer an implementation of the sendRequest method.
 */
public interface RequestSender {
    void sendRequest(ClientRequest request);
}
