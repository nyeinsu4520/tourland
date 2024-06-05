package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public Optional<Card> findById(Integer id) {
        return cardRepository.findById(id);
    }

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public void deleteById(Integer id) {
        cardRepository.deleteById(id);
    }
}

