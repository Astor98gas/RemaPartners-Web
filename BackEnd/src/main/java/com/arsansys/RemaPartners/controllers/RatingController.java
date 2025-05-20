package com.arsansys.RemaPartners.controllers;

import com.arsansys.RemaPartners.controllers.dto.RatingDTO;
import com.arsansys.RemaPartners.controllers.dto.RatingReplyDTO;
import com.arsansys.RemaPartners.models.entities.RatingEntity;
import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.services.RatingService;
import com.arsansys.RemaPartners.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserService userService;

    /**
     * Obtiene todas las valoraciones de un vendedor
     */
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<RatingEntity>> getRatingsBySeller(@PathVariable String sellerId) {
        try {
            List<RatingEntity> ratings = ratingService.getRatingsBySeller(sellerId);
            return ResponseEntity.ok(ratings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene una valoración específica por su ID
     */
    @GetMapping("/{ratingId}")
    public ResponseEntity<RatingEntity> getRatingById(@PathVariable String ratingId) {
        try {
            RatingEntity rating = ratingService.getRatingById(ratingId);
            return ResponseEntity.ok(rating);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene la valoración que un usuario ha hecho a un vendedor específico
     */
    @GetMapping("/user/{userId}/seller/{sellerId}")
    public ResponseEntity<RatingEntity> getUserRatingForSeller(
            @PathVariable String userId,
            @PathVariable String sellerId) {
        try {
            RatingEntity rating = ratingService.getUserRatingForSeller(userId, sellerId);
            return ResponseEntity.ok(rating);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea una nueva valoración
     */
    @PostMapping("/create")
    public ResponseEntity<?> createRating(@RequestBody RatingDTO ratingDTO) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity currentUser = userService.getUserByUsername(username);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            // Validar que el usuario no está intentando valorarse a sí mismo
            if (currentUser.getId().equals(ratingDTO.getSellerId())) {
                return ResponseEntity.badRequest().body("You cannot rate yourself");
            }

            // Validar puntuación
            if (ratingDTO.getRating() < 1 || ratingDTO.getRating() > 5) {
                return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
            }

            // Crear la entidad de valoración
            RatingEntity rating = RatingEntity.builder()
                    .sellerId(ratingDTO.getSellerId())
                    .userId(currentUser.getId())
                    .username(currentUser.getUsername())
                    .rating(ratingDTO.getRating())
                    .comment(ratingDTO.getComment())
                    .build();

            // Guardar la valoración
            RatingEntity savedRating = ratingService.createRating(rating);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRating);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Actualiza una valoración existente
     */
    @PutMapping("/{ratingId}")
    public ResponseEntity<?> updateRating(
            @PathVariable String ratingId,
            @RequestBody RatingDTO ratingDTO) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity currentUser = userService.getUserByUsername(username);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            // Verificar que la valoración existe y pertenece al usuario
            RatingEntity existingRating = ratingService.getRatingById(ratingId);
            if (!existingRating.getUserId().equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to edit this rating");
            }

            // Validar puntuación
            if (ratingDTO.getRating() < 1 || ratingDTO.getRating() > 5) {
                return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
            }

            // Crear la entidad actualizada
            RatingEntity updatedRating = RatingEntity.builder()
                    .rating(ratingDTO.getRating())
                    .comment(ratingDTO.getComment())
                    .build();

            // Actualizar la valoración
            RatingEntity savedRating = ratingService.updateRating(ratingId, updatedRating);
            return ResponseEntity.ok(savedRating);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Añade una respuesta del vendedor a una valoración
     */
    @PostMapping("/reply")
    public ResponseEntity<?> addReplyToRating(@RequestBody RatingReplyDTO replyDTO) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity currentUser = userService.getUserByUsername(username);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            // Verificar que la valoración existe
            RatingEntity existingRating = ratingService.getRatingById(replyDTO.getRatingId());

            // Verificar que el usuario es el vendedor de esta valoración
            if (!existingRating.getSellerId().equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the seller can reply to this rating");
            }

            // Añadir la respuesta
            RatingEntity updatedRating = ratingService.addReplyToRating(replyDTO.getRatingId(), replyDTO.getReply());
            return ResponseEntity.ok(updatedRating);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Elimina una valoración
     */
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<?> deleteRating(@PathVariable String ratingId) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity currentUser = userService.getUserByUsername(username);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            // Verificar que la valoración existe
            RatingEntity existingRating = ratingService.getRatingById(ratingId);

            // Verificar que el usuario es quien creó la valoración o es un administrador
            if (!existingRating.getUserId().equals(currentUser.getId()) &&
                    !"ADMIN".equals(currentUser.getRol().getName().toString())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to delete this rating");
            }

            // Eliminar la valoración
            ratingService.deleteRating(ratingId);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
