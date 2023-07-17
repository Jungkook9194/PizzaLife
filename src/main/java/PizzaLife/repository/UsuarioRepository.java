/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package PizzaLife.repository;

import PizzaLife.entity.gestion.RolEntity;
import PizzaLife.entity.gestion.UsuarioEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity , Long> {
    @Query("select u from UsuarioEntity u where u.estado=1")
    List<UsuarioEntity> findAllCustom();
    UsuarioEntity findByUsuario(String nombre);
    List<UsuarioEntity> findByRol(RolEntity rol);
    UsuarioEntity findByCorreo(String correo);
}
