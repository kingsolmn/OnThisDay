package net.trs.onthisday.backend.endpointapi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import net.trs.onthisday.backend.Month;
import net.trs.onthisday.backend.SongOnDay;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "soDApi",
        version = "v1",
        resource = "soD",
        namespace = @ApiNamespace(
                ownerDomain = "endpointapi.backend.onthisday.trs.net",
                ownerName = "endpointapi.backend.onthisday.trs.net",
                packagePath = ""
        )
)
public class SoDEndpoint {

    private static final Logger logger = Logger.getLogger(SoDEndpoint.class.getName());

    /**
     * This method gets the <code>SoD</code> object for the date given.
     *
     * @param monthIn The <code>Month</code> of the date.
     * @param targetDay The date of the date.
     * @param year The year of the date.
     *
     * @return The <code>SoD</code> for the date supplied.
     */
    @ApiMethod(name = "getSoD")
    public SoD getSoD(@Named("month") String monthIn, @Named("targetDay") String targetDay, @Named("year") String year) {
        logger.info("Calling getSoD method");
        HashMap<String, String> results = new HashMap<>();
        SoD sod = new SoD();
        Month month = null;
        try {
            month = Month.getMonth(monthIn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            results =  SongOnDay.getSoD(month, targetDay, year);
        } catch (Exception e) {
            e.printStackTrace();
        }


        sod.setSongName(results.get("songName"));
        sod.setArtName(results.get("songArtist"));

        return sod;
    }
}