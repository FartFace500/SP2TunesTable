package app.entities;

import app.config.HibernateConfig;
import app.dtos.AlbumDTO;
import app.dtos.ArtistDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String type;
    private String popularity;
    private String imageUrl;
    private String spotifyId;
    private String genresAsString;
    @OneToMany(mappedBy = "artist", fetch = FetchType.EAGER)
    @Cascade(CascadeType.PERSIST)
    private List<Album> albums = new ArrayList<>();
    @OneToMany(mappedBy = "artist", fetch = FetchType.EAGER)
    @Cascade(CascadeType.PERSIST) //TODO: possibly change this
    private List<Song> songs = new ArrayList<>(); //only used for singles

    public Artist(ArtistDTO dto){
        if (dto.getArtistId() != null) {
        this.id = dto.getArtistId();
        }
        this.name = dto.getName();
        this.type = dto.getType();
        this.popularity = dto.getPopularity();
        this.imageUrl = dto.getImageUrl();
        this.spotifyId = dto.getSpotifyId();
        this.genresAsString = String.join("!",dto.getGenres());
    }

    public void addSongs(List<Song> songs){
        for (Song song : songs) {
        if (!this.songs.contains(song)) {
            song.setSongSearchId(this.id + "-" + "0" + "-" + (this.songs.size() + 1));
            song.setArtist(this);
            this.songs.add(song);
        }
        }
    }

    public void addAlbumAsDTO(AlbumDTO albumDTO, int id){ //this is used by Populate
        Album album = new Album(albumDTO);
        if (!albums.contains(album)){
            album.setAlbumSearchId(this.id + "-" + String.valueOf(id));
            album.addSongsAsDTO(albumDTO.getSongs());
            album.setArtist(this);
            album.updateSongs();
            this.albums.add(album);
        }
    }

    public void addAlbum(Album album){
        if (!albums.contains(album)){
            album.setAlbumSearchId(this.id + "-" + (this.getAlbums().size() + 1));
            album.setArtist(this);
            album.updateSongs();
            this.albums.add(album);
        }
    }

    public void transferSinglesToAlbum(String albumId, String songId){
        for (Album album : albums) {
            if (album.getAlbumSearchId().equals(albumId)) {
                for (Song song : songs) {
                    if (song.getSongSearchId().equals(songId)) {
                        if (!album.getSongs().contains(song)) {
                            try(var em = HibernateConfig.getEntityManagerFactory().createEntityManager()){
                                Album foundAlbum = em.find(Album.class, albumId);
                                List<Song> singles = List.of(song);
                                foundAlbum.addSongs(singles);
                                this.songs.remove(song);
                            }
                        }
                    }
                }
            }
        }
    }

    public void transferFromAlbumToSingle(String albumId, String songId){
        //might be able to copy a lot from the method above
    }

}
