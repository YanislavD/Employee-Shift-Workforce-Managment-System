package com.workforce.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "work_logs",
       uniqueConstraints = @UniqueConstraint(columnNames = "shift_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class WorkLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = false, unique = true)
    private Shift shift;

    @Column(nullable = false)
    private LocalDateTime actualStartTime;

    @Column(nullable = false)
    private LocalDateTime actualEndTime;

    @Column(nullable = false)
    private Long workedMinutes;

    @Column(length = 1000)
    private String notes;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void calculateWorkedMinutes() {
        if (actualStartTime != null && actualEndTime != null) {
            this.workedMinutes = java.time.Duration.between(actualStartTime, actualEndTime).toMinutes();
        }
    }
}
