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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
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
@Entity(name="UsuarioEntity") 
@Table(name="usuario") 
public class UsuarioEntity extends BaseEntity implements  Serializable {
    private static final long serialVersionUID=1L;
    @Id 
    @Column(name="coduse")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long codigo;
    @Column(name="nomuse",length = 50, nullable = false)
    private String nombre;
    @Column(name="user", length=40, nullable = false, unique=true)
    private String usuario;
    @Column(name="password", length = 200, nullable = false)
    private String password;
    @Column(name="coruser", length = 200, nullable = false, unique=true)
    private String correo;
    @Column(name="dni", length=8, nullable = false, unique=true)
    private String dni;
    @Column(name="codereset", nullable = true)
    private String codereset;
    @ManyToOne //relacion de uno a muchos
    @JoinColumn(name="codrol",nullable = false)
    private RolEntity rol;
}
