package com.shishkin.common.jpa.entity;

import com.shishkin.common.jpa.entity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramId;
    @CreationTimestamp
    private LocalDateTime registerDate;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
}
