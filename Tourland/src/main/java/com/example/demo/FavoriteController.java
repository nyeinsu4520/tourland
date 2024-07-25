package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final MemberService memberService;

    public FavoriteController(FavoriteService favoriteService, MemberService memberService) {
        this.favoriteService = favoriteService;
        this.memberService = memberService;
    }

    @GetMapping("/favorites")
    public String viewFavorites(Model model) {
        Integer memberId = memberService.getLoggedInMemberId();
        List<Favorite> favorites = favoriteService.getFavoritesByMemberId(memberId);
        model.addAttribute("favorites", favorites);
        return "favorites";
    }
    @PostMapping("/addToFavorites")
    public String addToFavorites(@RequestParam("type") String type,
                                 @RequestParam(value = "packageId", required = false) Integer packageId,
                                 @RequestParam(value = "tripPackageId", required = false) Long tripPackageId,
                                 @RequestParam(value = "cardId", required = false) Integer cardId,
                                 @RequestParam(value = "restaurantId", required = false) Integer restaurantId,
                                 Model model) {
        Integer memberId = memberService.getLoggedInMemberId();

        try {
            if (memberId == null) {
                throw new IllegalArgumentException("Member ID is null or invalid.");
            }

            if ("Package".equals(type)) {
                favoriteService.addFavorite(packageId, type, memberId);
            } else if ("Trip_Package".equals(type)) {
                favoriteService.addFavoriteTripPackage(tripPackageId, type, memberId);
            } else if ("Hotel".equals(type)) {
                favoriteService.addFavoriteCard(cardId, type, memberId);
            } else if ("Restaurant".equals(type)) {
                favoriteService.addFavoriteRestaurant(restaurantId, type, memberId);
            } else {
                throw new IllegalArgumentException("Invalid favorite type: " + type);
            }

            return "redirect:/favorites";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            List<Favorite> favorites = favoriteService.getFavoritesByMemberId(memberId);
            model.addAttribute("favorites", favorites);
            return "favorites"; // Show the favorites page with the error message
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }



    @PostMapping("/removeFavorite")
    public String removeFavorite(@RequestParam("type") String type, @RequestParam("id") String idStr) {
        try {
            Integer memberId = memberService.getLoggedInMemberId();

            switch (type) {
                case "Package":
                    Integer packageId = Integer.valueOf(idStr);
                    favoriteService.removeFavorite(packageId);
                    break;
                case "Trip_Package":
                    Long tripPackageId = Long.valueOf(idStr);
                    favoriteService.removeFavoriteTripPackage(tripPackageId);
                    break;
                case "Hotel":
                    Integer hotelId = Integer.valueOf(idStr);
                    favoriteService.removeFavoriteCard(hotelId);
                    break;
                case "Restaurant":
                    Integer restaurantId = Integer.valueOf(idStr);
                    favoriteService.removeFavoriteRestaurant(restaurantId);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid favorite type: " + type);
            }
            return "redirect:/favorites";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
