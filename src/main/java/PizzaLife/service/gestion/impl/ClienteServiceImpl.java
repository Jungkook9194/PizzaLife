/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.service.gestion.impl;

import PizzaLife.entity.gestion.ClienteEntity;
import PizzaLife.repository.ClienteRepository;
import PizzaLife.service.gestion.ClienteService;
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
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository repositorio;

    @Override
    public List<ClienteEntity> findAll() {
        return repositorio.findAll();
    }

    @Override
    public List<ClienteEntity> findAllCustom() {
        return repositorio.findAllCustom();
    }

    @Override
    public ClienteEntity add(ClienteEntity t) {
        return repositorio.save(t);
    }

    @Override
    public Optional<ClienteEntity> findById(Long id) {
        return repositorio.findById(id);
    }

    @Override
    public ClienteEntity update(ClienteEntity t) {
        ClienteEntity objCliente = repositorio.getById(t.getCodigo());
        BeanUtils.copyProperties(t, objCliente);
        return repositorio.save(objCliente);
    }

    @Override
    public ClienteEntity delete(ClienteEntity t) {
        ClienteEntity objCliente = repositorio.getById(t.getCodigo());
        objCliente.setEstado(false);
        return repositorio.save(objCliente);
    }

    @Override
    public ClienteEntity enabled(ClienteEntity t) {
        ClienteEntity objCliente = repositorio.getById(t.getCodigo());
        objCliente.setEstado(true);
        return repositorio.save(objCliente);
    }

   

}
