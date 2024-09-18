package com.ani.home.repoimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ani.home.model.Slot;
import com.ani.home.model.Slot.ChargingType;
import com.ani.home.model.Slot.SlotStatus;

import com.ani.home.repo.SlotRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class SlotRepoImpl implements SlotRepo {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Slot save(Slot slot) {
        if (slot != null) {
            if (slot.getId() != 0) {
                return em.merge(slot);
            } else {
                em.persist(slot);
                return slot;
            }
        } else {
            throw new IllegalArgumentException("Slot must not be null");
        }
    }

    @Override
    public void deleteById(int id) {
        Slot slot = em.find(Slot.class, id);
        if (slot != null) {
            em.remove(slot);
        } else {
            throw new RuntimeException("Slot not found with id: " + id);
        }
    }

    @Override
    public Optional<Slot> findById(int id) {
        Slot slot = em.find(Slot.class, id);
        return Optional.ofNullable(slot);
    }

    @Override
    public List<Slot> findAll() {
        String hql = "FROM Slot";
        return em.createQuery(hql, Slot.class).getResultList();
    }

    @Override
    public List<Slot> findByStationId(String stationId) {
        String jpql = "SELECT s FROM Slot s WHERE s.chargingStation.id = :stationId";
        TypedQuery<Slot> query = em.createQuery(jpql, Slot.class);
        query.setParameter("stationId", stationId);
        return query.getResultList();
    }

    @Override
    public List<Slot> findByStationIdNew(int stationId) {
        String jpql = "SELECT s FROM Slot s LEFT JOIN FETCH s.booking b LEFT JOIN FETCH b.user u WHERE s.chargingStation.id = :stationId";
        TypedQuery<Slot> query = em.createQuery(jpql, Slot.class);
        query.setParameter("stationId", stationId);
        return query.getResultList();
    }

    @Override
    public Slot findAvailableSlotByChargingType(ChargingType chargingType) {
        String jpql = "SELECT s FROM Slot s WHERE s.status = :status AND s.chargingType = :chargingType";
        TypedQuery<Slot> query = em.createQuery(jpql, Slot.class);
        query.setParameter("status", SlotStatus.AVAILABLE);
        query.setParameter("chargingType", chargingType);
        List<Slot> slots = query.getResultList();
        return slots.isEmpty() ? null : slots.get(0);
    }

    @Override
    public List<Slot> findByStationId(int stationId) {
        String jpql = "SELECT s FROM Slot s WHERE s.chargingStation.id = :stationId";
        TypedQuery<Slot> query = em.createQuery(jpql, Slot.class);
        query.setParameter("stationId", stationId);
        return query.getResultList();
    }
}
