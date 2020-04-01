/**
 * This interface enforces the implementation of an ApplicationProtocol.
 */
interface ApplicationProtocol {
    ServerResponse processInput(ClientRequest request);
}