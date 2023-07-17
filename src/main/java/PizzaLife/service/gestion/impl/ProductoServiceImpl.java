package PizzaLife.service.gestion.impl;

import PizzaLife.entity.gestion.ProductosEntity;
import PizzaLife.repository.ProductoRepository;
import PizzaLife.service.gestion.ProductoService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductoServiceImpl implements ProductoService{
    
    @Autowired
    private ProductoRepository repositorio;

    @Override
    public List<ProductosEntity> findAll() {
        return repositorio.findAll();
    }

    @Override
    public List<ProductosEntity> findAllCustom() {
        return repositorio.findAllCustom();
    }

    @Override
    public ProductosEntity add(ProductosEntity t) {
        return repositorio.save(t);
    }

    @Override
    public Optional<ProductosEntity> findById(Long id) {
        return repositorio.findById(id);
    }

    @Override
    public ProductosEntity update(ProductosEntity t) {
        ProductosEntity objProducto = repositorio.getById(t.getCodigo());
        BeanUtils.copyProperties(t,objProducto);
        return repositorio.save(objProducto);
    }

    @Override
    public ProductosEntity delete(ProductosEntity t) {
        ProductosEntity objProducto = repositorio.getById(t.getCodigo());
        objProducto.setEstado(false);
        return repositorio.save(objProducto);
    }

    @Override
    public ProductosEntity enabled(ProductosEntity t) {
        ProductosEntity objProducto = repositorio.getById(t.getCodigo());
        objProducto.setEstado(true);
        return repositorio.save(objProducto);
    }

   
    
}
