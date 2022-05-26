package com.green.poolstatsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(	name = "matches")
public class Match {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Venue venue;

    @NotNull
    @ManyToOne
    private Format format;

    @NotNull
    @ManyToOne
    private GameType gameType;

    @NotNull
    @ManyToOne
    private TableType tableType;

    @NotNull
    @ManyToOne
    private RackType rackType;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;
}
