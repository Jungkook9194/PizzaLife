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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
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
@Entity(name="CategoriaEntity") //define el nombre de la entidad
@Table(name="categoria", uniqueConstraints = @UniqueConstraint(columnNames = "nomcat")) //define el nombre de la tabla
public class CategoriaEntity extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;
    @Id //representa la clave primaria
    @Column(name="codcat")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//autoincremento
    private long codigo;
    @Column(name="nomcat",length = 100, nullable = false)
    private String nombre;
}
