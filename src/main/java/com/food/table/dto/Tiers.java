package com.food.table.dto;

import com.food.table.model.TiersModel;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="tiers")
@Data
public class Tiers {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    public static Tiers convertModelToDto(TiersModel tiersModel){
        Tiers tiers = new Tiers();
        tiers.setId(tiersModel.getId());
        tiers.setName(tiersModel.getName());
        tiers.setDescription(tiersModel.getDescription());
        return tiers;
    }

}
