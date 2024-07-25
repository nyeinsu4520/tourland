package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    public List<Card> getAllCards() {
        return cardRepository.findAll(); // Implement according to your repository method
    }
    
    @Transactional
    public void saveCard(Card card) {
        cardRepository.save(card); // This method should handle persisting entities correctly
    }

    @Transactional
    public void updateCard(Card updatedCard) {
        cardRepository.save(updatedCard); // Save method handles updates correctly
    }
    

    public List<Card> getAllHotels() {
        return cardRepository.findAll();
    }


}

