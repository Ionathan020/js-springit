package com.jstronkhorst.springit.vote;

import com.jstronkhorst.springit.config.Auditable;
import com.jstronkhorst.springit.user.User;
import com.jstronkhorst.springit.link.Link;
import lombok.*;

import jakarta.persistence.*;

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
