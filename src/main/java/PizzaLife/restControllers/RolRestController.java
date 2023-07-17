/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.restControllers;

import PizzaLife.entity.gestion.RolEntity;
import PizzaLife.service.gestion.RolService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author user
 */
@RestController
@RequestMapping("/rol")
public class RolRestController {
    @Autowired
    private RolService servicio;
    
    @GetMapping
    public List<RolEntity> findAll(){
        return servicio.findAll();
    }
    @GetMapping("/custom")
    public List<RolEntity>findAllCustom(){
        return servicio.findAllCustom();
    }
    @PostMapping
    public RolEntity add(@RequestBody RolEntity r){
        return servicio.add(r);
    }
    @GetMapping("/{id}")
    public Optional<RolEntity>findById(@PathVariable long id){
        return servicio.findById(id);
    }
    @PutMapping("/{id}")
    public RolEntity update(@PathVariable long id,@RequestBody RolEntity r){
        r.getCodigo();
        return servicio.update(r);
    }
    @DeleteMapping("/{id}")
    public RolEntity delete(@PathVariable long id){
        RolEntity objRol = new RolEntity();
        objRol.setCodigo(id);
        return servicio.delete(RolEntity.builder().codigo(id).build());
    }
}
