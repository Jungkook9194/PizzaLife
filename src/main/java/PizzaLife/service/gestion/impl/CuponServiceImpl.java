/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.service.gestion.impl;

import PizzaLife.entity.gestion.CuponEntity;
import PizzaLife.repository.CuponRepository;
import PizzaLife.service.gestion.CuponService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author henrysebastianoteroalvarez
 */
@Service
public class CuponServiceImpl implements CuponService {

    @Autowired
    private CuponRepository repositorio;

    @Override
    public List<CuponEntity> findAll() {
        return repositorio.findAll();
    }

    @Override
    public List<CuponEntity> findAllCustom() {
        return repositorio.findAllCustom();
    }

    @Override
    public CuponEntity add(CuponEntity t) {
        return repositorio.save(t);
    }

    @Override
    public Optional<CuponEntity> findById(Long id) {
        return repositorio.findById(id);
    }

    @Override
    public CuponEntity update(CuponEntity t) {
        CuponEntity objCupon = repositorio.getById(t.getCodigo());
        BeanUtils.copyProperties(t, objCupon);
        return repositorio.save(objCupon);
    }

    @Override
    public CuponEntity delete(CuponEntity t) {
        CuponEntity objCupon = repositorio.getById(t.getCodigo());
        objCupon.setEstado(false);
        return repositorio.save(objCupon);
    }

    @Override
    public CuponEntity enabled(CuponEntity t) {
        CuponEntity objCupon = repositorio.getById(t.getCodigo());
        objCupon.setEstado(true);
        return repositorio.save(objCupon);
    }

}
