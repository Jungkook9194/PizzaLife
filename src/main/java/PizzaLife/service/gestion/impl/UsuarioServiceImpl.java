/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.service.gestion.impl;

import PizzaLife.entity.gestion.UsuarioEntity;
import PizzaLife.repository.RolRepository;
import PizzaLife.repository.UsuarioRepository;
import PizzaLife.service.gestion.UsuarioService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author user
 */
@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    @Autowired
    private UsuarioRepository repositorio;

    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<UsuarioEntity> findAll() {
        return repositorio.findAll();
    }

    @Override
    public List<UsuarioEntity> findAllCustom() {
        return repositorio.findAllCustom();
    }

    @Override
    public UsuarioEntity add(UsuarioEntity t) {
        return repositorio.save(t);
    }

    @Override
    public Optional<UsuarioEntity> findById(Long id) {
        return repositorio.findById(id);
    }

    @Override
    public UsuarioEntity update(UsuarioEntity t) {
        UsuarioEntity objUsuario = repositorio.getById(t.getCodigo());
        BeanUtils.copyProperties(t, objUsuario);
        return repositorio.save(objUsuario);
    }

    @Override
    public UsuarioEntity delete(UsuarioEntity t) {
        UsuarioEntity objUsuario = repositorio.getById(t.getCodigo());
        objUsuario.setEstado(false);
        return repositorio.save(objUsuario);
    }

    @Override
    public UsuarioEntity enabled(UsuarioEntity t) {
        UsuarioEntity objUsuario = repositorio.getById(t.getCodigo());
        objUsuario.setEstado(true);
        return repositorio.save(objUsuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity user = repositorio.findByUsuario(username);
        if (user == null) {
            throw new DisabledException("Usuario no encontrado");
        }
        if (!user.isEstado()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getName().equals(username)) {
                throw new DisabledException("Tu cuenta está actualmente en uso y no puede ser deshabilitada");
            }
            throw new DisabledException("Tu cuenta ha sido deshabilitada, Comuníquese con un Administrador");
        }
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRol().getNombre()));
        UserDetails userDetails = new User(user.getUsuario(), user.getPassword(), roles);
        return userDetails;
    }
}
