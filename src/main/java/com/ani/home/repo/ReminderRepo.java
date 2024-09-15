

package com.ani.home.repo;

import com.ani.home.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReminderRepo extends JpaRepository<Reminder, Integer> {  // Changed to Integer
    Optional<Reminder> findByUserId(int userId);  // Changed to int
}
