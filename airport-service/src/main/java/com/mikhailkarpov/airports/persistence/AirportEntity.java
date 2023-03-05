package com.mikhailkarpov.airports.persistence;

import com.mikhailkarpov.airports.api.Location;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "airports")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AirportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true)
    private String code;

    private String city;

    @Embedded
    private Location location;


    @Builder
    public AirportEntity(@NonNull String code, @NonNull String city, @NonNull Location location) {
        this.code = code;
        this.city = city;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AirportEntity that = (AirportEntity) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AirportEntity{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", city='" + city + '\'' +
                ", location=" + location +
                '}';
    }
}
