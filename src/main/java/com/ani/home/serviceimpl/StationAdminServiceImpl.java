package com.ani.home.serviceimpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ani.home.dto.StationAdminDTO;
import com.ani.home.model.StationAdmin;
import com.ani.home.model.StationAdmin.Status;
import com.ani.home.repo.StationAdminRepo;
import com.ani.home.service.StationAdminService;


@Service
public class StationAdminServiceImpl implements StationAdminService {

    @Autowired
    private StationAdminRepo stationAdminRepo;

    @Override
    public Optional<StationAdmin> authenticate(String email, String password) {
        return stationAdminRepo.findByEmailAndPassword(email, password);
    }

    @Override
    public String addUser(StationAdmin stationAdmin) {
        return stationAdminRepo.save(stationAdmin);
    }

    @Override
    public List<StationAdmin> getPendingAdmins() {
        return stationAdminRepo.findByStatus(Status.PENDING);
    }

    @Override
    public StationAdmin getAdminById(int id) {
        return stationAdminRepo.findById(id).orElse(null);
    }

    @Override
    public void approveAdmin(int id) {
        StationAdmin admin = getAdminById(id);
        if (admin != null) {
            admin.setStatus(Status.APPROVED);
            stationAdminRepo.save(admin);
        }
    }

    @Override
    public void rejectAdmin(int id) {
        StationAdmin admin = getAdminById(id);
        if (admin != null) {
            admin.setStatus(Status.REJECTED);
            stationAdminRepo.save(admin);
        }
    }

    @Override
    public int getCountByStatus(Status status) {
        return stationAdminRepo.countByStatus(status);
    }

    @Override
    public boolean isUserApproved(String email) {
        Optional<StationAdmin> optionalAdmin = stationAdminRepo.findByEmail(email);
        return optionalAdmin.isPresent() && optionalAdmin.get().getStatus() == StationAdmin.Status.APPROVED;
    }

    @Override
    public List<StationAdminDTO> findNearbyStations(BigDecimal latitude, BigDecimal longitude) {
        try {
            
            double latitudeRad = Math.toRadians(latitude.doubleValue());

            
            double radiusKm = 50;

            
            double latOffset = radiusKm / 111;
            double lonOffset = radiusKm / (111 * Math.cos(latitudeRad));

           
            BigDecimal latMin = latitude.subtract(BigDecimal.valueOf(latOffset));
            BigDecimal latMax = latitude.add(BigDecimal.valueOf(latOffset));
            BigDecimal lonMin = longitude.subtract(BigDecimal.valueOf(lonOffset));
            BigDecimal lonMax = longitude.add(BigDecimal.valueOf(lonOffset));

           
            List<StationAdmin> stations = stationAdminRepo.findByLatitudeBetweenAndLongitudeBetweenAndVehicleType(
                    latMin, latMax, lonMin, lonMax);

            
            List<StationAdminDTO> stationDTOs = new ArrayList<>();
            for (StationAdmin station : stations) {
                double distance = calculateDistance(station.getLatitude(), station.getLongitude(), latitude, longitude);
                stationDTOs.add(new StationAdminDTO(
                        station.getId(),
                        station.getChargingStationName(),
                        station.getEmail(),
                        station.getAddress(),
                        station.getLatitude(),
                        station.getLongitude(),
                        distance
                ));
            }

            return stationDTOs;
        } catch (Exception e) {
            e.printStackTrace();  
            throw new RuntimeException("Failed to fetch nearby stations", e);  
        }
    }

    private double calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        double earthRadius = 6371; 
        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLon = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1.doubleValue())) * Math.cos(Math.toRadians(lat2.doubleValue())) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }


    @Override
    public Optional<StationAdmin> findById(int id) {
        return stationAdminRepo.findById(id);
    }
    
	@Override
	public List<StationAdmin> getAllStations() {
		return stationAdminRepo.findAll();
	}

	@Override
	public List<StationAdmin> findBlockedUsers(Boolean blocked) {
		return stationAdminRepo.findByBlocked(blocked);
	}
	@Override
	public String blockUser(int stationId) {
		Optional<StationAdmin> userOpt = stationAdminRepo.findById(stationId);
        if (userOpt.isPresent()) {
            StationAdmin stationAdmin = userOpt.get();
            stationAdmin.setIsBlocked(true);
            stationAdminRepo.save(stationAdmin);
            return "User blocked successfully";
        } else {
            return "User not found";
        }
	}
	@Override
	public String unblockUser(int stationId) {
		Optional<StationAdmin> userOptional = stationAdminRepo.findById(stationId);
        if (userOptional.isPresent()) {
            StationAdmin user = userOptional.get();
            user.setIsBlocked(false);
            stationAdminRepo.save(user);
            return "User unblocked successfully";
        }
        return "User not found";
	}
	@Override
	public Optional<StationAdmin> getUserById(int id) {
		 return stationAdminRepo.findById(id);
	}
	@Override
	public StationAdmin getStationById(int id) {
        return stationAdminRepo.findById(id).orElse(null);
    }
	@Override
	public List<String> getAllAdminEmails() {
		return stationAdminRepo.findAll()
                .stream()
                .map(StationAdmin::getEmail)
                .collect(Collectors.toList());
	}
	@Override
	public StationAdmin updateStationAdmin(int id, StationAdmin stationAdmin) {
	    Optional<StationAdmin> existingStationAdmin = stationAdminRepo.findById(id);
	    if (existingStationAdmin.isPresent()) {
	        StationAdmin existingAdmin = existingStationAdmin.get();
	        
	        existingAdmin.setName(stationAdmin.getName());
	        existingAdmin.setAddress(stationAdmin.getAddress());
	        existingAdmin.setPhoneNumber(stationAdmin.getPhoneNumber());
	        existingAdmin.setEmail(stationAdmin.getEmail());
	        
	        return stationAdminRepo.save1(existingAdmin);
	    } else {
	        throw new RuntimeException("Station Admin not found");
	    }
	}

	@Override
	public boolean deleteStation(int id) {
		  try {
	            
	            if (stationAdminRepo.existsById(id)) {
	                
	                stationAdminRepo.deleteById(id);
	                return true; 
	            } else {
	                return false; 
	            }
	        } catch (Exception e) {
	            
	            e.printStackTrace(); 
	            return false; 
	        }
	}

}
