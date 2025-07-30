import creature.*;
import creature.character.*;
import creature.monster.*;
import creature.Character;
import creature.Monster;
import creature.Creature;
import weapon.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

@WebServlet("/BattleServlet")
public class BattleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        ArrayList<Character> party = (ArrayList<Character>) session.getAttribute("party");
        ArrayList<Monster> monsters = (ArrayList<Monster>) session.getAttribute("monsters");
        Integer currentTurnIndex = (Integer) session.getAttribute("currentTurnIndex");
        ArrayList<String> battleMessages = (ArrayList<String>) session.getAttribute("battleMessages");

        if (battleMessages == null) {
            battleMessages = new ArrayList<>();
            session.setAttribute("battleMessages", battleMessages);
        }

        // 進行する前にゲームオーバー条件を確認
        if (party == null || party.isEmpty() || monsters == null || monsters.isEmpty()) {
            response.sendRedirect("BattleEndServlet");
            return;
        }

        int characterIndex = Integer.parseInt(request.getParameter("characterIndex"));
        int actionChoice = Integer.parseInt(request.getParameter("action"));
        int targetIndex = -1;
        String targetIndexParam = request.getParameter("targetIndex");
        if (targetIndexParam != null && !targetIndexParam.isEmpty()) {
            targetIndex = Integer.parseInt(targetIndexParam);
        }

        Character currentCharacter = party.get(characterIndex);
        Monster targetMonster = (targetIndex != -1 && targetIndex < monsters.size()) ? monsters.get(targetIndex) : null;

        if (!currentCharacter.isAlive()) {
            battleMessages.add(currentCharacter.getName() + "はすでに倒れているため行動できませんでした。");
        } else {
            // 盗賊の防御状態をターン開始時にリセット
            if (currentCharacter instanceof Thief) {
                ((Thief) currentCharacter).setGuard(false);
            }

            if (currentCharacter instanceof Hero) {
                if (actionChoice == 1) { // 攻撃
                    if (targetMonster != null && targetMonster.isAlive()) {
                        battleMessages.add(currentCharacter.attack(targetMonster)); // attackメソッドがメッセージを含む
                    } else {
                        battleMessages.add(currentCharacter.getName() + "は攻撃したが、ターゲットがいなかった。");
                    }
                } else if (actionChoice == 2) { // スーパーヒーローに変身
                    if (!(currentCharacter instanceof SuperHero)) {
                        Hero hero = (Hero) currentCharacter;
                        SuperHero superHero = new SuperHero(hero);
                        battleMessages.add(hero.getName() + "はスーパーヒーローに進化した！");
                        battleMessages.add(superHero.getEvolutionMessage());
                        // パーティーリスト内の古いHeroオブジェクトを新しいSuperHeroオブジェクトに置き換える
                        int heroIdx = party.indexOf(hero);
                        if (heroIdx != -1) {
                            party.set(heroIdx, superHero);
                        }
                        if (!superHero.isAlive()) {
                            battleMessages.add(superHero.die());
                        }
                    } else {
                        battleMessages.add(currentCharacter.getName() + "はすでにスーパーヒーローだ！");
                    }
                }
            } else if (currentCharacter instanceof Wizard) {
                Wizard wizardChar = (Wizard) currentCharacter;
                if (actionChoice == 1) { // 攻撃（石投げ）
                    if (targetMonster != null && targetMonster.isAlive()) {
                        battleMessages.add(wizardChar.attack(targetMonster));
                    } else {
                        battleMessages.add(wizardChar.getName() + "は石を投げたが、ターゲットがいなかった。");
                    }
                } else if (actionChoice == 2) { // 魔法攻撃
                    if (targetMonster != null && targetMonster.isAlive()) {
                        String magicResult = wizardChar.magic(targetMonster);
                        battleMessages.add(magicResult); // Magicメソッドがメッセージを返す
                    } else {
                        battleMessages.add(wizardChar.getName() + "は魔法を唱えたが、ターゲットがいなかった。");
                    }
                }
            } else if (currentCharacter instanceof Thief) {
                Thief thiefChar = (Thief) currentCharacter;
                if (actionChoice == 1) { // 攻撃
                    if (targetMonster != null && targetMonster.isAlive()) {
                        battleMessages.add(thiefChar.attack(targetMonster));
                    } else {
                        battleMessages.add(thiefChar.getName() + "は攻撃したが、ターゲットがいなかった。");
                    }
                } else if (actionChoice == 2) { // 守る
                    battleMessages.add(thiefChar.guard());
                }
            }
        }

        checkMonsterStatus(monsters, battleMessages);
        checkCharacterStatus(party, battleMessages);

        session.setAttribute("party", party);
        session.setAttribute("monsters", monsters);
        session.setAttribute("battleMessages", battleMessages);

        // プレイヤーの行動後にゲームオーバーを確認
        if (party.isEmpty() || monsters.isEmpty()) {
            response.sendRedirect("BattleEndServlet");
            return;
        }

        // 次のキャラクターのターンを決定
        int nextTurnIndex = currentTurnIndex + 1;
        while (nextTurnIndex < party.size() && !party.get(nextTurnIndex).isAlive()) {
            nextTurnIndex++;
        }

        if (nextTurnIndex >= party.size()) {
            // 生存している全キャラクターが行動済み。敵のターンへ進む
            session.setAttribute("currentTurnIndex", 0); // 次のラウンドのためにインデックスをリセット
            response.sendRedirect("MonsterServlet");
        } else {
            // 次に生存しているキャラクターのターン
            session.setAttribute("currentTurnIndex", nextTurnIndex);
            response.sendRedirect("SelectServlet");
        }
    }

    public void checkMonsterStatus(ArrayList<Monster> monsters, List<String> messages) {
        Iterator<Monster> it = monsters.iterator();
        while (it.hasNext()) {
            Monster m = it.next();
            if (!m.isAlive()) {
                messages.add(m.die());
                it.remove();
            } else if (m.getHp() <= 5) { // HPが5以下の敵は逃げ出す
                messages.add(m.run());
                it.remove();
            }
        }
    }
    public void checkCharacterStatus(ArrayList<Character> party, List<String> messages) {
        Iterator<Character> it = party.iterator();
        while (it.hasNext()) {
            Character c = it.next();
            if (!c.isAlive()) {
                messages.add(c.die());
                it.remove();
            }
        }
    }
}
