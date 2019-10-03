package org.paniergarni.stock.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FarmerDTO {


    @NotBlank
    @Size(min = 4, max = 50, message = "farmer.name.incorrect.size")
    private String name;
    @NotBlank
    @Size(min = 10, max = 70, message = "farmer.location.incorrect.size")
    private String location;
    @NotBlank
    @Size(min = 10, max = 10, message = "farmer.phone.incorrect.size")
    private String phone;

}
