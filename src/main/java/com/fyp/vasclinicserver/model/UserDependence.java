package com.fyp.vasclinicserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserDependence {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    @JoinColumn(name = "primary_user_id", referencedColumnName = "id")
    private User primaryUser;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "dep_user_id", referencedColumnName = "id")
    private Set<User> dependentUser;

}
