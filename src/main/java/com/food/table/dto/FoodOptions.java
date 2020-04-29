package com.food.table.dto;

import com.food.table.dto.constant.FoodStatusEnum;
import com.food.table.model.FoodOptionsModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "food_options")
@Data
public class FoodOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String name;
    public String description;
    public String imageUrl;
    public int status;
    public int sortNo;
    @CreationTimestamp
    public Timestamp createdAt;
    @UpdateTimestamp
    public Timestamp updatedAt;

    @ManyToMany(mappedBy = "foodOptions")
    private List<FoodOptionMeta> foodsOptionMeta;

    public static FoodOptions convertModelToDto(FoodOptionsModel foodOptionsModel) {
        FoodOptions foodOptions = new FoodOptions();
        foodOptions.setName(foodOptionsModel.getName());
        foodOptions.setDescription(foodOptionsModel.getDescription());
        foodOptions.setImageUrl(foodOptionsModel.getImageUrl());
        foodOptions.setSortNo(foodOptionsModel.getSortNo());
        foodOptions.setStatus(FoodStatusEnum.getValue(foodOptionsModel.getStatus()));
        return foodOptions;
    }
}
