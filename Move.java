import java.io.Serializable;

class Move implements Serializable {

    // private static final long serialVersionUID = 1L;
    private String command;

    public Move(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return command;
    }

}