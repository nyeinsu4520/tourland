package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class RoomTypeService {

    @Autowired
    private RoomTypeRepository roomTypeRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void updateRoomType(RoomType updatedRoomType) {
        entityManager.merge(updatedRoomType); // Use merge for updates
    }

    public RoomType findById(Long id) {
        return roomTypeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Room Type not found with id " + id));
    }
    
    
    public RoomType findByRoomTypeId(Long id) {
        return roomTypeRepository.findById(id).orElse(null);
    }
    
 
}
