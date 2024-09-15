

package com.ani.home.service;

import com.ani.home.model.Reminder;

import java.util.Optional;

public interface ReminderService {
    Optional<Reminder> getReminderByUserId(int userId);
    Reminder createOrUpdateReminder(int userId, Reminder reminder);
	void sendReminders();
}
