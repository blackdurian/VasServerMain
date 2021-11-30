package com.fyp.vasclinicserver.model;

import com.fyp.vasclinicserver.model.audit.UserBaseEntity;
import com.fyp.vasclinicserver.model.enums.ShiftBoardStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ShiftBoard extends UserBaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id",
            updatable = false,
            nullable = false
    )
    private String id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "clinic_id", referencedColumnName = "id")
    private Clinic clinic;

    @OneToMany(mappedBy = "shiftBoard",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Shift> shiftList;

    @Enumerated(EnumType.STRING)
    private ShiftBoardStatus status;

}
