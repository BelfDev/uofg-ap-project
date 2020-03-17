
class BlackJackProtocol implements ApplicationProtocol {

    public BlackJackProtocol() {

    }

    @Override
    public Move processInput(Move input) {
        String output;
        if (input.getCommand().equals("oi")) {
            output = "koeh";
        } else {
            output = "não sei";
        }
        Move move = new Move(output);
        return move;
    }

}
