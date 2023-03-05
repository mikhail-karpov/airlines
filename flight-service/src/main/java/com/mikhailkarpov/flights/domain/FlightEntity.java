package com.mikhailkarpov.flights.domain;

import com.mikhailkarpov.flights.api.FlightStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "flights")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    private String code;

    private String origin;

    private String destination;

    @Enumerated(EnumType.STRING)
    private FlightStatus status;


    @Builder
    private FlightEntity(@NonNull String code,
                         @NonNull String origin,
                         @NonNull String destination,
                         @NonNull FlightStatus status) {
        this.code = code;
        this.origin = origin;
        this.destination = destination;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlightEntity that = (FlightEntity) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FlightEntity{" +
                "id=" + id +
                ", version=" + version +
                ", code='" + code + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", status=" + status +
                '}';
    }
}
