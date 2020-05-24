package com.food.table.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface FoodHistoryProjectionModel {
	public int getFoodId();

	public String getFoodName();

	public String getFoodImageUrl();

	public int getcount();

	@JsonIgnore
	public String getFoodTagList();

	default public String[] getFoodTags() {
		if (this.getFoodTagList() != null) {
			return getFoodTagList().split(",");
		}
		return null;
	}
}
