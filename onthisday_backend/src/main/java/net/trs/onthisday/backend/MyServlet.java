
package net.trs.onthisday.backend;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static net.trs.onthisday.backend.Month.JANUARY;

public class MyServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        Month month = JANUARY;

        try {
            month = Month.getMonth(req.getParameter("month"));
        } catch (Exception e) {
            resp.getWriter().println("A valid and properly spelled month name must be supplied");
        }
        String targetDay = req.getParameter("day");
        String year = req.getParameter("year");

        java.util.HashMap<String, String> results = null;
        try {
            results = net.trs.onthisday.backend.SongOnDay.getSoD(month, targetDay, year);
        } catch (Exception e) {
            resp.getWriter().println("Error getting the song information requested: \n" + e.getMessage());
        }

        String songName = results.get("songName");
        String songArtist = results.get("songArtist");

        resp.getWriter().println("Song: " + songName);
        resp.getWriter().println("Artist: " + songArtist);
    }
}
