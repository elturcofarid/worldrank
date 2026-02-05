package com.worldrank.app.auth.controller;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
      @Schema(
        description = "Correo electrónico del usuario",
        example = "elturcofarid@gmail.com"
    )
    String email, 

     @Schema(
        description = "Contraseña del usuario",
        example = "123456"
    )
    String password
) {}