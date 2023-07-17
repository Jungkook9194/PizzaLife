/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.restControllers;

import PizzaLife.entity.gestion.CuponEntity;
import PizzaLife.service.gestion.CuponService;
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
 * @author henrysebastianoteroalvarez
 */
@RestController
@RequestMapping("/cupon")
public class CuponRestController {
    @Autowired
    private CuponService servicio;
    
    @GetMapping
    public List<CuponEntity> findAll(){
        return servicio.findAll();
    }
    @GetMapping("/custom")
    public List<CuponEntity>findAllCustom(){
        return servicio.findAllCustom();
    }
    @PostMapping
    public CuponEntity add(@RequestBody CuponEntity c){
        return servicio.add(c);
    }
    @GetMapping("/{id}")
    public Optional<CuponEntity>findById(@PathVariable long id){
        return servicio.findById(id);
    }
    @PutMapping("/{id}")
    public CuponEntity update(@PathVariable long id,@RequestBody CuponEntity c){
        c.getCodigo();
        return servicio.update(c);
    }
    @DeleteMapping("/{id}")
    public CuponEntity delete(@PathVariable long id){
        CuponEntity objCupon = new CuponEntity();
        objCupon.setCodigo(id);
        return servicio.delete(CuponEntity.builder().codigo(id).build());
    }
}
