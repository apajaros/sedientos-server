package com.sedientos.restapi.controller;

import com.sedientos.data.model.Place;
import com.sedientos.data.model.Review;
import com.sedientos.data.model.User;
import com.sedientos.restapi.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class ReviewController {

    private final PlaceRepository repository;

    @Autowired
    public ReviewController(PlaceRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/places/{id}/reviews")
    public @ResponseBody ResponseEntity<?> getReviews(
            @PathVariable("id") String id) {
        Place place = repository.findOne(id);
        if (place == null) {
            return ResponseEntity.notFound().build();
        }
        Resources<Review> resources = new Resources<>(place.getReviews());
        resources.add(linkTo(
                methodOn(ReviewController.class).getReviews(id))
                .withSelfRel());
        return ResponseEntity.ok(resources);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/places/{id}/reviews")
    public @ResponseBody ResponseEntity<?> addReview(Principal principal,
            @PathVariable("id") String id, @RequestBody Review review) {
        Place place = repository.findOne(id);
        if (place == null) {
            return ResponseEntity.notFound().build();
        }
        if (review.getStars() == 0) {
            return ResponseEntity.badRequest().build();
        }
        if (review.getUser() == null) {
            User user = new User();
            user.setName(principal.getName());
            review.setUser(user);
        }
        // TODO don't allow the same user to add more than one review a month
        review.setDate(new Date());
        place.addReview(review);
        repository.save(place);

        Resources<Review> resources = new Resources<>(place.getReviews());
        resources.add(linkTo(
                methodOn(ReviewController.class).getReviews(id))
                .withSelfRel());
        return ResponseEntity.ok(resources);
    }
}
