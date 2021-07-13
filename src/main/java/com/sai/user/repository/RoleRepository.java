package com.sai.user.repository;

import java.util.Optional;

import com.sai.user.domain.Role;
import com.sai.user.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	Optional<Role> findByRole(RoleEnum role);

}
