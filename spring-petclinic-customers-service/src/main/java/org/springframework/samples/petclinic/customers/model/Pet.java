/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.customers.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.springframework.core.style.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Maciej Szarlinski
 */
@Entity
@Data
@Table(name = "pets")
public class Pet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	@NotEmpty(message = "Name cannot be empty")
	private String name;

	@Column(name = "birth_date")
	@NotEmpty(message = "Birth date cannot be empty")
	@Temporal(TemporalType.DATE)
	private Date birthDate;

	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType type;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	@JsonIgnore
	private Owner owner;

}
