/**
 * This model class represents the Dealer entity.
 */
public class Dealer extends Player {

    public Dealer() {
        this(null, 0);
    }

    public Dealer(String id, Integer slot) {
        super(id, slot);
    }

}
