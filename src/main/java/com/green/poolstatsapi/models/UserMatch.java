package com.green.poolstatsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(	name = "user_matches")
public class UserMatch {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private Match match;

    @Column(nullable = true)
    private Integer formatSkillLevel;

    @Column(nullable = true)
    private Integer fargoRating;

    @ColumnDefault("0")
    private Integer otherRating;

    @Column(nullable = true)
    private Integer race;

    @ColumnDefault("0")
    private Integer matchPoints;

    @ColumnDefault("0")
    private Integer matchScore;

    @ColumnDefault("0")
    private Integer breakRuns;

    @ColumnDefault("0")
    private Integer tableRuns;

    @ColumnDefault("0")
    private Integer onSnaps;

    @ColumnDefault("0")
    private Integer rackless;

    @ColumnDefault("0")
    private Integer innings;

    @ColumnDefault("0")
    private Integer homeTeam;

    @ColumnDefault("0")
    private Integer isWin;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

}
