package com.jstronkhorst.springit.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "LINK_ID"})})
public class Vote extends Auditable {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private Short direction;

    @NonNull
    @ManyToOne
    private User user;

    @NonNull
    @ManyToOne
    private Link link;
}
