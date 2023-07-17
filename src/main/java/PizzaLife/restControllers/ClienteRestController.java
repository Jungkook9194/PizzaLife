/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.restControllers;

import PizzaLife.service.gestion.ClienteService;
import PizzaLife.entity.gestion.ClienteEntity;
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
@RequestMapping("/cliente")
public class ClienteRestController {
    @Autowired
    private ClienteService servicio;
    
    @GetMapping
    public List<ClienteEntity> findAll(){
        return servicio.findAll();
    }
    @GetMapping("/custom")
    public List<ClienteEntity>findAllCustom(){
        return servicio.findAllCustom();
    }
    @PostMapping
    public ClienteEntity add(@RequestBody ClienteEntity c){
        return servicio.add(c);
    }
    @GetMapping("/{id}")
    public Optional<ClienteEntity>findById(@PathVariable long id){
        return servicio.findById(id);
    }
    @PutMapping("/{id}")
    public ClienteEntity update(@PathVariable long id,@RequestBody ClienteEntity c){
        c.getCodigo();
        return servicio.update(c);
    }
    @DeleteMapping("/{id}")
    public ClienteEntity delete(@PathVariable long id){
        ClienteEntity objCliente = new ClienteEntity();
        objCliente.setCodigo(id);
        return servicio.delete(ClienteEntity.builder().codigo(id).build());
    }
}
