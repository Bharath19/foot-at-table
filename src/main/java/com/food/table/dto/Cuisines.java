package com.food.table.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.food.table.model.CuisinesModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "cuisines")
public class Cuisines {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String description;	
	
	@ManyToMany(mappedBy = "cuisines")
	private List<Restaurant> restaurant;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;	
	
	public static Cuisines convertModelToDto(CuisinesModel cuisinesModel){
        Cuisines cuisines = new Cuisines();
        cuisines.setId(cuisinesModel.getId());
        cuisines.setName(cuisinesModel.getName());
        cuisines.setDescription(cuisinesModel.getDescription());
        return cuisines;
    }
}
