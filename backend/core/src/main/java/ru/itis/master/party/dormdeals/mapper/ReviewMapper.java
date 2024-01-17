package ru.itis.master.party.dormdeals.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import ru.itis.master.party.dormdeals.dto.review.ReviewDto;
import ru.itis.master.party.dormdeals.models.jpa.Review;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        uses = {ProductMapper.class, UserMapper.class},
        builder = @Builder(disableBuilder = true))
public interface ReviewMapper {

    ReviewDto toReviewDto(Review review);
}
