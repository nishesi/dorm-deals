package ru.itis.master.party.dormdeals.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.itis.master.party.dormdeals.models.ItemElastic;

import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogueElastic {
    private String name;
    private Long catalogueId;
    private List<ItemElastic> items;
    private String brand;
}
