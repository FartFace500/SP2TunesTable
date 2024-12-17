package app.dtos;

import app.entities.Album;
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
public class AlbumDTO {
    String albumId;
    String albumSearchId;
    @JsonProperty("id")
    String spotifyId;
    @JsonProperty("name")
    String name;
    @JsonProperty("type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String type;
    @JsonProperty("total_tracks")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    int totalSongs;
    @JsonProperty("release_date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String releaseDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String imageUrl;
    @ToString.Exclude
    @JsonInclude(JsonInclude.Include.NON_NULL)
    ArtistDTO artist;
    @ToString.Exclude
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<SongDTO> songs;

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
        addInfoAndTrim(album);
    }

    public AlbumDTO(Integer id, String albumSearchId, String spotifyId, String name) {
        this.albumId = String.valueOf(id);
        this.albumSearchId = albumSearchId;
        this.spotifyId = spotifyId;
        this.name = name;
    }

    public void addInfoAndTrim(Album album) {
        ArtistDTO artistDTO = new ArtistDTO(album.getArtist().getId(), album.getArtist().getSpotifyId(), album.getArtist().getName());
        artistDTO.setGenres(Arrays.stream(album.getArtist().getGenresAsString().split("!")).toList());
        this.artist = artistDTO;
        List<SongDTO> songDTOList = new ArrayList<>();
        for (int i = 0; i < album.getSongs().size(); i++) {
            songDTOList.add(new SongDTO(album.getSongs().get(i).getId(), album.getSongs().get(i).getSongSearchId(), album.getSongs().get(i).getSpotifyId(), album.getSongs().get(i).getName()));
        }
        this.songs = songDTOList;
    }

    @JsonGetter("id")
    public String getAlbumId() {
        return albumId;
    }

    @JsonGetter("search_id")
    public String getAlbumSearchId() {
        return albumSearchId;
    }

    @JsonGetter("spotify_id")
    public String getSpotifyId() {
        return spotifyId;
    }

    @JsonGetter("total_songs")
    public int getTotalSongs() {
        return totalSongs;
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