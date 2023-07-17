/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.restControllers;

import PizzaLife.entity.gestion.VentaEntity;
import PizzaLife.service.gestion.VentaService;
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
@RequestMapping("/venta")
public class VentaRestController {
     @Autowired
    private VentaService servicio;
    
    @GetMapping
    public List<VentaEntity> findAll(){
        return servicio.findAll();
    }
    @GetMapping("/custom")
    public List<VentaEntity>findAllCustom(){
        return servicio.findAllCustom();
    }
    @PostMapping
    public VentaEntity add(@RequestBody VentaEntity v){
        return servicio.add(v);
    }
    @GetMapping("/{id}")
    public Optional<VentaEntity>findById(@PathVariable long id){
        return servicio.findById(id);
    }
    @PutMapping("/{id}")
    public VentaEntity update(@PathVariable long id,@RequestBody VentaEntity v){
        v.getCodigo();
        return servicio.update(v);
    }
    @DeleteMapping("/{id}")
    public VentaEntity delete(@PathVariable long id){
        VentaEntity objUsuario = new VentaEntity();
        objUsuario.setCodigo(id);
        return servicio.delete(VentaEntity.builder().codigo(id).build());
    }
}
