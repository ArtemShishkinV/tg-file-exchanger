package com.shishkin.common.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "binary_content")
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@AllArgsConstructor
public class BinaryContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private byte[] arrayOfBytes;
}
