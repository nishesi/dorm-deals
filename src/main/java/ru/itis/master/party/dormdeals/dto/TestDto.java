package ru.itis.master.party.dormdeals.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.Test;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestDto {
    private String name;
    public static TestDto from(Test test) {
        return new TestDto(test.getName());
    }
}
