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

    private String deletedBy;

    private Timestamp deletionDate;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
