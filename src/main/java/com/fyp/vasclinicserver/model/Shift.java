package com.fyp.vasclinicserver.model;

import com.fyp.vasclinicserver.model.audit.UserBaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Shift extends UserBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant start;
    private Instant end;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_user_id", referencedColumnName = "id")
    private User doctor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_board_id", referencedColumnName = "id")
    private ShiftBoard shiftBoard;
    private boolean enabled;
}
