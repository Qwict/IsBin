package com.qwict.isbin.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private Long id;

    @Pattern(regexp = "[a-zA-Z]+", message = "Name should only contain letters")
    @NotNull(message = "Name should not be null")
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Min(value = 0, message = "Place code 1 should not be less than 0")
    @Max(value = 300, message = "Place code 1 should not be greater than 300")
    @NotNull(message = "Place code 1 should not be null")
    @NotEmpty(message = "Place code 1 should not be empty")
    private Integer placeCode1;

    @Min(value = 0, message = "Place code 2 should not be less than 0")
    @Max(value = 300, message = "Place code 2 should not be greater than 300")
    @NotNull(message = "Place code 2 should not be null")
    @NotEmpty(message = "Place code 2 should not be empty")
    private Integer placeCode2;

    @Override
    public String toString() {
        return "\n\tLocationDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", placeCode1=" + placeCode1 +
                ", placeCode2=" + placeCode2 +
                '}';
    }

    public boolean isNotBlank() {
        return this.name != null && !this.name.isBlank() && this.placeCode1 != null && this.placeCode2 != null;
    }
}