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
        String month = req.getParameter("month").toLowerCase();
        String day = req.getParameter("day");
        String year = req.getParameter("year");

        resp.setContentType("text/plain");
        if (month == null || day == null || year == null) {
            resp.getWriter().println("Please enter a date");
        }

        String url = "http://en.wikipedia.org/wiki/List_of_Billboard_Hot_100_number-one_singles_of_" + year;
//        String url   = "http://www.gamquistu.com/stuff/charts/result";//params[0]; //SoD Service URI
        String song  = "Opps! No song info found for that date.";

        resp.getWriter().println("Date: " + month.substring(0, 1).toUpperCase() + month.substring(1) + " " + day + " " + year);
        resp.getWriter().println("Source: " + url);

        org.jsoup.nodes.Document doc = null;
        try {
            doc = org.jsoup.Jsoup.connect(url).userAgent("Mozilla").get();
        } catch (java.io.IOException e) {
            resp.getWriter().println("Doc Exception: " + e.getMessage());
        }

        org.jsoup.select.Elements results = null;
        try{
            results = doc.getElementsByTag("td");
//            results = doc.getElementsContainingText(month);
        }catch(Exception e2){
            resp.getWriter().println("Element Exception: " + e2.getMessage());
        }



        for(org.jsoup.nodes.Element td : results){
            if(td.text().toLowerCase().matches("^" + month + "\\s+\\d+")){
                resp.getWriter().println("Matched: " + td.text());
                String date = td.text().split(" ")[1];
                resp.getWriter().println("Date: " + month + " " + date);

                int targetDate = Integer.valueOf(day);
                int curDate   = Integer.valueOf(date);

                int dateDiff = targetDate - curDate;

                org.jsoup.nodes.Element songNameTD = null;
                String songName = "N/A";
                if(td.nextElementSibling() != null){
                    // Get Song name td ref
                    songNameTD = td.nextElementSibling();
                    songName = songNameTD.text();
                }

                org.jsoup.nodes.Element songArtistTD = null;
                String songArtist = "N/A";
                if(songNameTD.nextElementSibling() != null){
                    // Get Song Artist td ref
                    songArtistTD = songNameTD.nextElementSibling();
                    songArtist = songArtistTD.text();
                }

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


//        try{
//            song = results.text();
//        }catch(Exception e1){
//            resp.getWriter().println("Result Exception: " + e1.getMessage());
//        }

        resp.getWriter().println(song);
    }
}
