package creature.monster;
import creature.Monster;
import creature.Creature;

public class Goblin extends Monster {
    public Goblin(char suffix, int hp) {
        super("ゴブリン", suffix, hp);
    }

    public String attack(Creature target) {
        target.setHp(target.getHp() - 8);
        return "ゴブリン" + this.getSuffix() + "はナイフで切りつけた！" + target.getName() + "に8のダメージを与えた！";
    }
}

