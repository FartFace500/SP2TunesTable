package app.dtos;

import app.entities.Song;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String type;
    @JsonProperty("track_number")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    int songNumber;
    @JsonProperty("duration_ms")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    int durationMs;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String releaseDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String imageUrl;
    @ToString.Exclude
    @JsonInclude(JsonInclude.Include.NON_NULL)
    ArtistDTO artist;
    @ToString.Exclude
    @JsonInclude(JsonInclude.Include.NON_NULL)
    AlbumDTO album;

    public SongDTO(Song song) {
        if (song.getId() != null) {
        this.songId = String.valueOf(song.getId());
        }
        this.songSearchId = song.getSongSearchId();
        this.name = song.getName();
        this.type = song.getType();
        this.songNumber = song.getSongNumber();
        this.durationMs = song.getDurationMs();
        this.releaseDate = song.getReleaseDate();
        this.imageUrl = song.getImageUrl();
        this.spotifyId = song.getSpotifyId();
        addInfoAndTrim(song);
    }

    public SongDTO(Integer id, String songSearchId, String spotifyId, String name) {
        this.songId = String.valueOf(id);
        this.songSearchId = songSearchId;
        this.spotifyId = spotifyId;
        this.name = name;
    }

    public void addInfoAndTrim(Song song) {
        ArtistDTO artistDTO = new ArtistDTO(song.getArtist().getId(), song.getArtist().getSpotifyId(), song.getArtist().getName());
        AlbumDTO albumDTO = new AlbumDTO(song.getAlbum().getId(), song.getAlbum().getAlbumSearchId(), song.getAlbum().getSpotifyId(), song.getAlbum().getName());
        this.artist = artistDTO;
        this.album = albumDTO;
    }

    @JsonGetter("id")
    public String getSongId() {
        return songId;
    }

    @JsonGetter("search_id")
    public String getSongSearchId() {
        return songSearchId;
    }

    @JsonGetter("spotify_id")
    public String getSpotifyId() {
        return spotifyId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @JsonGetter("song_number")
    public int getSongNumber() {
        return songNumber;
    }

    @JsonGetter("duration_ms")
    public int getDurationMs() {
        return durationMs;
    }

    @JsonGetter("release_date")
    public String getReleaseDate() {
        return releaseDate;
    }

    @JsonGetter("image_url")
    public String getImageUrl() {
        return imageUrl;
    }
}