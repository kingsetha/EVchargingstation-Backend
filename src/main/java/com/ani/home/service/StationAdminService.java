//package com.ani.home.service;
//
//import java.util.Optional;
//
//import com.ani.home.model.Admin;
//import com.ani.home.model.StationAdmin;
//
//public interface StationAdminService {
//	Optional<StationAdmin> authenticate(String email, String password);
//
//	String addUser(StationAdmin stationAdmin);
//
//}
package com.ani.home.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.ani.home.dto.StationAdminDTO;
import com.ani.home.model.StationAdmin;
import com.ani.home.model.StationAdmin.Status;

public interface StationAdminService {

    Optional<StationAdmin> authenticate(String email, String password);

    String addUser(StationAdmin stationAdmin);

    List<StationAdmin> getPendingAdmins();

    StationAdmin getAdminById(int id);

    void approveAdmin(int id);
    void rejectAdmin(int id);
    int getCountByStatus(Status status);
    boolean isUserApproved(String email);
    List<StationAdminDTO> findNearbyStations(BigDecimal latitude, BigDecimal longitude);

	Optional<StationAdmin> findById(int id);

	public  List<StationAdmin> getAllStations();


	String blockUser(int stationId);

	String unblockUser(int stationId);

	List<StationAdmin> findBlockedUsers(Boolean blocked);

	Optional<StationAdmin> getUserById(int id);
	public StationAdmin getStationById(int id);

	List<String> getAllAdminEmails();
	 StationAdmin updateStationAdmin(int id, StationAdmin stationAdmin);

	boolean deleteStation(int id);
	
		
}
