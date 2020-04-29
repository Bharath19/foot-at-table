package com.food.table.dto;

import com.food.table.model.FoodTagModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "food_tag")
public class FoodTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    @ManyToMany(mappedBy = "foodTags")
    private List<Foods> foods;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public static FoodTag convertModelToDto(FoodTagModel foodTagModel) {
        FoodTag foodTag = new FoodTag();
        foodTag.setName(foodTagModel.getName());
        foodTag.setDescription(foodTagModel.getDescription());
        return foodTag;
    }
}

