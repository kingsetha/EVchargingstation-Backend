////package com.ani.home.service;
////
////import java.util.List;
////import java.util.Optional;
////
////import com.ani.home.model.Slot;
////
////public interface SlotService {
////    void addSlot(Slot slot);
////    void deleteSlot(int id);
////    Optional<Slot> getSlotById(int id);
////    List<Slot> getAllSlots();
////    Slot updateSlot(Slot slot); 
////    public List<Slot> getSlotsByStationId(String stationId);// Return Slot instead of String
////}
//// SlotService.java
//package com.ani.home.service;
//
//import java.util.List;
//import java.util.Optional;
//
//import com.ani.home.model.Slot;
//
//public interface SlotService {
//    void addSlot(Slot slot);
//    void deleteSlot(int id);
//    Optional<Slot> getSlotById(int id);
//    List<Slot> getAllSlots();
//    Slot updateSlot(Slot slot); // Ensure this returns Slot
//    List<Slot> getSlotsByStationId(String stationId);
////	List<Slot> getSlotsByStationIdNew(int stationId);
//	
//}
//
package com.ani.home.service;

import java.util.List;
import java.util.Optional;
import com.ani.home.model.Booking.DeviceType;

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
