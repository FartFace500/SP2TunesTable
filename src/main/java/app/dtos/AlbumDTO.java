package app.dtos;

import app.entities.Album;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumDTO {
    String albumId;
    String albumSearchId;
    String imageUrl; // make custom deserializer that gets the first "url" in "images"'s list
    @JsonProperty("id")
    String spotifyId;
    @JsonProperty("name")
    String name;
    @JsonProperty("type")
    String type;
    @JsonProperty("total_tracks")
    int totalSongs;
    @JsonProperty("release_date")
    String releaseDate;
    @JsonProperty("artists")
    List<ArtistDTO> artists;
    @JsonProperty("tracks")
    TracksDTO tracks;

    public AlbumDTO(Album album) {
        if (album.getId() != null) {
        this.albumId = String.valueOf(album.getId());
        }
        this.albumSearchId = album.getAlbumSearchId();
        this.name = album.getName();
        this.type = album.getType();
        this.totalSongs = album.getTotalSongs();
        this.releaseDate = album.getReleaseDate();
        this.imageUrl = album.getImageUrl();
        this.spotifyId = album.getSpotifyId();
        if (album.getArtist() != null) {
        this.artists = List.of(new ArtistDTO(album.getArtist()));
        }
        if (album.getSongs() != null) {
        this.tracks = new TracksDTO(album.getSongs().stream().map(song -> new SongDTO(song)).toList());
        }
    }

}
