package app.entities;

import app.security.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer id;

    @Column(name = "artist_leaderboard_placement")
    private Integer artistLeaderboardPlacement;
    @Column(name = "album_leaderboard_placement")
    private Integer albumLeaderboardPlacement;
    @Column(name = "song_leaderboard_placement")
    private Integer songLeaderboardPlacement;

    @Column(name = "artist_correct_guesses")
    private Integer artistCorrectGuesses;
    @Column(name = "album_correct_guesses")
    private Integer albumCorrectGuesses;
    @Column(name = "song_correct_guesses")
    private Integer songCorrectGuesses;
    @Column(name = "total_correct_guesses")
    private Integer correctGuesses;

    @Column(name = "artist_average_guess_amount")
    private Double artistAverageGuessAmount;
    @Column(name = "album_average_guess_amount")
    private Double albumAverageGuessAmount;
    @Column(name = "song_average_guess_amount")
    private Double songAverageGuessAmount;
    @Column(name = "total_average_guess_amount")
    private Double averageGuessAmount;

    @Column(name = "artist_total_guesses")
    private Integer artistTotalGuesses;
    @Column(name = "album_total_guesses")
    private Integer albumTotalGuesses;
    @Column(name = "song_total_guesses")
    private Integer songTotalGuesses;
    @Column(name = "total_guesses")
    private Integer totalGuesses;

    @Column(name = "artist_rounds_played")
    private Integer artistRoundsPlayed;
    @Column(name = "album_rounds_played")
    private Integer albumRoundsPlayed;
    @Column(name = "song_rounds_played")
    private Integer songRoundsPlayed;
    @Column(name = "total_rounds_played")
    private Integer roundsPlayed;

    @OneToOne
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ToString.Exclude
    private User user;
}
