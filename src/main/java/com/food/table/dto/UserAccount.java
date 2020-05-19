package com.food.table.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "user_account", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class UserAccount implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 15)
    private String userId;

    @Size(max = 40)
    private String name;

    @Email
    private String email;

    @Size(max = 100)
    private String password;

    private Long phoneNo;

    private String refreshToken;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_link", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
            , inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<UserRole> roles;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
