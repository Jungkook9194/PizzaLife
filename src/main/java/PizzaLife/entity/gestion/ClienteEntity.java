/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.entity.gestion;

import PizzaLife.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
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
@Entity(name="ClienteEntity") 
@Table(name="cliente", uniqueConstraints = @UniqueConstraint(columnNames = "dnicli")) 
public class ClienteEntity extends BaseEntity implements  Serializable {
    private static final long serialVersionUID=1L;
    @Id 
    @Column(name="codcli")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long codigo;
    @Column(name="dnicli",length = 8, nullable = false)
    private String dni;
    @Column(name="nomcli",length = 50, nullable = false)
    private String nombre;
    @Column(name="domicli", length = 100, nullable = false)
    private String domicilio;
    @Column(name="telcli", length = 9, nullable = true)
    private String telefono;
}