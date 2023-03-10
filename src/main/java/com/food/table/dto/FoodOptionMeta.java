package com.food.table.dto;

import com.food.table.constant.FoodOptionType;
import com.food.table.constant.FoodStatusEnum;
import com.food.table.model.FoodOptionMetaModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "food_option_meta")
@Getter
@Setter
public class FoodOptionMeta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public int optionType;

    public String optionName;

    public String optionDescription;

    @Column(name = "foods_id")
    public Integer foodsId;

    public int status;

    @CreationTimestamp
    public Timestamp createdAt;

    @UpdateTimestamp
    public Timestamp updatedAt;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "food_options_map", joinColumns = {@JoinColumn(name = "food_option_meta_id", referencedColumnName = "id")}
            , inverseJoinColumns = {@JoinColumn(name = "food_options_id", referencedColumnName = "id")})
    List<FoodOptions> foodOptions;

    public static FoodOptionMeta convertModelToDto(FoodOptionMetaModel foodOptionMetaModel) {
        FoodOptionMeta foodOptionMeta = new FoodOptionMeta();
        foodOptionMeta.setOptionType(FoodOptionType.getValue(foodOptionMetaModel.getType()));
        foodOptionMeta.setOptionName(foodOptionMetaModel.getName());
        foodOptionMeta.setFoodOptions(foodOptionMetaModel.getFoodOptionsModels().stream().map(FoodOptions::convertModelToDto).collect(Collectors.toList()));
        foodOptionMeta.setOptionDescription(foodOptionMetaModel.getDescription());
        foodOptionMeta.setStatus(FoodStatusEnum.getValue(foodOptionMetaModel.getStatus()));
        return foodOptionMeta;
    }

}
