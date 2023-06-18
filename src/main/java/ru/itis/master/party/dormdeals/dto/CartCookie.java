package ru.itis.master.party.dormdeals.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartCookie {
    private Long id;
    private Integer count;
    private Boolean stateProduct;
}
