package com.example.demo;
import org.springframework.stereotype.Service;
import com.example.demo.Package;
import com.example.demo.Member;
import com.example.demo.Favorite;
import com.example.demo.TripPackage;
import com.example.demo.Card;
import com.example.demo.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);
    private final PackageRepository packageRepository;
    private final TripPackageRepository tripPackageRepository;
    private final CardRepository cardRepository;
    private final RestaurantRespository restaurantRepository;
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(PackageRepository packageRepository,
                           TripPackageRepository tripPackageRepository,
                           CardRepository cardRepository,
                           RestaurantRespository restaurantRepository,
                           FavoriteRepository favoriteRepository,
                           MemberRepository memberRepository) {
        this.packageRepository = packageRepository;
        this.tripPackageRepository = tripPackageRepository;
        this.cardRepository = cardRepository;
        this.restaurantRepository = restaurantRepository;
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
    }
    
    public List<Favorite> getFavoritesByMemberId(Integer memberId) {
        return favoriteRepository.findByMemberId(memberId);
    }
    

    // Method to add a favorite Package
    public void addFavorite(Integer packageId, String type, Integer memberId) {
        Optional<Package> packageOptional = packageRepository.findById(packageId);
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if (packageOptional.isPresent() && memberOptional.isPresent()) {
            Package pkg = packageOptional.get();
            Member member = memberOptional.get();

            // Check if the favorite already exists
            Optional<Favorite> existingFavorite = favoriteRepository.findByPackageIdAndMemberId(packageId, memberId);
            if (existingFavorite.isPresent()) {
                throw new IllegalArgumentException("This package is already added to favorites.");
            }

            Favorite favorite = new Favorite(pkg, type, member);
            favoriteRepository.save(favorite);
        } else {
            throw new IllegalArgumentException("Package or member not found");
        }
    }

 // Add favorite TripPackage
    public void addFavoriteTripPackage(Long tripPackageId, String type, Integer memberId) {
        Optional<TripPackage> tripPackageOptional = tripPackageRepository.findById(tripPackageId);
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        tripPackageOptional.ifPresent(tripPackage -> {
            memberOptional.ifPresent(member -> {
                Optional<Favorite> existingFavorite = favoriteRepository.findByTripPackageIdAndMemberId(tripPackageId, memberId);
                if (existingFavorite.isPresent()) {
                    throw new IllegalArgumentException("This trip package is already added to favorites.");
                }

                Favorite favorite = new Favorite(tripPackage, type, member);
                favoriteRepository.save(favorite);
            });
        });

        if (!tripPackageOptional.isPresent() || !memberOptional.isPresent()) {
            throw new IllegalArgumentException("Trip package or member not found");
        }
    }

    // Method to add a favorite Card (Hotel)
    public void addFavoriteCard(Integer cardId, String type, Integer memberId) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if (cardOptional.isPresent() && memberOptional.isPresent()) {
            Card card = cardOptional.get();
            Member member = memberOptional.get();

            // Check if the favorite already exists
            Optional<Favorite> existingFavorite = favoriteRepository.findByCardIdAndMemberId(cardId, memberId);
            if (existingFavorite.isPresent()) {
                throw new IllegalArgumentException("This hotel (card) is already added to favorites.");
            }

            Favorite favorite = new Favorite(card, type, member);
            favoriteRepository.save(favorite);
        } else {
            throw new IllegalArgumentException("Card (hotel) or member not found");
        }
    }

    // Method to add a favorite Restaurant
    public void addFavoriteRestaurant(Integer restaurantId, String type, Integer memberId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if (restaurantOptional.isPresent() && memberOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            Member member = memberOptional.get();

            // Check if the favorite already exists
            Optional<Favorite> existingFavorite = favoriteRepository.findByRestaurantIdAndMemberId(restaurantId, memberId);
            if (existingFavorite.isPresent()) {
                throw new IllegalArgumentException("This restaurant is already added to favorites.");
            }

            Favorite favorite = new Favorite(restaurant, type, member);
            favoriteRepository.save(favorite);
        } else {
            throw new IllegalArgumentException("Restaurant or member not found");
        }
    }

    // Method to remove a favorite by TripPackage ID
    public void removeFavoriteTripPackage(Long tripPackageId) {
        List<Favorite> favorites = favoriteRepository.findByTripPackageId(tripPackageId);
        if (!favorites.isEmpty()) {
            favoriteRepository.deleteAll(favorites);
        } else {
            throw new IllegalArgumentException("Favorite not found for tripPackageId: " + tripPackageId);
        }
    }

    // Method to remove a favorite by Card (Hotel) ID
    public void removeFavoriteCard(Integer cardId) {
        List<Favorite> favorites = favoriteRepository.findByCardId(cardId);
        if (!favorites.isEmpty()) {
            favoriteRepository.deleteAll(favorites);
        } else {
            throw new IllegalArgumentException("Favorite not found for cardId: " + cardId);
        }
    }

    // Method to remove a favorite by Restaurant ID
    public void removeFavoriteRestaurant(Integer restaurantId) {
        List<Favorite> favorites = favoriteRepository.findByRestaurantId(restaurantId);
        if (!favorites.isEmpty()) {
            favoriteRepository.deleteAll(favorites);
        } else {
            throw new IllegalArgumentException("Favorite not found for restaurantId: " + restaurantId);
        }
    }
    
    public void removeFavorite(Integer packageId) {
        List<Favorite> favorites = favoriteRepository.findByPackageId(packageId);
        if (!favorites.isEmpty()) {
            favoriteRepository.deleteAll(favorites);
        } else {
            throw new IllegalArgumentException("Favorite not found for packageId: " + packageId);
        }
    }

    
    // Get all favorites
    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }
}
