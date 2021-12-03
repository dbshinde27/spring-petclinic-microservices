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
package org.springframework.samples.petclinic.customers.web;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.customers.model.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Maciej Szarlinski
 */
@RestController
@Timed("petclinic.pet")
@RequiredArgsConstructor
@Slf4j
class PetResource {

	private final PetRepository petRepository;

	private final OwnerRepository ownerRepository;

	@GetMapping("/petTypes")
	public List<PetType> getPetTypes() {
		return petRepository.findPetTypes();
	}

	@PostMapping("/owners/{ownerId}/pets")
	@ResponseStatus(HttpStatus.CREATED)
	public Pet processCreationForm(@Valid @RequestBody PetRequest petRequest, @PathVariable("ownerId") int ownerId) {

		final Pet pet = new Pet();
		final Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);
		Owner owner = optionalOwner.orElseThrow(() -> new ResourceNotFoundException("Owner " + ownerId + " not found"));
		owner.addPet(pet);

		return save(pet, petRequest);
	}

	@PutMapping("/owners/*/pets/{petId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Update a Owner by its id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Pet not found"),
			@ApiResponse(responseCode = "405", description = "Validation exception") })
	public Pet processUpdateForm(@Valid @RequestBody PetRequest petRequest) {
		int petId = petRequest.getId();
		Pet pet = findPetById(petId);
		return save(pet, petRequest);
	}

	
	private Pet save(final Pet pet, final PetRequest petRequest) {

		pet.setName(petRequest.getName());
		pet.setBirthDate(petRequest.getBirthDate());

		petRepository.findPetTypeById(petRequest.getTypeId()).ifPresent(pet::setType);

		log.info("Saving pet {}", pet);
		return petRepository.save(pet);
	}

	@GetMapping("owners/*/pets/{petId}")
	@Operation(summary = "Get a Pet by its id")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "Found the Pet",
							content = { @Content(mediaType = "application/json",
									schema = @Schema(implementation = Pet.class)) }),
					@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
					@ApiResponse(responseCode = "404", description = "Pet not found", content = @Content) })
	public PetDetails findPet(@PathVariable("petId") int petId) {
		return new PetDetails(findPetById(petId));
	}

	private Pet findPetById(int petId) {
		Optional<Pet> pet = petRepository.findById(petId);
		if (!pet.isPresent()) {
			throw new ResourceNotFoundException("Pet " + petId + " not found");
		}
		log.info("Result pet {}", pet.get());
		return pet.get();
	}

}
