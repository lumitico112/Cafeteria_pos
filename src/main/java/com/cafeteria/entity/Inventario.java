package com.cafeteria.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = false)
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    @EqualsAndHashCode.Include
    private Integer idInventario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @ToString.Exclude
    private Producto producto;

    @Column(name = "cantidad_actual")
    private Integer cantidadActual = 0;

    @Column(name = "stock_minimo")
    private Integer stockMinimo = 0;

    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;
}
