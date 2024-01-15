package ru.itis.master.party.dormdeals.search;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchLink<T> {
    List<T> findAll(String text, Pageable pageable);
}
