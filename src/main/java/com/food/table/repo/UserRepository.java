package com.food.table.repo;

import com.food.table.dto.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Integer> {

    @Query(value = "SELECT * FROM user_account u where u.email=:email", nativeQuery = true)
    UserAccount findUserByEmailId(@Param("email") String email);

    @Query(value = "SELECT * FROM user_account u where u.phone_no=:phone_no", nativeQuery = true)
    UserAccount findUserByPhoneNo(@Param("phone_no") long phoneNo);

    @Query(value = "SELECT * FROM user_account u where u.refresh_token=:refresh_token", nativeQuery = true)
    UserAccount findUserByRefreshToken(@Param("refresh_token") String refreshToken);

}
