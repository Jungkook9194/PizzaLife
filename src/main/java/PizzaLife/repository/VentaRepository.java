/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package PizzaLife.repository;

import PizzaLife.entity.gestion.VentaEntity;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public interface VentaRepository extends JpaRepository<VentaEntity, Long> {

    @Query("select v from VentaEntity v where v.estado=1")
    List<VentaEntity> findAllCustom();
}
