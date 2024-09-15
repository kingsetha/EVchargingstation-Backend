
package com.ani.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ani.home.model.Slot;
import com.ani.home.serviceimpl.SlotServiceImpl;

@RestController
@RequestMapping("/station/slots")
@CrossOrigin("*")
public class SlotController {

    private static final Logger logger = LoggerFactory.getLogger(SlotController.class);

    @Autowired
    private SlotServiceImpl service;

    @PostMapping
    public ResponseEntity<String> addSlot(@RequestBody Slot slot) {
        // Log the received ChargingStation ID
        System.out.println("Received ChargingStation ID: " + 
            (slot.getChargingStation() != null ? slot.getChargingStation().getId() : "null"));

        try {
            // Validate the ChargingStation ID (for instance, you might want to check if it exists in the database)
            if (slot.getChargingStation() == null || slot.getChargingStation().getId() <= 0) {
                return new ResponseEntity<>("Invalid ChargingStation ID", HttpStatus.BAD_REQUEST);
            }
            
            // Add the slot to the database
            service.addSlot(slot);
            return new ResponseEntity<>("Slot added successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add slot", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<String> updateSlot(@PathVariable("id") int id, @RequestBody Slot slot) {
//        try {
//            slot.setId(id);
//            service.updateSlot(slot);
//            return new ResponseEntity<>("Slot updated successfully", HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            logger.error("Invalid input: {}", e.getMessage());
//            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            logger.error("Failed to update slot: {}", e.getMessage());
//            return new ResponseEntity<>("Failed to update slot", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSlot(@PathVariable("id") int id, @RequestBody  Slot slot) {
        try {
            // Validate that the Slot object is valid
            if (id <= 0) {
                return new ResponseEntity<>("Invalid slot ID", HttpStatus.BAD_REQUEST);
            }
            
            // Set the ID from the URL to the slot object
            slot.setId(id);
            
            // Call the service to update the slot
            service.updateSlot(slot);
            
            // Return success response
            return new ResponseEntity<>("Slot updated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Log and handle invalid input
            logger.error("Invalid input: {}", e.getMessage());
            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log and handle other exceptions
            logger.error("Failed to update slot: {}", e.getMessage());
            return new ResponseEntity<>("Failed to update slot", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSlot(@PathVariable("id") int id) {
        try {
            service.deleteSlot(id);
            return new ResponseEntity<>("Slot deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input: {}", e.getMessage());
            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Failed to delete slot: {}", e.getMessage());
            return new ResponseEntity<>("Failed to delete slot", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/available")
    public ResponseEntity<?> getAvailableSlots(@RequestParam(required = false) String stationId) {
        try {
            if (stationId != null && !stationId.isEmpty()) {
                // Filter slots based on stationId
                List<Slot> slots = service.getSlotsByStationId(stationId);
                return new ResponseEntity<>(slots, HttpStatus.OK);
            } else {
                // Return all slots if no stationId is provided
                List<Slot> slots = service.getAllSlots();
                return new ResponseEntity<>(slots, HttpStatus.OK);
            }
        } catch (Exception e) {
            // Return a generic error message to the client
            return new ResponseEntity<>("Failed to fetch available slots", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/history/{stationId}")
    public ResponseEntity<?> getSlotHistoryByStationId(@PathVariable("stationId") int stationId) {
        System.out.println("Received stationId: " + stationId); // Log the received ID
        try {
            List<Slot> slots = service.getSlotHistoryByStationId(stationId);
            if (slots.isEmpty()) {
                return new ResponseEntity<>("No slot history available for the given station ID", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(slots, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return new ResponseEntity<>("Failed to fetch slot history", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
