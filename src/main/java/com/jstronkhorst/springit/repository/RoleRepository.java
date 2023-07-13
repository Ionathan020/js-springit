package com.jstronkhorst.springit.repository;

import com.jstronkhorst.springit.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
