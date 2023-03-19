package ru.itis.master.party.dormdeals.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DormitoryDto {

    private Long id;

    private String name;

    private String address;
}
