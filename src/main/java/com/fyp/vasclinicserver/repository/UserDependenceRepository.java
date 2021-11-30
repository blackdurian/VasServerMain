package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.UserDependence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDependenceRepository extends JpaRepository<UserDependence,String> {

}
