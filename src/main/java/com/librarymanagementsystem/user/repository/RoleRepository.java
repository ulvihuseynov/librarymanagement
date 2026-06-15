package com.librarymanagementsystem.user.repository;

import com.librarymanagementsystem.user.entity.AppRole;
import com.librarymanagementsystem.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByRoleName(AppRole roleName);

    boolean existsByRoleName(AppRole roleName);

}
