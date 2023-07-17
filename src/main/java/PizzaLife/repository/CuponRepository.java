/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package PizzaLife.repository;

import PizzaLife.entity.gestion.CuponEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author henrysebastianoteroalvarez
 */
@Repository
public interface CuponRepository extends JpaRepository<CuponEntity, Long> {

    @Query("select cu from CuponEntity cu where cu.estado=1")
    List<CuponEntity> findAllCustom();

    CuponEntity findByCupon(String cupon);

}
