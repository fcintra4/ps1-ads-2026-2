package br.edu.fatecfranca.api.controllers;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecfranca.api.controllers.dtos.UserDto;
import br.edu.fatecfranca.api.entities.Users;
import br.edu.fatecfranca.api.services.UsersService;
import br.edu.fatecfranca.api.services.policy.contracts.Contract;
import br.edu.fatecfranca.api.services.policy.contracts.PageMeta;
import jakarta.validation.Valid;

@RestController @RequestMapping("/users")
public class UserController {

	private final UsersService service;

	public UserController(UsersService service) { this.service = service; }

	private boolean exists(Long id) { return this.service.existsById(id); }

	@PostMapping
	public ResponseEntity<Contract<Long>> create(@Valid @RequestBody UserDto userDto, BindingResult br) {
		if (br.hasErrors()) {
			String msg = br.getAllErrors()
				.stream()
				.map(error -> error.getDefaultMessage() == null ? error.toString() : error.getDefaultMessage())
				.collect(Collectors.joining("; "));
			Contract<Long> body = Contract.badRequest("BAD_REQUEST", msg);
			return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
		}
		Contract<Long> body = Contract.created(this.service.create(userDto.toEntity()).getId());
		return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Contract<UserDto>> update(
			@PathVariable Long id,
			@Valid @RequestBody UserDto userDto,
			BindingResult br
	) {
		if (!exists(id)) {
			Contract<UserDto> body = Contract.notFound("USER_NOT_FOUND", "User not found");
			return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
		}
		if (br.hasErrors()) {
			String msg = br.getAllErrors()
				.stream()
				.map(error -> error.getDefaultMessage() == null ? error.toString() : error.getDefaultMessage())
				.collect(Collectors.joining("; "));
			Contract<UserDto> body = Contract.badRequest("BAD_REQUEST", msg);
			return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
		}

		Users user = userDto.toEntity();
		user.setId(id);
		this.service.findById(id).ifPresent(existing -> user.setIsAdmin(existing.getIsAdmin()));
		Contract<UserDto> body = Contract.ok(new UserDto(this.service.update(user)));
		return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
	}

	@GetMapping
	public ResponseEntity<Contract<Iterable<UserDto>>> getAll(Pageable pageable) {
		Page<UserDto> page = this.service.findAll(pageable).map(UserDto::new);
		Contract<Iterable<UserDto>> body = Contract.okPage(
			page.getContent(), PageMeta.of(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages())
		);
		return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Contract<UserDto>> find(@PathVariable Long id) {
		Contract<UserDto> body = this.service.findById(id)
			.map(user -> Contract.ok(new UserDto(user)))
			.orElse(Contract.notFound("USER_NOT_FOUND", "User not found"));
		return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Contract<Void>> delete(@PathVariable Long id) {
		if (!exists(id)) {
			Contract<Void> body = Contract.notFound("USER_NOT_FOUND", "User not found");
			return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
		}
		this.service.deleteById(id);
		Contract<Void> body = Contract.noContent();
		return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Contract<UserDto>> patch(@PathVariable Long id, @RequestBody UserDto userDto) {
		var optionalUser = this.service.findById(id);
		if (optionalUser.isEmpty()) {
			Contract<UserDto> body = Contract.notFound("USER_NOT_FOUND", "User not found");
			return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
		}
		if (!userDto.isValidForUpdate()) {
			Contract<UserDto> body = Contract.badRequest("BAD_REQUEST", "no fields provided for update");
			return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
		}

		Users user = optionalUser.get();
		if (userDto.getFullname() != null && !userDto.getFullname().isBlank()) user.setFullname(userDto.getFullname());
		if (userDto.getUsername() != null && !userDto.getUsername().isBlank()) user.setUsername(userDto.getUsername());
		if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) user.setEmail(userDto.getEmail());
		if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) user.setPassword(userDto.getPassword());

		Contract<UserDto> body = Contract.ok(new UserDto(this.service.update(user)));
		return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
	}
}
