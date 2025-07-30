package creature.character;
import creature.Character;
import creature.Creature;
import weapon.Weapon;

public class SuperHero extends Hero {
    private String evolutionMessage;

    public SuperHero(Hero hero) {
        super(hero.getName(), hero.getHp(), hero.getWeapon());
        this.setHp(this.getHp() - 30);
        this.evolutionMessage = this.getName() + "はスーパーヒーローに変身した！";
        if (hero.getHp() > 30) {
            this.evolutionMessage += "代償として30のダメージを受けた！";
        }
    }

    public String attack(Creature target) {
        int damage = (int) (this.getWeapon().getDamage() * 2.5);
        target.setHp(target.getHp() - damage);
        return this.getName() + "は" + this.getWeapon().getName() + this.getWeapon().attackMessage() + target.getName() + "に" + damage + "のダメージを与えた！";
    }

    public String getEvolutionMessage() {
        return evolutionMessage;
    }
}


