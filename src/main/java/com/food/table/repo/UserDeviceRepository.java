package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.UserAccount;
import com.food.table.dto.UserDevice;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long>{

	UserDevice findByUseraccount(UserAccount userAccount);

	UserDevice findByDeviceToken(String deviceToken);

}
