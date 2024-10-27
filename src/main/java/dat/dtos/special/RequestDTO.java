package dat.dtos.special;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestDTO {
    @JsonProperty("artistId")
    int artistId;
    @JsonProperty("albumId")
    int albumId;
    @JsonProperty("songId")
    int songId;
}
