
package com.ani.home.service;

import java.util.List;
import java.util.Optional;


import com.ani.home.model.Slot;
import com.ani.home.model.Slot.ChargingType;

public interface SlotService {
    void addSlot(Slot slot);
    void deleteSlot(int id);
    Optional<Slot> getSlotById(int id);
    List<Slot> getAllSlots();
    Slot updateSlot(Slot slot); // Ensure this returns Slot
    List<Slot> getSlotsByStationId(String stationId);
	List<Slot> getSlotsByStationIdNew(int id);
	void decreaseSlotCount(ChargingType chargingType);
	Slot findAvailableSlotByChargingType(ChargingType chargingType);
	void save(Slot slot);
	List<Slot> getSlotHistoryByStationId(int stationId);
	void completeSlot(int slotId);
	 
	
}
