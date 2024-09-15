
package com.ani.home.repo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.ani.home.model.StationAdmin;
import com.ani.home.model.StationAdmin.Status;

public interface StationAdminRepo {
    Optional<StationAdmin> findByEmailAndPassword(String email, String password);

    String save(StationAdmin user);
    
    List<StationAdmin> findByStatus(StationAdmin.Status status);
    
    void updateStatus(int id, StationAdmin.Status status);

	Optional<StationAdmin> findById(int id);
	Optional<StationAdmin> findByEmail(String email);

	int countByStatus(Status status);
	 List<StationAdmin> findByLatitudeBetweenAndLongitudeBetweenAndVehicleType(
	            BigDecimal latMin, BigDecimal latMax, BigDecimal lonMin, BigDecimal lonMax);

	List<StationAdmin> findAll();

	public List<StationAdmin> findByBlocked(Boolean blocked);

	StationAdmin findByStationId(String stationId);

	boolean existsById(int id);

	StationAdmin save1(StationAdmin stationAdmin);

	void deleteById(int id);

}
