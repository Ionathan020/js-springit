package com.jstronkhorst.springit.domain;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.Collection;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

}
