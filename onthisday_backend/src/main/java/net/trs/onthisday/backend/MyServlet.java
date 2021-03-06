
package net.trs.onthisday.backend;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static net.trs.onthisday.backend.Month.JANUARY;

// TODO Add in error checking so that the web app is more tolorent of misspelled month names
public class MyServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("Please use the form to POST to this url");
        resp.getWriter().println("<a href=\"onthisday.teamrampage.net\">Start over</a>");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("<script>\n" +
                "  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n" +
                "  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n" +
                "  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n" +
                "  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');\n" +
                "\n" +
                "  ga('create', 'UA-29375172-7', 'auto');\n" +
                "  ga('send', 'pageview');\n" +
                "\n" +
                "</script>");
        Month month = JANUARY;

        try {
            month = Month.getMonth(req.getParameter("month"));
        } catch (Exception e) {
            resp.getWriter().println("A valid and properly spelled month name must be supplied");
            resp.getWriter().println("<a href=\"onthisday.teamrampage.net\">Start over</a>");
        }
        String targetDay = req.getParameter("day");
        String year = req.getParameter("year");

        java.util.HashMap<String, String> results = null;
        try {
            results = net.trs.onthisday.backend.SongOnDay.getSoD(month, targetDay, year);
        } catch (Exception e) {
            resp.getWriter().println("Error getting the song information requested: \n" + e.getMessage());
            resp.getWriter().println("<a href=\"onthisday.teamrampage.net\">Start over</a>");
        }

        String songName = results.get("songName");
        String songArtist = results.get("songArtist");

        resp.getWriter().println("<p>");
        resp.getWriter().println("Song: " + songName + "<br />");
        resp.getWriter().println("Artist: " + songArtist + "</p>");
        resp.getWriter().println("<a href=\"http://onthisday.teamrampage.net\">Start over</a>");
    }
}
