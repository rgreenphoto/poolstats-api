package com.green.poolstatsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(	name = "game_types")
public class GameType {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;
}
