package creature.character;
import creature.Character;
import creature.Creature;
import weapon.Weapon;

public class Hero extends Character {
    public Hero(String name, int hp, Weapon weapon) {
        super(name, hp, weapon);
    }

    public String attack(Creature target) {
        target.setHp(target.getHp() - this.getWeapon().getDamage());
        return this.getName() + "は" + this.getWeapon().getName() + this.getWeapon().attackMessage() + target.getName() + "に" + this.getWeapon().getDamage() + "のダメージを与えた！";
    }
}



