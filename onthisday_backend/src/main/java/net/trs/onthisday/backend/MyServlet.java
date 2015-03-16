/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

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

        org.jsoup.select.Elements results;
        net.trs.onthisday.backend.Month month = JANUARY;

        try {
            month = Month.getMonth(req.getParameter("month"));
        } catch (Exception e) {
            resp.getWriter().println("A valid and properly spelled month name must be supplied");
        }
        String targetDay = req.getParameter("day");
        String year = req.getParameter("year");

        if (targetDay == null || year == null) {
            resp.getWriter().println("Please enter a date");
        }

        String url = "http://en.wikipedia.org/wiki/List_of_Billboard_Hot_100_number-one_singles_of_";
//        String url   = "http://www.gamquistu.com/stuff/charts/result";//params[0]; //SoD Service URI
        String song  = "Opps! No song info found for that date.";

        resp.getWriter().println("Date: " + month + " " + targetDay + " " + year);
        resp.getWriter().println("Source: " + url + year);

        results = getPage(url + year);

        getTargetCell(getPage(url + year), month.toString(), Integer.valueOf(targetDay));

        for(org.jsoup.nodes.Element td : results){
            if(td.text().toLowerCase().matches("^" + month.toString() + "\\s+\\d+")){
                String date = td.text().split(" ")[1];
                resp.getWriter().println("Target Date: " + month.toString() + " " + targetDay);
                resp.getWriter().println("Matched: " + td.text());

                int targetDate = Integer.valueOf(targetDay);
                int curDate   = Integer.valueOf(date);

                int dateDiff = targetDate - curDate;
                resp.getWriter().println("dateDiff: " + dateDiff);

                if(dateDiff < 0 && dateDiff > -7){
                    // Over shot by lees than a week
                    resp.getWriter().println("Overshot by less than a week");
                    if(month.toString().toLowerCase().contains("january")){
                        resp.getWriter().println("Need to go back to last year");
                    }else{
                        resp.getWriter().println("Need to go back to last month");
                    }
                }else if(dateDiff == 0){
                    // on the nose
                    resp.getWriter().println("On the nose");
                }else if(dateDiff > 0 && dateDiff < 7){
                    // undershot by less than a week
                    resp.getWriter().println("Undershot by less than a week");
                }else{
                    // more than a week off
                    resp.getWriter().println("More than a week off");
                }

                String songName = getSongName(td.nextElementSibling());
                String songArtist = getSongArtist(td.nextElementSibling().nextElementSibling());

                resp.getWriter().println(" ---- ");
//                resp.getWriter().println("This sibling: " + songNameTD);
//                resp.getWriter().println("Next Sibling: " + songArtistTD);
//                resp.getWriter().println("songName: " + songName);
//                resp.getWriter().println("songArtist: " + songArtist);
                resp.getWriter().println("Song Name: " + songName + "\nSong Artist: " + songArtist);
                resp.getWriter().println(" ---- ");
                resp.getWriter().println("\n");
            }
        }

        resp.getWriter().println(song);
    }

    /*
     *
     */
    private String getSongName(org.jsoup.nodes.Element songNameTD){
        String sn = "N/A";

        if(songNameTD.nextElementSibling() != null){
            sn = songNameTD.text();
        }

        return sn;
    }

    /*
     *
     */
    private String getSongArtist(org.jsoup.nodes.Element songArtistTD){
        String sa = "N/A";

        if(songArtistTD != null){
            sa = songArtistTD.text();
        }

        return sa;
    }

    /* org.jsoup.select.Elements getPage(String url)
     *
     * Retrieves specified page for processing.
     * @param url The URL of the page to retrieve.
     * @return The XML <code>td</code> elements of the page for processing
     */
    private org.jsoup.select.Elements getPage(String url) {
        org.jsoup.nodes.Document doc = null;
        org.jsoup.select.Elements res = null;
        try {
            doc = org.jsoup.Jsoup.connect(url).userAgent("Mozilla").get();
        } catch (java.io.IOException e) {
            // TODO Need to complete error reporting/handling
        }

        try{
            res = doc.getElementsByTag("td");
        }catch(Exception e2){
            // TODO Need to complete error reporting/handling
        }
        return res;
    }

    /*
     *
     */
    private org.jsoup.select.Elements getTargetCell(org.jsoup.select.Elements td, String targetMonth, int targetDate){

        for(org.jsoup.nodes.Element target : td){
            if(target.text().toLowerCase().matches("^" + targetMonth + "\\s+\\d")){
                int curDate = Integer.valueOf(td.text().split(" ")[1]);
                int dateDiff = targetDate - curDate;



                if(dateDiff <= 0 && dateDiff >= -7){
                    // Where we need to be
                }else if(dateDiff > 0 && dateDiff < 7){
                    // Need to go back a week
                }
            }
        }

        return null;
    }
}
