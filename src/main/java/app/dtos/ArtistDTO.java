package app.dtos;

import app.entities.Artist;
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
    String type;
    @JsonProperty("popularity")
    String popularity;
    @JsonProperty("genres")
    List<String> genres = new ArrayList<>();
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
    }

    public ArtistDTO(Integer artistId, String spotifyId, String name, String type, String popularity, String imageUrl) {
        this.artistId = artistId;
        this.spotifyId = spotifyId;
        this.name = name;
        this.type = type;
        this.popularity = popularity;
        this.imageUrl = imageUrl;
    }

    public void addInfoAndTrim(Artist artist) {
        this.albums = AlbumDTO.getTrimmedDTOList(artist.getAlbums());
        this.songs = SongDTO.getTrimmedDTOList(artist.getSongs());
        this.genres = Arrays.stream(artist.getGenresAsString().split("!")).toList();
    }

    public ArtistDTO trimDTO(){
        ArtistDTO artist = this;
        artist.getSongs().forEach(song -> {song.setArtist(null); song.getAlbum().setArtist(null); song.getAlbum().setSongs(null);});
        artist.getAlbums().forEach(album -> {album.setArtist(null); album.getArtist().setSongs(null);});
        return artist;
    }

    static public ArtistDTO getTrimmedDTO(ArtistDTO artist){
        artist.getSongs().forEach(song -> {song.setArtist(null); song.getAlbum().setArtist(null); song.getAlbum().setSongs(null);});
        artist.getAlbums().forEach(album -> {album.setArtist(null); album.getArtist().setSongs(null);});
        return artist;
    }

    static public ArtistDTO getTrimmedDTO(Artist artistE){
        ArtistDTO artist = new ArtistDTO(artistE);
        artist.addInfoAndTrim(artistE);
        artist.getSongs().forEach(song -> {song.setArtist(null); song.getAlbum().setArtist(null); song.getAlbum().setSongs(null);});
        artist.getAlbums().forEach(album -> {album.setArtist(null); album.getArtist().setSongs(null);});
        return artist;
    }

    static public List<ArtistDTO> trimDTOList(List<ArtistDTO> artists){
        return artists.stream().map(ArtistDTO::getTrimmedDTO).toList();
    }

    static public List<ArtistDTO> getArtistsAsDTOList(List<Artist> artists){
        return artists.stream().map(ArtistDTO::new).toList();
    }
}