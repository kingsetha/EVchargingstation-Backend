


package com.ani.home.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ani.home.model.Slot;
import com.ani.home.model.Slot.ChargingType;
import com.ani.home.repo.SlotRepo;
import com.ani.home.service.EmailService;
import com.ani.home.service.SlotService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import com.ani.home.model.Booking.DeviceType;

import java.util.List;
import java.util.Optional;

@Service
public class SlotServiceImpl implements SlotService {

    @Autowired
    private SlotRepo slotRepository;

    @Override
    public void addSlot(Slot slot) {
        slotRepository.save(slot);
    }

    @Override
    public void deleteSlot(int id) {
        slotRepository.deleteById(id);
    }

    @Override
    public Optional<Slot> getSlotById(int id) {
        return slotRepository.findById(id);
    }

    @Override
    public List<Slot> getAllSlots() {
        return slotRepository.findAll();
    }

    @Override
    public Slot updateSlot(Slot slot) {
        return slotRepository.save(slot); // Ensure this returns Slot
    }

    @Override
    public List<Slot> getSlotsByStationId(String stationId) {
        return slotRepository.findByStationId(stationId); // Ensure this matches the repository method
    }
    @Override
    public List<Slot> getSlotsByStationIdNew(int id) {
        // Assuming your repository method is named findByStationId
        return slotRepository.findByStationIdNew(id);
    }
    @Override
    public void decreaseSlotCount(ChargingType chargingType) {
        // Fetch the slot before updating
        Slot slot = findAvailableSlotByChargingType(chargingType);
        
        if (slot != null) {
            // Log the current slot state before update
            System.out.println("Before Update - Slot ID: " + slot.getId() + 
                ", Level1 Count: " + slot.getLevel1Count() +
                ", Level2 Count: " + slot.getLevel2Count() + 
                ", DC Fast Charging Count: " + slot.getDcFastChargingCount());
            
            // Decrease the count for the specified charging type
            slot.decreaseCount(chargingType);
            
            // Save the updated slot
            slotRepository.save(slot);
            
            // Log the updated slot state
            System.out.println("After Update - Slot ID: " + slot.getId() + 
                ", Level1 Count: " + slot.getLevel1Count() +
                ", Level2 Count: " + slot.getLevel2Count() + 
                ", DC Fast Charging Count: " + slot.getDcFastChargingCount());
        } else {
            // Handle case where no available slot is found
            throw new RuntimeException("No available slot found for charging type: " + chargingType);
        }
    }

    
    @Override
    public Slot findAvailableSlotByChargingType(ChargingType chargingType) {
        if (chargingType == null) {
            throw new IllegalArgumentException("ChargingType must not be null");
        }
        Slot slot = slotRepository.findAvailableSlotByChargingType(chargingType);
        System.out.println("Found slot: " + slot);
        return slot;
    }

    public void save(Slot slot) {
        // Validate the slot if needed
        if (slot == null) {
            throw new IllegalArgumentException("Slot must not be null");
        }

        // Save the slot to the database
        slotRepository.save(slot);
    }
    @Override
    public List<Slot> getSlotHistoryByStationId(int stationId) {
        return slotRepository.findByStationId(stationId);
    }
    @Override
    public void completeSlot(int slotId) {
        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new EntityNotFoundException("Slot not found"));
        slot.setStatus(Slot.SlotStatus.COMPLETED); // Update the status
        slotRepository.save(slot);
    }
    @Autowired
    private EmailService emailService;


    public void completeSlot(int slotId, String userEmail) {
        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new EntityNotFoundException("Slot not found"));
        slot.setStatus(Slot.SlotStatus.COMPLETED);
        slotRepository.save(slot);

        // Send feedback request email
        emailService.sendFeedbackRequestEmail(userEmail, slotId);
    }
	
    

}


