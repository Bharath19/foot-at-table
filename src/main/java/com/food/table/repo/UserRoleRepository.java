package com.food.table.repo;

import com.food.table.dto.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    @Query(value = "SELECT * FROM user_account_roles u where u.role_name=:roleName", nativeQuery = true)
    UserRole findRoleByName(@Param("roleName") String roleName);

}
