/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package PizzaLife.repository;

import PizzaLife.entity.gestion.DetalleVentaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVentaEntity, Long> {
    @Query("select d from DetalleVentaEntity d where d.estado=1")
    List<DetalleVentaEntity> findAllCustom();
        
}
