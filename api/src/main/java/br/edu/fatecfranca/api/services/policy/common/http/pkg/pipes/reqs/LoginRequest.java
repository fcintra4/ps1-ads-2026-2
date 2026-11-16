package br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String usernameOrEmail, @NotBlank String password) {}

