package com.nofaterock.restdocs.user.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author 한승룡
 * @since 2019-02-14
 */
@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;
	private String nickname;
	private LocalDateTime created;

	@Deprecated
	public User() {

	}

	public User(String name, String nickname) {
		this.name = name;
		this.nickname = nickname;
		this.created = LocalDateTime.now();
	}

}
