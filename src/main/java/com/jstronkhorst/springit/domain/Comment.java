package com.jstronkhorst.springit.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@RequiredArgsConstructor
@Getter @Setter
@ToString
@NoArgsConstructor
public class Comment extends Auditable {

    @Id
    @GeneratedValue
    private long id;
    @NonNull
    private String body;

    @ManyToOne
    @NonNull
    private Link link;

}
