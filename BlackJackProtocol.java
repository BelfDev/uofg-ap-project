
class BlackJackProtocol implements ApplicationProtocol {

    public BlackJackProtocol() {

    }

    @Override
    public String processInput(String input) {
        String output;
        if (input.equals("oi")) {
            output = "koeh";
        } else {
            output = "não sei";
        }
        return output;
    }

}