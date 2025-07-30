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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/HelloServlet")
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        if ("start_game".equals(action)) {
            session.invalidate(); // 既存のセッションを無効にし、新しいゲームのためにクリア
            session = request.getSession(); // 新しいセッションを取得

            ArrayList<Character> party = new ArrayList<>();
            party.add(new Hero("勇者", 100, new Sword()));
            party.add(new Wizard("魔法使い", 60, 20, new Wand()));
            party.add(new Thief("盗賊", 70, new Dagger()));

            ArrayList<Monster> monsters = new ArrayList<>();
            Random random = new Random();
            Map<String, Integer> monsterCounts = new HashMap<>(); // 敵の種類の数を追跡し、枝番を管理

            for (int i = 0; i < 5; i++) {
                int monsterType = random.nextInt(3); // 0: Slime, 1: Goblin, 2: Matango
                String monsterName;
                Monster newMonster;

                switch (monsterType) {
                    case 0:
                        monsterName = "スライム";
                        break;
                    case 1:
                        monsterName = "ゴブリン";
                        break;
                    case 2:
                        monsterName = "お化けキノコ";
                        break;
                    default:
                        monsterName = "スライム"; // 通常は発生しない
                        break;
                }

                monsterCounts.put(monsterName, monsterCounts.getOrDefault(monsterName, 0) + 1);
                char suffix = (char) ('A' + (monsterCounts.get(monsterName) - 1));

                switch (monsterType) {
                    case 0:
                        newMonster = new Slime(suffix, 40);
                        break;
                    case 1:
                        newMonster = new Goblin(suffix, 50);
                        break;
                    case 2:
                        newMonster = new Matango(suffix, 45);
                        break;
                    default:
                        newMonster = new Slime(suffix, 40); // 通常は発生しない
                        break;
                }
                monsters.add(newMonster);
            }

            session.setAttribute("party", party);
            session.setAttribute("monsters", monsters);
            session.setAttribute("currentTurnIndex", 0);
            session.setAttribute("battleMessages", new ArrayList<String>()); // 戦闘メッセージを初期化

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>戦闘準備</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>戦闘準備</h1>");
            out.println("<h2>味方パーティ</h2>");
            out.println(getPartyStatusHtml(party));
            out.println("<h2>敵グループ</h2>");
            out.println(getMonsterStatusHtml(monsters));
            out.println("<form action=\"SelectServlet\" method=\"get\">");
            out.println("<button type=\"submit\">ゲーム開始</button>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");

        } else {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Hello World!</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Hello World!</h1>");
            out.println("<br>");
            out.println("<a href=\"HelloServlet?action=start_game\">ゲーム開始</a>");
            out.println("</body>");
            out.println("</html>");
        }
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
