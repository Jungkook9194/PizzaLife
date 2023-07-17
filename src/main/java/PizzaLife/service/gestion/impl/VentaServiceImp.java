/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.service.gestion.impl;

import PizzaLife.entity.gestion.VentaEntity;
import PizzaLife.repository.VentaRepository;
import PizzaLife.service.gestion.VentaService;
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
public class VentaServiceImp implements VentaService {

    @Autowired
    private VentaRepository repositorio;

    @Override
    public List<VentaEntity> findAll() {
        return repositorio.findAll();
    }

    @Override
    public List<VentaEntity> findAllCustom() {
        return repositorio.findAllCustom();
    }

    @Override
    public VentaEntity add(VentaEntity t) {
        return repositorio.save(t);
    }

    @Override
    public Optional<VentaEntity> findById(Long id) {
        return repositorio.findById(id);
    }

    @Override
    public VentaEntity update(VentaEntity t) {
        VentaEntity objUsuario = repositorio.getById(t.getCodigo());
        BeanUtils.copyProperties(t, objUsuario);
        return repositorio.save(objUsuario);
    }

    @Override
    public VentaEntity delete(VentaEntity t) {
        VentaEntity objUsuario = repositorio.getById(t.getCodigo());
        objUsuario.setEstado(false);
        return repositorio.save(objUsuario);
    }

    @Override
    public VentaEntity enabled(VentaEntity t) {
        VentaEntity objUsuario = repositorio.getById(t.getCodigo());
        objUsuario.setEstado(true);
        return repositorio.save(objUsuario);
    }

}
