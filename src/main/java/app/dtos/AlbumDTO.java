package app.dtos;

import app.entities.Album;
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
    }

    public void addInfoAndTrim(Album album) {
        ArtistDTO artistDTO = new ArtistDTO(album.getArtist().getId(), album.getArtist().getSpotifyId(), album.getArtist().getName(), album.getArtist().getType(), album.getArtist().getPopularity(), album.getArtist().getImageUrl());
        artistDTO.setGenres(Arrays.stream(album.getArtist().getGenresAsString().split("!")).toList());
        this.artist = artistDTO;
        List<SongDTO> songDTOList = new ArrayList<>();
        for (int i = 0; i < album.getSongs().size(); i++) {
            songDTOList.add(new SongDTO(album.getSongs().get(i).getId(), album.getSongs().get(i).getSongSearchId(), album.getSongs().get(i).getSpotifyId(), album.getSongs().get(i).getName(), album.getSongs().get(i).getType(), album.getSongs().get(i).getSongNumber(), album.getSongs().get(i).getDurationMs(), album.getSongs().get(i).getReleaseDate(), album.getSongs().get(i).getImageUrl()));
        }
        this.songs = songDTOList;
    }

    public AlbumDTO trimDTO(){
        AlbumDTO album = this;
        album.getArtist().setAlbums(null);
        album.getArtist().setSongs(null);
        album.getSongs().forEach(song -> {song.setAlbum(null); song.setArtist(null);});
        return album;
    }

    static public AlbumDTO getTrimmedDTO(AlbumDTO album){
        album.getArtist().setAlbums(null);
        album.getArtist().setSongs(null);
        album.getSongs().forEach(song -> {song.setAlbum(null); song.setArtist(null);});
        return album;
    }

    static public AlbumDTO getTrimmedDTO(Album albumE){
        AlbumDTO album = new AlbumDTO(albumE);
        album.addInfoAndTrim(albumE);
        album.getArtist().setAlbums(null);
        album.getArtist().setSongs(null);
        album.getSongs().forEach(song -> {song.setAlbum(null); song.setArtist(null);});
        return album;
    }

    static public List<AlbumDTO> trimDTOList(List<AlbumDTO> albums){
        return albums.stream().map(AlbumDTO::getTrimmedDTO).toList();
    }

    static public List<AlbumDTO> getTrimmedDTOList(List<Album> albums){
        return albums.stream().map(AlbumDTO::getTrimmedDTO).toList();
    }

    static public List<AlbumDTO> getAlbumsAsDTOList(List<Album> albums){
        return albums.stream().map(AlbumDTO::new).toList();
    }
}
