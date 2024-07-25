package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // Find all favorites for a member
    List<Favorite> findByMemberId(int memberId);

    // Find favorites by Package 
    @Query("SELECT f FROM Favorite f WHERE f.pkg.id = :packageId")
    List<Favorite> findByPackageId(@Param("packageId") Integer packageId);

    // Find favorite by Package ID and Member ID
    @Query("SELECT f FROM Favorite f WHERE f.pkg.id = :packageId AND f.member.id = :memberId")
    Optional<Favorite> findByPackageIdAndMemberId(@Param("packageId") Integer packageId, @Param("memberId") Integer memberId);
    
    
 // Find favorites by Package (TripPackage) ID
    @Query("SELECT f FROM Favorite f WHERE f.tripPackage.id = :tripPackageId")
    List<Favorite> findByTripPackageId(@Param("tripPackageId") Long tripPackageId);


    // Find favorite by Package (TripPackage) ID and Member ID
    @Query("SELECT f FROM Favorite f WHERE f.tripPackage.id = :trippackageId AND f.member.id = :memberId")
    Optional<Favorite> findByTripPackageIdAndMemberId(@Param("trippackageId") Long trippackageId, @Param("memberId") Integer memberId);


    // Find favorites by Card (Hotel)
    @Query("SELECT f FROM Favorite f WHERE f.card.id = :cardId")
    List<Favorite> findByCardId(@Param("cardId") Integer cardId);

    // Find favorite by Card (Hotel) ID and Member ID
    @Query("SELECT f FROM Favorite f WHERE f.card.id = :cardId AND f.member.id = :memberId")
    Optional<Favorite> findByCardIdAndMemberId(@Param("cardId") Integer cardId, @Param("memberId") Integer memberId);

    // Find favorites by Restaurant
    @Query("SELECT f FROM Favorite f WHERE f.restaurant.id = :restaurantId")
    List<Favorite> findByRestaurantId(@Param("restaurantId") Integer restaurantId);

    // Find favorite by Restaurant ID and Member ID
    @Query("SELECT f FROM Favorite f WHERE f.restaurant.id = :restaurantId AND f.member.id = :memberId")
    Optional<Favorite> findByRestaurantIdAndMemberId(@Param("restaurantId") Integer restaurantId, @Param("memberId") Integer memberId);
}
