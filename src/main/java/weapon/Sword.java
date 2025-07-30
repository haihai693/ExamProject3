package weapon;

public class Sword extends Weapon {

    public Sword() {
        super("剣", 10);
    }

    public String attackMessage() {
        return "で切りつけた！";
    }
}


