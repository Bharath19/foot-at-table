package com.food.table.dto;

import com.food.table.dto.constant.FoodOptionType;
import com.food.table.dto.constant.FoodStatusEnum;
import com.food.table.model.FoodOptionMetaModel;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "food_option_meta")
@Data
public class FoodOptionMeta {

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

    public static FoodOptionMeta convertModelToDto(FoodOptionMetaModel foodOptionMetaModel, List<FoodOptions> foodOptionsList) {
        FoodOptionMeta foodOptionMeta = new FoodOptionMeta();
        foodOptionMeta.setOptionType(FoodOptionType.getValue(foodOptionMetaModel.getType()));
        foodOptionMeta.setOptionName(foodOptionMetaModel.getName());
        foodOptionMeta.setFoodOptions(foodOptionsList);
        foodOptionMeta.setOptionDescription(foodOptionMetaModel.getDescription());
        foodOptionMeta.setStatus(FoodStatusEnum.getValue(foodOptionMetaModel.getStatus()));
        return foodOptionMeta;
    }

}
