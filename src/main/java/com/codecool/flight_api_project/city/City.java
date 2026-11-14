package com.codecool.flight_api_project.city;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "City")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "city_name")
    private String cityName;

    //    @OneToMany(
    //               cascade = CascadeType.ALL,
    //               orphanRemoval = true,
    //               mappedBy = "city")
    //    private List<Airport> airportList;

}
