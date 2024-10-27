package dat.dtos;

import dat.entities.Song;
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
    @JsonProperty("name")
    String name;
    @JsonProperty("type")
    String type;
    //not fetching artist, adding the primary artist manually
    @JsonProperty("track_number")
    int songNumber;
//    AlbumDTO album;
//    ArtistDTO artist;

    public SongDTO(Song song) {
        if (song.getId() != null) {
        this.songId = String.valueOf(song.getId());
        }
        this.songSearchId = song.getSongSearchId();
        this.name = song.getName();
        this.type = song.getType();
        this.songNumber = song.getSongNumber();
//        this.album = new AlbumDTO(song.getAlbum());
//        this.artist = new ArtistDTO(song.getArtist());
    }
}