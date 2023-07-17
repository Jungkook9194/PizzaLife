/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author henrysebastianoteroalvarez
 */
@SuperBuilder // herencia
@NoArgsConstructor // metodo constructor sin parametros
@AllArgsConstructor //metodo constructor con parametros
@Data //metodos Get y Set
@MappedSuperclass //mapear la herencia
public class BaseEntity {
    @Column(name="estado", nullable = false) //nombre de la columna en la tabla
    private boolean estado = true;
}
