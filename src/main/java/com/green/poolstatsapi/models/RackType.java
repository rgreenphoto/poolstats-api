package com.green.poolstatsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(	name = "rack_types")
public class RackType {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;
}
