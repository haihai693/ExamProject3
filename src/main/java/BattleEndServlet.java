import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import creature.*;
import creature.Creature;
import creature.Character;
import creature.Monster;

@WebServlet("/BattleEndServlet")
public class BattleEndServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        ArrayList<Character> party = (ArrayList<Character>) session.getAttribute("party");
        ArrayList<Monster> monsters = (ArrayList<Monster>) session.getAttribute("monsters");
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>戦闘終了</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>戦闘終了</h1>");

        if ((party == null || party.isEmpty()) && (monsters == null || monsters.isEmpty())) {
            out.println("<p>戦闘が不明な状態で終了しました。</p>");
        } else if (party == null || party.isEmpty()) {
            out.println("<p>味方パーティは全滅してしまった…</p>");
        } else if (monsters == null || monsters.isEmpty()) {
            out.println("<p>敵を全て倒した！勇者達は勝利した！</p>");
        } else {
            out.println("<p>戦闘が終了しました。</p>");
        }

        out.println("<br>");
        out.println("<form action=\"HelloServlet\" method=\"get\">");
        out.println("<button type=\"submit\" name=\"action\" value=\"start_game\">もう一度遊ぶ</button>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");

        session.invalidate();
    }
}
