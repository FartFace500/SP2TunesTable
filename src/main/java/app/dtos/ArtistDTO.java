package app.dtos;

import app.entities.Artist;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
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

    public ArtistDTO(Artist artist) {
        if (artist.getId() != null) {
        this.artistId = artist.getId();
        }
        this.name = artist.getName();
        this.type = artist.getType();
        this.imageUrl = artist.getImageUrl();
        this.popularity = artist.getPopularity();
        this.spotifyId = artist.getSpotifyId();
//        this.genres = artist.getGenresAsJsonArray(); //TODO: fix this later
    }
}