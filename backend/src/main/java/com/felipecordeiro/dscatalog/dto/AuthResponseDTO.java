package com.felipecordeiro.dscatalog.dto;

public record AuthResponseDTO(String accessToken, UserResponseDTO user) {
}