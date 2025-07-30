package creature;

import weapon.Weapon;

public abstract class Character implements Creature {
    private String name;
    private int hp;
    private Weapon weapon;

    public Character(String name, int hp, Weapon weapon) {
        if (hp < 0) {
            throw new IllegalArgumentException("初期設定に誤りがあるため、キャラクターを作成できませんでした");
        }
        this.setName(name);
        this.setHp(hp);
        this.setWeapon(weapon);
    }

    public final boolean isAlive(){
        return this.getHp() > 0;
    }

    public String getStatusString() {
        return this.getName() + "：HP " + this.getHp();
    }

    public String die(){
        return this.getName() + "は死んでしまった!";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }
    public Weapon getWeapon(){
        return weapon;
    }

    public void setWeapon(Weapon weapon){
        this.weapon = weapon;
    }

    public abstract String attack(Creature target);
}
