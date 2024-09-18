
package com.ani.home.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "slots")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private StationAdmin chargingStation;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "charging_type", nullable = false)
    private ChargingType chargingType = ChargingType.LEVEL_1;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SlotStatus status = SlotStatus.AVAILABLE;

    @Column(name = "level1_count", nullable = false)
    private int level1Count = 0;

    @Column(name = "level2_count", nullable = false)
    private int level2Count = 0;

    @Column(name = "dc_fast_charging_count", nullable = false)
    private int dcFastChargingCount = 0;

    @Column(name = "created_at", updatable = false)
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    private static final String DATE_FORMAT = "d/M/yyyy, hh:mm:ss a";

    @PrePersist
    protected void onCreate() {
        createdAt = formatDateTime(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = formatDateTime(System.currentTimeMillis());
    }

    public boolean isCompleted() {
        return SlotStatus.COMPLETED.equals(status);
    }


    public StationAdmin getChargingStation() {
        return chargingStation;
    }

    public void setChargingStation(StationAdmin chargingStation) {
        this.chargingStation = chargingStation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public ChargingType getChargingType() {
        return chargingType;
    }

    public void setChargingType(ChargingType chargingType) {
        this.chargingType = chargingType;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    public int getLevel1Count() {
        return level1Count;
    }

    public void setLevel1Count(int level1Count) {
        this.level1Count = level1Count;
    }

    public int getLevel2Count() {
        return level2Count;
    }

    public void setLevel2Count(int level2Count) {
        this.level2Count = level2Count;
    }

    public int getDcFastChargingCount() {
        return dcFastChargingCount;
    }

    public void setDcFastChargingCount(int dcFastChargingCount) {
        this.dcFastChargingCount = dcFastChargingCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public enum ChargingType {
        LEVEL_1,
        LEVEL_2,
        DC_FAST_CHARGING
    }

    public enum SlotStatus {
        AVAILABLE,
        BOOKED,
        COMPLETED
    }

    public void decreaseCount(ChargingType chargingType) {
        System.out.println("Decreasing count for ChargingType: " + chargingType);
        switch (chargingType) {
            case LEVEL_1:
                if (level1Count > 0) {
                    level1Count--;
                    System.out.println("Level1 Count decreased. New count: " + level1Count);
                } else {
                    System.out.println("Level1 Count is already zero. Cannot decrease.");
                }
                break;
            case LEVEL_2:
                if (level2Count > 0) {
                    level2Count--;
                    System.out.println("Level2 Count decreased. New count: " + level2Count);
                } else {
                    System.out.println("Level2 Count is already zero. Cannot decrease.");
                }
                break;
            case DC_FAST_CHARGING:
                if (dcFastChargingCount > 0) {
                    dcFastChargingCount--;
                    System.out.println("DC Fast Charging Count decreased. New count: " + dcFastChargingCount);
                } else {
                    System.out.println("DC Fast Charging Count is already zero. Cannot decrease.");
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown ChargingType: " + chargingType);
        }
    }

    private String formatDateTime(long epochMilli) {
        return java.time.format.DateTimeFormatter.ofPattern(DATE_FORMAT)
            .withZone(java.time.ZoneId.systemDefault())
            .format(java.time.Instant.ofEpochMilli(epochMilli));
    }
}

