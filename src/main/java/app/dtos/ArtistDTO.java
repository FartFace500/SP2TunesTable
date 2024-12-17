package app.dtos;

import app.entities.Artist;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistDTO {
    Integer artistId;
    @JsonProperty("id")
    String spotifyId;
    @JsonProperty("name")
    String name;
    @JsonProperty("type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String type;
    @JsonProperty("popularity")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String popularity;
    @JsonProperty("genres")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<String> genres = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String imageUrl;
    @ToString.Exclude
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<AlbumDTO> albums = new ArrayList<>();
    @ToString.Exclude
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<SongDTO> songs = new ArrayList<>();

    public ArtistDTO(Artist artist) {
        if (artist.getId() != null) {
        this.artistId = artist.getId();
        }
        this.name = artist.getName();
        this.type = artist.getType();
        this.imageUrl = artist.getImageUrl();
        this.popularity = artist.getPopularity();
        this.spotifyId = artist.getSpotifyId();
        addInfoAndTrim(artist);
    }

    public ArtistDTO(Integer artistId, String spotifyId, String name) {
        this.artistId = artistId;
        this.spotifyId = spotifyId;
        this.name = name;
    }

    public void addInfoAndTrim(Artist artist) {
        List<AlbumDTO> albumDTOS = new ArrayList<>();
        for (int i = 0; i < artist.getAlbums().size(); i++) {
            AlbumDTO albumDTO = new AlbumDTO(artist.getAlbums().get(i).getId(), artist.getAlbums().get(i).getAlbumSearchId(), artist.getAlbums().get(i).getSpotifyId(), artist.getAlbums().get(i).getName());
            albumDTOS.add(albumDTO);
        }
        List<SongDTO> songDTOs = new ArrayList<>();
        for (int i = 0; i < artist.getSongs().size(); i++) {
            SongDTO songDTO = new SongDTO(artist.getSongs().get(i).getId(), artist.getSongs().get(i).getSongSearchId(), artist.getSongs().get(i).getSpotifyId(), artist.getSongs().get(i).getName());
            songDTOs.add(songDTO);
        }
        this.albums = albumDTOS;
        this.songs = songDTOs;
        this.genres = Arrays.stream(artist.getGenresAsString().split("!")).toList();
    }

    @JsonGetter("id")
    public Integer getArtistId() {
        return artistId;
    }

    @JsonGetter("spotify_id")
    public String getSpotifyId() {
        return spotifyId;
    }

    @JsonGetter("image_url")
    public String getImageUrl() {
        return imageUrl;
    }
}