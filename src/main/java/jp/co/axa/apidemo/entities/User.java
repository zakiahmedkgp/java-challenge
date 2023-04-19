package jp.co.axa.apidemo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
@Table(	name = "users", 
	uniqueConstraints = { 
		@UniqueConstraint(columnNames = "username"),
	})
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max = 20)
	private String username;
	
	@NotBlank
	@Size(max = 120)
	private String password;

	public User(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 120) String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public User() {}
	
}
