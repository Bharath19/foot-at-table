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

import com.food.table.model.SearchTagModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "search_type")
public class SearchType {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String description;	
	
	@ManyToMany(mappedBy = "searchType")
	private List<Restaurant> restaurant;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	public static SearchType convertModelToDto(SearchTagModel searchTagModel){
		SearchType searchTag = new SearchType();
        searchTag.setId(searchTagModel.getId());
        searchTag.setName(searchTagModel.getName());
        searchTag.setDescription(searchTagModel.getDescription());
        return searchTag;
    }
}
