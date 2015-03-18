package net.trs.onthisday.backend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

/**
 * Created by Steve Palacios on 3/18/15.
 */
public class SongOnDay {

    /**
     * Retrieves the Song On Day.
     * @param month The {@code net.trs.onthisday.Month} requested.
     * @param targetDay The date of the month requested.
     * @param year The year requested.
     * @return {@code java.util.HashMap<String, String>} map of the resulting song info.
     * @throws Exception Throws {@code InvalidInputException}, {@code Exception} with details of error.
     */
    public static HashMap<String, String> getSoD(Month month, String targetDay, String year) throws Exception{
        HashMap<String, String> results = new HashMap<>();

        if (targetDay == null || year == null) {
            throw new InvalidInputException("Please enter a valid date.");
        }

        // TODO Move this out to an XML resource file
        String url = "http://en.wikipedia.org/wiki/List_of_Billboard_Hot_100_number-one_singles_of_";

        Element target;
        try {
            target = getTargetCell(getPage(url + year), month, Integer.valueOf(targetDay), Integer.valueOf(year));
        } catch (NullPointerException e) {
            throw new Exception("Unable to find the target cell.");
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Please enter a valid date.");
        }

        String songName;
        try {
            songName = getSongName(target);
        } catch (Exception e) {
            songName = e.getMessage();
        }
        String songArtist;
        try {
            songArtist = getSongArtist(target);
        } catch (Exception e) {
            songArtist = e.getMessage();
        }

        results.put("songArtist", songArtist);
        results.put("songName", songName);

        return results;
    }



    /*
     *
     */
    private static String getSongName(Element songNameTD) throws NullPointerException{
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
    private static String getSongArtist(Element songArtistTD) throws NullPointerException{
        String sa = "N/A";

        if(songArtistTD.nextElementSibling() != null){
            sa = songArtistTD.nextElementSibling().text();
        }else{
            Element previousSibling = songArtistTD.parent().previousElementSibling().child(1);
            return getSongArtist(previousSibling);
        }

        return sa;
    }

    private static Elements getPage(String url) throws Exception{
        Document doc = null;
        Elements res = null;

        doc = Jsoup.connect(url).userAgent("Mozilla").get();

        res = doc.select("table");

        return res;
    }

    private static Element getTargetCell(Elements in, Month targetMonth, int targetDate, int year) throws NullPointerException{

        Element table = in.get(1);
        java.util.Iterator<Element> tr = table.select("tr").iterator();

        return findTarget(tr, targetMonth, targetDate, year);
    }

    private static Element findTarget(java.util.Iterator<Element> tr, Month targetMonth, int targetDate, int year){

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
