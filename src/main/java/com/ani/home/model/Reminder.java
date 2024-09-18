

package com.ani.home.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "reminder_enabled")
    private boolean reminderEnabled;

    @Column(name = "days_before")
    private int daysBefore;

    @Column(name = "reminder_date")
    private LocalDate reminderDate;

    @Column(name = "reminder_time")
    private LocalTime reminderTime;  // Add this field

    // Constructors, getters, and setters
    public Reminder() {}

    public Reminder(User user, boolean reminderEnabled, int daysBefore, LocalDate reminderDate, LocalTime reminderTime) {
        this.user = user;
        this.reminderEnabled = reminderEnabled;
        this.daysBefore = daysBefore;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public boolean isReminderEnabled() { return reminderEnabled; }
    public void setReminderEnabled(boolean reminderEnabled) { this.reminderEnabled = reminderEnabled; }

    public int getDaysBefore() { return daysBefore; }
    public void setDaysBefore(int daysBefore) { this.daysBefore = daysBefore; }

    public LocalDate getReminderDate() { return reminderDate; }
    public void setReminderDate(LocalDate reminderDate) { this.reminderDate = reminderDate; }

    public LocalTime getReminderTime() { return reminderTime; }
    public void setReminderTime(LocalTime reminderTime) { this.reminderTime = reminderTime; }
}
