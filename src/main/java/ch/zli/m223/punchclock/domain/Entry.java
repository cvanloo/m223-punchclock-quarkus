package ch.zli.m223.punchclock.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Entry {

    // NOTE(cvl): The classdiagram falsely depicts this as type User instead of Long.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime checkIn;

    @Column(nullable = false)
    private LocalDateTime checkOut;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Category category;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Facility facility;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}