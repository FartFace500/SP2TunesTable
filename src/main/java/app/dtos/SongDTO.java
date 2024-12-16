package app.dtos;

import app.entities.Song;
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
    String type;
    //not fetching artist, adding the primary artist manually
    @JsonProperty("track_number")
    int songNumber;
    @JsonProperty("duration_ms")
    int durationMs;
    String releaseDate;
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
    }

    public SongDTO(Integer id, String songSearchId, String spotifyId, String name, String type, int songNumber, int durationMs, String releaseDate, String imageUrl) {
        this.songId = String.valueOf(id);
        this.songSearchId = songSearchId;
        this.spotifyId = spotifyId;
        this.name = name;
        this.type = type;
        this.songNumber = songNumber;
        this.durationMs = durationMs;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
    }

    public void addInfoAndTrim(Song song) {
        this.artist = ArtistDTO.getTrimmedDTO(song.getArtist());
        this.album = AlbumDTO.getTrimmedDTO(song.getAlbum());
    }

    public SongDTO trimDTO(){
        SongDTO song = this;
        song.getArtist().setSongs(null);
        song.getArtist().getAlbums().forEach(album -> {album.setSongs(null); album.getArtist().setSongs(null); album.getArtist().setAlbums(null);});
        song.getAlbum().setSongs(null);
        song.getAlbum().getArtist().setSongs(null);
        song.getAlbum().getArtist().setAlbums(null);
        return song;
    }

    static public SongDTO getTrimmedDTO(SongDTO song){
        song.getArtist().setSongs(null);
        song.getArtist().getAlbums().forEach(album -> {album.setSongs(null); album.getArtist().setSongs(null); album.getArtist().setAlbums(null);});
        song.getAlbum().setSongs(null);
        song.getAlbum().getArtist().setSongs(null);
        song.getAlbum().getArtist().setAlbums(null);
        return song;
    }

    static public SongDTO getTrimmedDTO(Song songE){
        SongDTO song = new SongDTO(songE);
        song.addInfoAndTrim(songE);
        song.getArtist().setSongs(null);
        song.getArtist().getAlbums().forEach(album -> {album.setSongs(null); album.getArtist().setSongs(null); album.getArtist().setAlbums(null);});
        song.getAlbum().setSongs(null);
        song.getAlbum().getArtist().setSongs(null);
        song.getAlbum().getArtist().setAlbums(null);
        return song;
    }

    static public List<SongDTO> trimDTOList(List<SongDTO> songs) {
        return songs.stream().map(SongDTO::getTrimmedDTO).toList();
    }

    static public List<SongDTO> getTrimmedDTOList(List<Song> songs) {
        return songs.stream().map(SongDTO::getTrimmedDTO).toList();
    }

    static public List<SongDTO> getSongsAsDTOList(List<Song> songs){
        return songs.stream().map(SongDTO::new).toList();
    }
}