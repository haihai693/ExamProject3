package weapon;

public class Wand extends Weapon {

    public Wand() {
        super("魔法の杖", 15);
        super.setCost(5);
    }

    public String attackMessage() {
        return "から魔法を放った！";
    }
}


