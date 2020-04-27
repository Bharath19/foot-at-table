package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Address;

@Repository
public interface AddressRepository  extends JpaRepository<Address, Integer>{

}
