package com.fyp.vasclinicserver.model;

import com.fyp.vasclinicserver.model.audit.UserBaseEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Shift extends UserBaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    private Instant timeStart;
    private Instant timeEnd;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_user_id", referencedColumnName = "id")
    private User doctor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_board")
    private ShiftBoard shiftBoard;
    private boolean enabled;
}
