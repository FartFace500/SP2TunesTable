package app.entities;

import app.dtos.AlbumDTO;
import app.dtos.SongDTO;
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
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(columnDefinition = "VARCHAR(255)", unique = true)
    private String albumSearchId; //is given right after Artist is persisted
    private String name;
    private String type;
    private int totalSongs;
    private String releaseDate;
    private String imageUrl;
    @Column(name = "spotifyId")
    private String spotifyId;
    @ManyToOne
    @ToString.Exclude
    private Artist artist;
    @OneToMany
    @Cascade(CascadeType.PERSIST)
    private List<Song> songs = new ArrayList<>();

    public Album(AlbumDTO dto) {
        if (dto.getAlbumId() != null) {
        this.id = Integer.valueOf(dto.getAlbumId());
        }
        this.albumSearchId = dto.getAlbumSearchId();
        this.name = dto.getName();
        this.type = dto.getType();
        this.totalSongs = dto.getTotalSongs();
        this.releaseDate = dto.getReleaseDate();
        this.imageUrl = dto.getImageUrl();
        this.spotifyId = dto.getSpotifyId();
        if (dto.getArtists() != null){
        this.artist = dto.getArtists().stream().map(artist -> new Artist(artist)).toList().get(0);
        }
        if (dto.getTracks() != null){ //problem starts down here
            if (dto.getTracks().getSongs() != null) {
                this.songs = dto.getTracks().getSongs().stream().map(song -> new Song(song)).toList();
            }
        }
    }

    public void addSongsAsDTO(List<SongDTO> songsDTO) { //this is used by Populate
        if (this.songs.isEmpty() && !songsDTO.isEmpty()) {
            for (int i = 0; i < totalSongs; i++) {
                Song song = new Song(songsDTO.get(i));
                String id = this.albumSearchId + "-" + (i +1 );
                song.setSongSearchId(id);
                song.setAlbum(this);
                this.songs.add(song);
            }
        }
    }

    public void addSongs(List<Song> songsS) {
        if (this.songs.isEmpty() && !songsS.isEmpty()) {
            for (Song song : songsS) {
                String id;
                if (song.getSongNumber() != 0){
                    id = this.albumSearchId + "-" + song.getSongNumber();
                } else {
                    id = this.albumSearchId + "-" + this.songs.size()+1;
                }
                song.setSongSearchId(id);
                song.setAlbum(this);
                this.songs.add(song);
            }
                this.totalSongs = this.songs.size();
        }
    }

    public void updateSongs(){
        if (!this.songs.isEmpty()){
            for (int i = 0; i < totalSongs; i++) {
                songs.get(i).setSongSearchId(this.albumSearchId + "-" + (i + 1));
            }
        }
    }

    public void giveId(int existingAlbums){ //do NOT run this if it already connected to an artist
        if (this.artist == null) {
            this.albumSearchId = "0" + "-" + (existingAlbums + 2); //the 2 makes up for counting 1 higher and makes sure 0 isn't used
        }
    }

}
