
package com.ani.home.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ani.home.model.Slot;
import com.ani.home.repo.SlotRepo;
import com.ani.home.serviceimpl.SlotServiceImpl;

@RestController
@RequestMapping("/station/slots")
@CrossOrigin("*")
public class SlotController {

    private static final Logger logger = LoggerFactory.getLogger(SlotController.class);

   
    private SlotServiceImpl service;
    
    public SlotController(SlotServiceImpl service, SlotRepo slotRepository) {
		super();
		this.service = service;
		this.slotRepository = slotRepository;
	}
	
    private SlotRepo slotRepository;

    @PostMapping
    public ResponseEntity<String> addSlot(@RequestBody Slot slot) {
       
        System.out.println("Received ChargingStation ID: " + 
            (slot.getChargingStation() != null ? slot.getChargingStation().getId() : "null"));

        try {
             if (slot.getChargingStation() == null || slot.getChargingStation().getId() <= 0) {
                return new ResponseEntity<>("Invalid ChargingStation ID", HttpStatus.BAD_REQUEST);
            }
            
            
            service.addSlot(slot);
            return new ResponseEntity<>("Slot added successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add slot", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSlot(@PathVariable("id") int id, @RequestBody  Slot slot) {
        try {
            
            if (id <= 0) {
                return new ResponseEntity<>("Invalid slot ID", HttpStatus.BAD_REQUEST);
            }
            
            
            slot.setId(id);
            
           
            service.updateSlot(slot);
            
            
            return new ResponseEntity<>("Slot updated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            
            logger.error("Invalid input: {}", e.getMessage());
            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            
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
                
                List<Slot> slots = service.getSlotsByStationId(stationId);
                return new ResponseEntity<>(slots, HttpStatus.OK);
            } else {
                
                List<Slot> slots = service.getAllSlots();
                return new ResponseEntity<>(slots, HttpStatus.OK);
            }
        } catch (Exception e) {
            
            return new ResponseEntity<>("Failed to fetch available slots", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/history/{stationId}")
    public ResponseEntity<?> getSlotHistoryByStationId(@PathVariable("stationId") int stationId) {
        System.out.println("Received stationId: " + stationId); 
        try {
            List<Slot> slots = service.getSlotHistoryByStationId(stationId);
            if (slots.isEmpty()) {
                return new ResponseEntity<>("No slot history available for the given station ID", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(slots, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to fetch slot history", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public void updateSlotsToCompleted() {
        List<Slot> slots = slotRepository.findAll(); 
        LocalDateTime now = LocalDateTime.now();

        for (Slot slot : slots) {
            LocalDateTime endTime = LocalDateTime.parse(slot.getEndTime()); 
            if (endTime.isBefore(now) && slot.getStatus() == Slot.SlotStatus.BOOKED) {
                slot.setStatus(Slot.SlotStatus.COMPLETED);
                slotRepository.save(slot);
            }
        }
    }


}
