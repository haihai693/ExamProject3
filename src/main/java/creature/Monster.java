package creature;
public abstract class Monster implements Creature {
    private String name;
    private int hp;
    private char suffix;

    public Monster(String name, char suffix, int hp) {
        if (hp < 0) {
            throw new IllegalArgumentException("初期設定に誤りがあるため、キャラクターを作成できませんでした");
        }
        this.setName(name);
        this.setSuffix(suffix);
        this.setHp(hp);
    }

    public final boolean isAlive() {
        return this.getHp() > 0;
    }

    public String getStatusString() {
        return this.getName() + this.getSuffix() + "：HP " + this.getHp();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getSuffix() {
        return suffix;
    }

    public void setSuffix(char suffix) {
        this.suffix = suffix;
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

    public String run() {
        return this.getName() + this.getSuffix() + "は逃げ出した";
    }

    public String die() {
        return this.getName() + this.getSuffix() + "を倒した！";
    }

    public abstract String attack(Creature target);
}
