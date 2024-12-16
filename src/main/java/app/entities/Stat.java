package app.entities;

import app.security.entities.User;
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
    private Integer id;

    @Column(name = "leaderboard_placement_artist")
    private Integer lp1;
    @Column(name = "leaderboard_placement_album")
    private Integer lp2;
    @Column(name = "leaderboard_placement_song")
    private Integer lp3;

    private Integer artistCorrectGuesses;
    private Integer albumCorrectGuesses;
    private Integer songCorrectGuesses;
    private Integer correctGuesses;

    private Integer artistAverageGuessAmount;
    private Integer albumAverageGuessAmount;
    private Integer songAverageGuessAmount;
    private Integer averageGuessAmount;

    private Integer artistRoundsPlayed;
    private Integer albumRoundsPlayed;
    private Integer songRoundsPlayed;
    private Integer roundsPlayed;

    @OneToOne
    private User user;

    public Stat(Integer lp1, Integer lp2, Integer lp3, Integer artistCorrectGuesses, Integer albumCorrectGuesses, Integer songCorrectGuesses, Integer artistAverageGuessAmount, Integer albumAverageGuessAmount, Integer songAverageGuessAmount) {}
}
