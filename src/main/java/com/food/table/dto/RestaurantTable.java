package com.food.table.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurant_table")
@Data
@Builder
@Where(clause = "delete_flag = 0")
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Restaurant restaurant;

    private String name;

    private String qrCode;

    private Integer seats;

    private int status;

    private int deleteFlag;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "table_employee_link", joinColumns = {@JoinColumn(name = "table_id", referencedColumnName = "id")}
            , inverseJoinColumns = {@JoinColumn(name = "employee_id", referencedColumnName = "id")})
    Set<RestaurantEmployee> restaurantEmployees;

    private String deletedBy;

    private Timestamp deletionDate;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
