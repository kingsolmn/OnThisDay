package net.trs.onthisday.backend.endpointapi;

/**
 * Created by steve on 3/31/15.
 */
public class SoD {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    private Long id;
    private String artName;
    private String songName;
}
