package com.example.database1;

public class Artist {
    private String artistId;
    private String songName;
    private String singerName;

    public Artist(String artistId, String songName, String singerName) {
        this.artistId = artistId;
        this.songName = songName;
        this.singerName = singerName;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getSongName() {
        return songName;
    }

    public String getSingerName() {
        return singerName;
    }
}
