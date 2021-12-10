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

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.customers.applicaton.ThirdPartyServiceClient;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.model.OwnerRepository;
import org.springframework.samples.petclinic.global.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Maciej Szarlinski
 */
@RequestMapping("/owners")
@RestController
@Timed("petclinic.owner")
@RequiredArgsConstructor
@Slf4j
@Validated
class OwnerResource {

	private final OwnerRepository ownerRepository;

	private final ThirdPartyServiceClient thirdPartyServiceClient;

	/**
	 * Create Owner
	 */
	// @RequestMapping(value = "/createOwner", method = { RequestMethod.POST })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new Owner")
	public Owner createOwner(@Valid @RequestBody Owner owner) {
		log.info("Creating new owner {}", owner);
		return ownerRepository.save(owner);
	}

	/**
	 * Read single Owner
	 */
	@Operation(summary = "Get a Owner by its id")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "Found the Owner",
							content = { @Content(mediaType = "application/json",
									schema = @Schema(implementation = Owner.class)) }),
					@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
					@ApiResponse(responseCode = "404", description = "Owner not found", content = @Content) })
	@GetMapping(value = "/{ownerId}")
	public Optional<Owner> findOwner(@PathVariable("ownerId") @NotNull Integer ownerId) {
		Optional<Owner> owner = ownerRepository.findById(ownerId);
		log.info("Result owner {}", owner);
		String results = thirdPartyServiceClient.getExternalService();
		log.info("External call result = {} ", results);
		return owner;
	}

	/**
	 * Read List of Owners
	 */
	@GetMapping
	@Operation(summary = "Get all Owners")
	public List<Owner> findAll() {
		log.info("Getting all owners");
		return ownerRepository.findAll();
	}

	/**
	 * Update Owner
	 */
	@PutMapping(value = "/{ownerId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Update a Owner by its id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Owner not found"),
			@ApiResponse(responseCode = "405", description = "Validation exception") })
	public Owner updateOwner(@PathVariable("ownerId") Integer ownerId, @Valid @RequestBody Owner ownerRequest) {
		final Optional<Owner> owner = ownerRepository.findById(ownerId);

		final Owner ownerModel = owner
				.orElseThrow(() -> new ResourceNotFoundException("Owner " + ownerId + " not found"));
		// This is done by hand for simplicity purpose. In a real life use-case we should
		// consider using MapStruct.
		ownerModel.setFirstName(ownerRequest.getFirstName());
		ownerModel.setLastName(ownerRequest.getLastName());
		ownerModel.setCity(ownerRequest.getCity());
		ownerModel.setAddress(ownerRequest.getAddress());
		ownerModel.setTelephone(ownerRequest.getTelephone());
		log.info("Saving owner {}", ownerModel);
		return ownerRepository.save(ownerModel);
	}

}
