package creature;

public interface Creature {
    boolean isAlive();
    String getStatusString();
    String attack(Creature target);
    String getName();
    int getHp();
    void setHp(int hp);
    String die();
}


