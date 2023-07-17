/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.restControllers;
import PizzaLife.entity.gestion.ProductosEntity;
import PizzaLife.service.gestion.ProductoService;
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

@RestController
@RequestMapping("/producto")
public class ProductoRestController {
    @Autowired
    private ProductoService servicio;
    
    @GetMapping
    public List<ProductosEntity> findAll(){
        return servicio.findAll();
    }
    @GetMapping("/custom")
    public List<ProductosEntity>findAllCustom(){
        return servicio.findAllCustom();
    }
    @PostMapping
    public ProductosEntity add(@RequestBody ProductosEntity p){
        return servicio.add(p);
    }
    @GetMapping("/{id}")
    public Optional<ProductosEntity>findById(@PathVariable long id){
        return servicio.findById(id);
    }
    @PutMapping("/{id}")
    public ProductosEntity update(@PathVariable long id,@RequestBody ProductosEntity p){
        p.getCodigo();
        return servicio.update(p);
    }
    @DeleteMapping("/{id}")
    public ProductosEntity delete(@PathVariable long id){
        ProductosEntity objProducto = new ProductosEntity();
        objProducto.setCodigo(id);
        return servicio.delete(ProductosEntity.builder().codigo(id).build());
    }
    
}
