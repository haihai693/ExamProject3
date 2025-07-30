package creature.character;
import creature.Character;
import creature.Creature;
import weapon.Weapon;

public class Thief extends Character {
    private boolean guard;

    public Thief(String name, int hp, Weapon weapon) {
        super(name, hp, weapon);
        this.guard = false;
    }

    public boolean isGuard() {
        return guard;
    }

    public void setGuard(boolean guard) {
        this.guard = guard;
    }

    public String attack(Creature target) {
        int damage = (this.getWeapon().getDamage() * 2);
        target.setHp(target.getHp() - damage);
        return this.getName() + "は素早く2回攻撃した！" + this.getWeapon().attackMessage() + target.getName() + "に" + damage + "のダメージを与えた！";
    }

    public String guard() {
        this.setGuard(true);
        return this.getName() + "は身を守っている！";
    }

    public void setHp(int hp) {
        if (this.isGuard() && hp < this.getHp()) {
        } else {
            super.setHp(hp);
        }
    }
}
