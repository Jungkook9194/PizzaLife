/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.restControllers;

import PizzaLife.entity.gestion.DetalleVentaEntity;
import PizzaLife.service.gestion.DetalleVentaService;
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
@RequestMapping("/detalleventa")
public class DetalleVentaRestController {
    @Autowired
    private DetalleVentaService servicio;
    
    @GetMapping
    public List<DetalleVentaEntity> findAll(){
        return servicio.findAll();
    }
    @GetMapping("/custom")
    public List<DetalleVentaEntity>findAllCustom(){
        return servicio.findAllCustom();
    }
    @PostMapping
    public DetalleVentaEntity add(@RequestBody DetalleVentaEntity p){
        return servicio.add(p);
    }
    @GetMapping("/{id}")
    public Optional<DetalleVentaEntity>findById(@PathVariable long id){
        return servicio.findById(id);
    }
    @PutMapping("/{id}")
    public DetalleVentaEntity update(@PathVariable long id,@RequestBody DetalleVentaEntity p){
        p.getCodigo();
        return servicio.update(p);
    }
    @DeleteMapping("/{id}")
    public DetalleVentaEntity delete(@PathVariable long id){
        DetalleVentaEntity objProducto = new DetalleVentaEntity();
        objProducto.setCodigo(id);
        return servicio.delete(DetalleVentaEntity.builder().codigo(id).build());
    }
}
