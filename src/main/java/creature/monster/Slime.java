package creature.monster;
import creature.Monster;
import creature.Creature;

public final class Slime extends Monster {
    public Slime(char suffix, int hp) {
        super("スライム", suffix, hp);
    }

    public String attack(Creature target) {
        target.setHp(target.getHp() - 5);
        return "スライム" + this.getSuffix() + "は体当たり攻撃！" + target.getName() + "に5のダメージを与えた！";
    }
}
