package creature.character;
import creature.Character;
import creature.Creature;
import weapon.Weapon;

public class Wizard extends Character {
    private int mp;

    public Wizard(String name, int hp, int mp, Weapon weapon) {
        super(name, hp, weapon);
        this.mp = mp;
    }

    public String magic(Creature target){
        if (this.mp < this.getWeapon().getCost()) {
            return "MPが足りない！";
        }
        this.mp -= this.getWeapon().getCost();
        target.setHp(target.getHp() - this.getWeapon().getDamage());
        return this.getName() + "は" + this.getWeapon().getName() + this.getWeapon().attackMessage() + target.getName() + "に" + this.getWeapon().getDamage() + "のダメージを与えた！";
    }

    public String attack(Creature target){
        target.setHp(target.getHp() - 3);
        return this.getName() + "は石を投げた！" + target.getName() + "に3のダメージを与えた！" ;
    }

    public String getStatusString() {
        return this.getName() + "：HP " + this.getHp() + ", MP " + this.mp;
    }

    public int getMp() {
        return mp;
    }
    public void setMp(int mp) {
        this.mp = mp;
    }
}
