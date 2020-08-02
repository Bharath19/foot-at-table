package com.food.table.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String employeeId;

    private String name;

    private String deviceId;

    private String deviceName;

    private int age;

    private String gender;

    private String bloodGroup;

    private long phoneNo;

    private String email;

    private int status;

    private int deleteFlag;

    @ManyToMany(mappedBy = "restaurantEmployees")
    private List<RestaurantTable> restaurantTables;


    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserAccount user;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Restaurant restaurant;
}
