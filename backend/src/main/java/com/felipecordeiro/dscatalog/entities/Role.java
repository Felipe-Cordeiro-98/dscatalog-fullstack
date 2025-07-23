package com.felipecordeiro.dscatalog.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;
}
