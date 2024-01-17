package ru.itis.master.party.dormdeals.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.models.jpa.Dormitory;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        builder = @Builder(disableBuilder = true))
public interface DormitoryMapper {
    ShopDto.Dormitory toDormitoryDto(Dormitory dormitory);

    List<ShopDto.Dormitory> toDormitoryDto(List<Dormitory> dormitories);
}
