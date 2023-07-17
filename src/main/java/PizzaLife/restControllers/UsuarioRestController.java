/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.restControllers;

import PizzaLife.entity.gestion.UsuarioEntity;
import PizzaLife.service.gestion.UsuarioService;
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
@RequestMapping("/usuario")
public class UsuarioRestController {
     @Autowired
    private UsuarioService servicio;
    
    @GetMapping
    public List<UsuarioEntity> findAll(){
        return servicio.findAll();
    }
    @GetMapping("/custom")
    public List<UsuarioEntity>findAllCustom(){
        return servicio.findAllCustom();
    }
    @PostMapping
    public UsuarioEntity add(@RequestBody UsuarioEntity u){
        return servicio.add(u);
    }
    @GetMapping("/{id}")
    public Optional<UsuarioEntity>findById(@PathVariable long id){
        return servicio.findById(id);
    }
    @PutMapping("/{id}")
    public UsuarioEntity update(@PathVariable long id,@RequestBody UsuarioEntity u){
        u.getCodigo();
        return servicio.update(u);
    }
    @DeleteMapping("/{id}")
    public UsuarioEntity delete(@PathVariable long id){
        UsuarioEntity objUsuario = new UsuarioEntity();
        objUsuario.setCodigo(id);
        return servicio.delete(UsuarioEntity.builder().codigo(id).build());
    }
}
