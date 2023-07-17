/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.service.gestion.impl;

import PizzaLife.entity.gestion.DetalleVentaEntity;
import PizzaLife.repository.DetalleVentaRepository;
import PizzaLife.service.gestion.DetalleVentaService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 * @author user
 */
@Service
public class DetalleVentaServiceImp implements DetalleVentaService {

    @Autowired
    private DetalleVentaRepository repositorio;

    @Override
    public List<DetalleVentaEntity> findAll() {
        return repositorio.findAll();
    }

    @Override
    public List<DetalleVentaEntity> findAllCustom() {
        return repositorio.findAllCustom();
    }

    @Override
    public DetalleVentaEntity add(DetalleVentaEntity t) {
        return repositorio.save(t);
    }

    @Override
    public Optional<DetalleVentaEntity> findById(Long id) {
        return repositorio.findById(id);
    }

    @Override
    public DetalleVentaEntity update(DetalleVentaEntity t) {
        DetalleVentaEntity objProducto = repositorio.getById(t.getCodigo());
        BeanUtils.copyProperties(t, objProducto);
        return repositorio.save(objProducto);
    }

    @Override
    public DetalleVentaEntity delete(DetalleVentaEntity t) {
        DetalleVentaEntity objCliente = repositorio.getById(t.getCodigo());
        objCliente.setEstado(false);
        return repositorio.save(objCliente);
    }

    @Override
    public DetalleVentaEntity enabled(DetalleVentaEntity t) {
        DetalleVentaEntity objCliente = repositorio.getById(t.getCodigo());
        objCliente.setEstado(true);
        return repositorio.save(objCliente);
    }

  
}
