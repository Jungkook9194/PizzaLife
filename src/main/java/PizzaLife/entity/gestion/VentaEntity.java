/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.entity.gestion;

import PizzaLife.entity.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author henrysebastianoteroalvarez
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "VentaEntity")
@Table(name = "venta")
public class VentaEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "codven")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long codigo;
    @Column(name = "fecha", length = 50, nullable = false)
    private LocalDate fechaventa = LocalDate.now();
    @Column(name = "total")
    private Double total;
    @ManyToOne //relacion de uno a muchos
    @JoinColumn(name = "codcli", nullable = false)
    private ClienteEntity cliente;
    @ManyToOne //relacion de uno a muchos
    @JoinColumn(name = "coduse", nullable = false)
    private UsuarioEntity usuario;
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVentaEntity> detallesVenta;

}
