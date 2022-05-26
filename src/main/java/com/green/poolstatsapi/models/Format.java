package com.green.poolstatsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(	name = "formats")
public class Format {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;
}
