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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;
import java.util.stream.Collectors;

@WebServlet("/MonsterServlet")
public class MonsterServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        ArrayList<Character> party = (ArrayList<Character>) session.getAttribute("party");
        ArrayList<Monster> monsters = (ArrayList<Monster>) session.getAttribute("monsters");
        ArrayList<String> battleMessages = (ArrayList<String>) session.getAttribute("battleMessages");

        if (battleMessages == null) {
            battleMessages = new ArrayList<>();
            session.setAttribute("battleMessages", battleMessages);
        }

        // 進行する前にゲームオーバー条件を確認
        if (party == null || party.isEmpty()) {
            response.sendRedirect("BattleEndServlet");
            return;
        }
        if (monsters == null || monsters.isEmpty()) {
            response.sendRedirect("BattleEndServlet");
            return;
        }

        ArrayList<String> monsterAttackMessages = new ArrayList<>();
        Random random = new Random();

        // ConcurrentModificationExceptionを避けるため、モンスターリストのコピーをイテレート
        for (Monster currentMonster : new ArrayList<>(monsters)) {
            if (!currentMonster.isAlive()) {
                continue;
            }

            List<Character> livingPartyMembers = party.stream().filter(Character::isAlive).collect(Collectors.toList());

            if (!livingPartyMembers.isEmpty()) {
                Character targetCharacter = livingPartyMembers.get(random.nextInt(livingPartyMembers.size()));

                monsterAttackMessages.add("---" + currentMonster.getName() + currentMonster.getSuffix() + "の攻撃！---");

                // 盗賊の防御チェック
                if (targetCharacter instanceof Thief && ((Thief) targetCharacter).isGuard()) {
                    monsterAttackMessages.add(currentMonster.getName() + currentMonster.getSuffix() + "は攻撃を仕掛けたが、" + targetCharacter.getName() + "は攻撃を回避し、ダメージが入らなかった！");
                } else {
                    monsterAttackMessages.add(currentMonster.attack(targetCharacter));
                }

                // 敵の攻撃後、キャラクターのステータスを確認
                checkCharacterStatus(party, monsterAttackMessages);

                // 敵のターン中にパーティーが全滅した場合、ループを終了
                if (party.isEmpty()) {
                    break;
                }
            } else {
                // 生存しているパーティーメンバーがいない場合、ゲームオーバー
                break;
            }
        }

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>戦闘結果</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>戦闘結果</h1>");

        if (!battleMessages.isEmpty()) {
            out.println("---味方の攻撃---<br>"); // プレイヤーの行動のヘッダーを追加
            for (String msg : battleMessages) {
                out.println(msg + "<br>");
            }
        }

        if (!monsterAttackMessages.isEmpty()) {
            out.println("<br>---敵の攻撃---<br>"); // 敵の行動のヘッダーを追加
            for(String msg : monsterAttackMessages) {
                out.println(msg + "<br>");
            }
        }

        out.println("<hr>");
        out.println("<h3>現在のステータス</h3>");
        out.println("<h4>味方パーティ</h4>");
        out.println(getPartyStatusHtml(party));
        out.println("<h4>敵グループ</h4>");
        out.println(getMonsterStatusHtml(monsters));

        session.setAttribute("party", party);
        session.setAttribute("monsters", monsters);
        session.setAttribute("battleMessages", new ArrayList<String>()); // 次のターンのためにメッセージをクリア

        // 敵の攻撃後にゲーム終了条件を確認
        if (party.isEmpty() || monsters.isEmpty()) {
            response.sendRedirect("BattleEndServlet");
        } else {
            out.println("<form action=\"SelectServlet\" method=\"get\">");
            out.println("<button type=\"submit\">次のターンへ</button>");
            out.println("</form>");
        }
        out.println("</body>");
        out.println("</html>");
    }

    private String getPartyStatusHtml(List<Character> party) {
        StringBuilder sb = new StringBuilder();
        if (party.isEmpty()) {
            sb.append("（パーティは全滅しています）");
        } else {
            for (Character member : party) {
                sb.append(member.getStatusString()).append("<br>");
            }
        }
        return sb.toString();
    }
    private String getMonsterStatusHtml(List<Monster> monsters) {
        StringBuilder sb = new StringBuilder();
        if (monsters.isEmpty()) {
            sb.append("（敵は全滅しています）");
        } else {
            for (Monster monster : monsters) {
                sb.append(monster.getStatusString()).append("<br>");
            }
        }
        return sb.toString();
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

