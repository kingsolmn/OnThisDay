/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package net.trs.onthisday.backend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;

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

        if (targetDay == null || year == null) {
            resp.getWriter().println("Please enter a date");
        }

        String url = "http://en.wikipedia.org/wiki/List_of_Billboard_Hot_100_number-one_singles_of_";

        resp.getWriter().println("Results for date: " + month + " " + targetDay + " " + year);

        Element target = null;
        try {
            target = getTargetCell(getPage(url + year), month, Integer.valueOf(targetDay), Integer.valueOf(year));
        } catch (NullPointerException e) {
            resp.getWriter().println("Target Cell Exception: " + e.getMessage());
        } catch (NumberFormatException e) {
            resp.getWriter().println("Target Cell Exception: " + e.getMessage());
        }

        String songName = "N/A";
        try {
            songName = getSongName(target);
        } catch (NullPointerException e) {
            songName = e.getMessage();
        }
        String songArtist = "N/A";
        try {
            songArtist = getSongArtist(target);
        } catch (Exception e) {
            songArtist = e.getMessage();
        }

        resp.getWriter().println("Song: " + songName);
        resp.getWriter().println("Artist: " + songArtist);
    }

    /*
     *
     */
    private String getSongName(Element songNameTD) throws NullPointerException{
        String sn = "N/A";

        if(songNameTD.nextElementSibling() != null){
            sn = songNameTD.text();
        }else{
            Element previousSibling = songNameTD.parent().previousElementSibling().child(1);
            return getSongName(previousSibling);
        }

        return sn;
    }

    /*
     *
     */
    private String getSongArtist(Element songArtistTD) throws NullPointerException{
        String sa = "N/A";

        if(songArtistTD.nextElementSibling() != null){
            sa = songArtistTD.nextElementSibling().text();
        }else{
            Element previousSibling = songArtistTD.parent().previousElementSibling().child(1);
            return getSongArtist(previousSibling);
        }

        return sa;
    }

    /* org.jsoup.select.Elements getPage(String url)
     *
     * Retrieves specified page for processing.
     * @param url The URL of the page to retrieve.
     * @return The XML <code>table</code> elements of the page for processing
     */
    private Elements getPage(String url) {
        Document doc = null;
        Elements res = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla").get();
        } catch (java.io.IOException e) {
            // TODO Need to complete error handling
        }

        try{
            res = doc.select("table");
        }catch(Exception e2){
            // TODO Need to complete error handling
        }
        return res;
    }

    /*
     *
     */
    private Element getTargetCell(Elements in, Month targetMonth, int targetDate, int year) throws NullPointerException{

        Element table = in.get(1);
        Iterator<Element> tr = table.select("tr").iterator();

        return findTarget(tr, targetMonth, targetDate, year);
    }

    private Element findTarget(Iterator<Element> tr, Month targetMonth, int targetDate, int year){

        Month curMonth;

        while(tr.hasNext()){
            Elements td = tr.next().select("td");

            if(td.size() > 0){
                try {
                    curMonth = Month.getMonth(td.get(0).text().split(" ")[0].toLowerCase());
                } catch (Exception e) {
                    // Not a month row
                    continue;
                }
            }else{
                continue;
            }

            if(curMonth == targetMonth){
                int curDate = Integer.valueOf(td.text().split(" ")[1]);
                int dateDiff = targetDate - curDate;

                if(dateDiff <= 0 && dateDiff >= -7){
                    return td.get(1);
                }else if((curMonth.length(Month.isLeapYear(year)) >= targetDate) && dateDiff <= 7) {
                    return td.get(1);
                }else{
                    continue;
                }
            }else{
                continue;
            }
        }
        return null;
    }
}
