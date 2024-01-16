package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.DormitoryDto;
import ru.itis.master.party.dormdeals.models.jpa.Dormitory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DormitoryConverter {

    public DormitoryDto from(Dormitory dormitory) {
        return DormitoryDto.builder()
                .id(dormitory.getId())
                .name(dormitory.getName())
                .address(dormitory.getAddress())
                .build();
    }

    public List<DormitoryDto> from(List<Dormitory> dormitories) {
        return dormitories.stream().map(this::from).toList();
    }
}
