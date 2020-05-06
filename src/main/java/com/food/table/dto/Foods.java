package com.food.table.dto;

import com.food.table.dto.constant.FoodStatusEnum;
import com.food.table.model.FoodsModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "foods")
@Getter
@Setter
public class Foods implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Restaurant restaurant;
    private String name;
    private String description;
    private String imageUrl;

    @ManyToOne(cascade = CascadeType.ALL)
    private Diets diets;

    @ManyToOne(cascade = CascadeType.ALL)
    private Cuisines cuisines;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "food_tag_map", joinColumns = {@JoinColumn(name = "food_id", referencedColumnName = "id")}
            , inverseJoinColumns = {@JoinColumn(name = "food_tag_id", referencedColumnName = "id")})
    private List<FoodTag> foodTags;

    @ManyToOne(cascade = CascadeType.ALL)
    private FoodCategory foodCategories;
    private double price;
    private String endTime;
    private String startTime;
    private int sortNo;
    private int status;
    private int deleteFlag;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "foods_id", referencedColumnName = "id")
    private List<FoodOptionMeta> extras;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
    private Timestamp deletionDate;
    private String deletedBy;

    public static Foods convertModelToDto(FoodsModel foodsModel, Diets diets,
                                          Cuisines cuisines, FoodCategory foodCategory, List<FoodOptionMeta> foodOptionMetaList,
                                          Restaurant restaurant, List<FoodTag> foodTags) {
        Foods foods = new Foods();
        if (foodsModel.getId() != 0) {
            foods.setId(foodsModel.getId());
        }
        foods.setName(foodsModel.getName());
        foods.setImageUrl(foodsModel.getImageUrl());
        foods.setPrice(foodsModel.getPrice());
        foods.setDiets(diets);
        foods.setCuisines(cuisines);
        foods.setFoodCategories(foodCategory);
        foods.setExtras(foodOptionMetaList);
        foods.setStartTime(foodsModel.getStartTime());
        foods.setEndTime(foodsModel.getEndTime());
        foods.setSortNo(foodsModel.getSortNo());
        foods.setDescription(foodsModel.getDescription());
        foods.setRestaurant(restaurant);
        foods.setFoodTags(foodTags);
        foods.setStatus(FoodStatusEnum.getValue(foodsModel.getStatus()));
        return foods;
    }


}
