package app.entities;

import app.dtos.SongDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "VARCHAR(255)", unique = true)
    private String songSearchId; //is given when an album is created, after an artist is persisted
    private String name;
    private String type;
    private int songNumber;
    private String releaseDate;
    private String imageUrl;
    private String spotifyId;
    @ManyToOne
    @ToString.Exclude
    private Album album;
    @ManyToOne
    @ToString.Exclude
    private Artist artist; //only used for singles

    public Song(SongDTO dto){
        if (dto.getSongId() != null) {
        this.id = Integer.valueOf(dto.getSongId());
        }
        this.songSearchId = dto.getSongSearchId();
        this.name = dto.getName();
        this.type = dto.getType();
        this.songNumber = dto.getSongNumber();
        this.releaseDate = dto.getReleaseDate();
        this.imageUrl = dto.getImageUrl();
        this.spotifyId = dto.getSpotifyId();
    }

    public void giveId(int existingSongs){ //do NOT run this if it already connected to an artist/album
        int number = existingSongs + 2; //the 2 makes up for counting 1 higher and makes sure 0 isn't used
        if (this.artist != null){
            this.songSearchId = artist.getId() + "-" + "0" + "-" + number;
        }
        if (this.album == null){
            this.songSearchId = "0" + "-" + "0" + "-" + number;
        }
        if (this.album.getArtist() == null){
            this.songSearchId = this.album.getAlbumSearchId() + "-" + number;
        }
    }
}
