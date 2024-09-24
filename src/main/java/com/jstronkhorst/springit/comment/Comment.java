package com.jstronkhorst.springit.comment;

import com.jstronkhorst.springit.config.Auditable;
import com.jstronkhorst.springit.user.User;
import com.jstronkhorst.springit.link.Link;
import com.jstronkhorst.springit.config.BeanUtil;
import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
@RequiredArgsConstructor
@Getter @Setter
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

    @ManyToOne
    @NonNull
    private User user;

    public String getPrettyTime() {
        PrettyTime pt = BeanUtil.getBean(PrettyTime.class);
        return pt.format(convertToDateViaInstant(getCreatedDateTime()));
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
}
