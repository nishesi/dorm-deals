package ru.itis.master.party.dormdeals.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.TestDto;
import ru.itis.master.party.dormdeals.models.Test;
import ru.itis.master.party.dormdeals.repositories.TestRepo;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepo testRepo;
    public TestDto getTestDto(long id) {
        Test test = testRepo.findById(id).orElseThrow(NoSuchElementException::new);
        return TestDto.from(test);
    }
}
