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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Simple JavaBean domain object representing an owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Maciej Szarlinski
 */
@Entity
@Data
@Table(name = "owners")
public class Owner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Unique identifier of the Owner.", example = "1", required = true)
	private Integer id;

	@Column(name = "first_name")
	@NotEmpty(message = "{owner.firstName.required}")
	@Schema(description = "First name of the Owner.", example = "Jessica", required = true)
	private String firstName;

	@Column(name = "last_name")
	@NotEmpty(message = "owner.lastName.required")
	@Schema(description = "Last name of the Owner.", example = "Abigail", required = true)
	private String lastName;

	@Column(name = "address")
	@NotEmpty(message = "{owner.address.required}")
	@Size(max = 200, message = "{owner.address.limit}")
	@Schema(description = "Address of the Owner.", example = "88 Constantine Ave, #54", required = true)
	private String address;

	@Column(name = "city")
	@NotEmpty(message = "{owner.city.required}")
	@Schema(description = "City of the Owner.", example = "San Angeles", required = true)
	private String city;

	@Column(name = "telephone")
	@NotEmpty(message = "{owner.telephone.required}")
	@Digits(fraction = 0, integer = 10, message = "{owner.telephone.invalid}")
	@Size(min = 10, max = 10, message = "{owner.telephone.limit}")
	@Schema(description = "Telephone of the Owner.", example = "1234567890", required = true)
	private String telephone;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "owner")
	private Set<Pet> pets;

	protected Set<Pet> getPetsInternal() {
		if (this.pets == null) {
			this.pets = new HashSet<>();
		}
		return this.pets;
	}

	public List<Pet> getPets() {
		final List<Pet> sortedPets = new ArrayList<>(getPetsInternal());
		PropertyComparator.sort(sortedPets, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedPets);
	}

	public void addPet(Pet pet) {
		getPetsInternal().add(pet);
		pet.setOwner(this);
	}

}
