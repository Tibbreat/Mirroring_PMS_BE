package sep490.g13.pms_be.entities;

import jakarta.persistence.*;
import lombok.*;
import sep490.g13.pms_be.utils.enums.AttendanceStatusEnums;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceLog extends Auditable<String>  {
    @ManyToOne
    private Children children;

    private LocalDate attendanceDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date morningBoardingTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date morningAlightingTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date afternoonBoardingTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date afternoonAlightingTime;

    @Enumerated(EnumType.STRING)
    private AttendanceStatusEnums status;

    @Lob
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Classes classes;
}
