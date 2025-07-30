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
import java.util.stream.Collectors;

@WebServlet("/SelectServlet")
public class SelectServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        ArrayList<Character> party = (ArrayList<Character>) session.getAttribute("party");
        ArrayList<Monster> monsters = (ArrayList<Monster>) session.getAttribute("monsters");
        Integer currentTurnIndex = (Integer) session.getAttribute("currentTurnIndex");

        // ゲームオーバー条件を最初に確認
        if (party == null || party.isEmpty()) {
            response.sendRedirect("BattleEndServlet");
            return;
        }
        if (monsters == null || monsters.isEmpty()) {
            response.sendRedirect("BattleEndServlet");
            return;
        }

        Character currentCharacter = null;
        // 次に生存しているキャラクターを探す
        while (currentTurnIndex < party.size()) {
            if (party.get(currentTurnIndex).isAlive()) {
                currentCharacter = party.get(currentTurnIndex);
                break;
            }
            currentTurnIndex++;
        }

        if (currentCharacter == null) {
            // 生存している全キャラクターが行動済み、またはパーティーが全滅している
            response.sendRedirect("MonsterServlet"); // 全員が行動したら敵のターンへ進む
            return;
        }

        session.setAttribute("currentTurnIndex", currentTurnIndex);

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>行動選択</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>行動選択</h1>");

        out.println("<h3>味方パーティ</h3>");
        out.println(getPartyStatusHtml(party));
        out.println("<h3>敵グループ</h3>");
        out.println(getMonsterStatusHtml(monsters));
        out.println("<hr>");
        out.println("<h2>" + currentCharacter.getName() + "のターン</h2>");
        out.println("<form action=\"BattleServlet\" method=\"post\">");
        out.println("<input type=\"hidden\" name=\"characterIndex\" value=\"" + currentTurnIndex + "\">");

        if (currentCharacter instanceof Hero) {
            out.println("<input type=\"radio\" name=\"action\" value=\"1\" checked>攻撃<br>");
            if (!(currentCharacter instanceof SuperHero)) {
                out.println("<input type=\"radio\" name=\"action\" value=\"2\">スーパーヒーローになる<br>");
            }
        } else if (currentCharacter instanceof Wizard) {
            out.println("<input type=\"radio\" name=\"action\" value=\"1\" checked>攻撃<br>");
            out.println("<input type=\"radio\" name=\"action\" value=\"2\">魔法攻撃 (MP:" + ((Wizard) currentCharacter).getMp() + ")<br>");
        } else if (currentCharacter instanceof Thief) {
            out.println("<input type=\"radio\" name=\"action\" value=\"1\" checked>攻撃<br>");
            out.println("<input type=\"radio\" name=\"action\" value=\"2\">防御<br>");
        } else {
            out.println("<input type=\"radio\" name=\"action\" value=\"1\" checked>攻撃<br>");
        }

        out.println("<br>");
        out.println("誰に？ ");
        out.println("<select name=\"targetIndex\">");

        List<Monster> aliveMonsters = monsters.stream().filter(Monster::isAlive).collect(Collectors.toList());

        if (aliveMonsters.isEmpty()) {
            out.println("<option value=\"-1\" disabled selected>（敵はいない）</option>");
            out.println("</select>");
            out.println("<button type=\"submit\" disabled>決定</button>"); // ターゲットがいない場合、ボタンを無効化
        } else {
            for (int i = 0; i < monsters.size(); i++) {
                Monster m = monsters.get(i);
                if (m.isAlive()) {
                    out.println("<option value=\"" + i + "\">" + m.getName() + m.getSuffix() + "</option>");
                }
            }
            out.println("</select>");
            out.println("<button type=\"submit\">決定</button>");
        }

        out.println("</form>");
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
}
