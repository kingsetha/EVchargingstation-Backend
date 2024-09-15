
package com.ani.home.repo;

import java.util.List;
import java.util.Optional;

import com.ani.home.model.Booking;
import com.ani.home.model.Booking.DeviceType;
import com.ani.home.model.Slot;
import com.ani.home.model.Slot.ChargingType;

public interface SlotRepo {
    Slot save(Slot slot);  // Ensure this returns Slot
    void deleteById(int id);
    Optional<Slot> findById(int id);
    List<Slot> findAll();
    List<Slot> findByStationId(String stationId); // Ensure this is correct
	List<Slot> findByStationIdNew(int id);
	Slot findAvailableSlotByChargingType(ChargingType chargingType);
	List<Slot> findByStationId(int stationId); 
//	List<Booking> findSlotAndUserDetailsByStationId(int stationId);
	
	
}