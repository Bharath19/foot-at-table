package com.food.table.dto;

import java.util.Date;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user_device")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDevice {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private UserAccount useraccount;
	
	private String deviceType;
	
	private String deviceToken;
	
	private String endPointArn;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;	

}
