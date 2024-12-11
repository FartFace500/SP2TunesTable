package app.dtos;

import app.entities.Song;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SongDTO {
    String songId;
    String songSearchId;
    @JsonProperty("id")
    String spotifyId;
    @JsonProperty("name")
    String name;
    @JsonProperty("type")
    String type;
    //not fetching artist, adding the primary artist manually
    @JsonProperty("track_number")
    int songNumber;
    String releaseDate;
    String imageUrl;
    String artistName;

    public SongDTO(Song song) {
        if (song.getId() != null) {
        this.songId = String.valueOf(song.getId());
        }
        this.songSearchId = song.getSongSearchId();
        this.name = song.getName();
        this.type = song.getType();
        this.songNumber = song.getSongNumber();
        this.releaseDate = song.getReleaseDate();
        this.imageUrl = song.getImageUrl();
        this.spotifyId = song.getSpotifyId();
        this.artistName = song.getArtistName();
    }
}