/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.controller;

import PizzaLife.entity.gestion.CategoriaEntity;
import PizzaLife.entity.gestion.ClienteEntity;
import PizzaLife.entity.gestion.CuponEntity;
import PizzaLife.entity.gestion.DetalleVentaEntity;
import PizzaLife.entity.gestion.ProductosEntity;
import PizzaLife.entity.gestion.RolEntity;
import PizzaLife.entity.gestion.UsuarioEntity;
import PizzaLife.entity.gestion.VentaEntity;
import PizzaLife.entity.gestion.VerificationCodeGenerator;
import PizzaLife.repository.CuponRepository;
import PizzaLife.repository.ProductoRepository;
import PizzaLife.repository.UsuarioRepository;
import PizzaLife.seguridad.Seguridad;
import PizzaLife.service.gestion.CategoriaService;
import PizzaLife.service.gestion.ClienteService;
import PizzaLife.service.gestion.CuponService;
import PizzaLife.service.gestion.DetalleVentaService;
import PizzaLife.service.gestion.ProductoService;
import PizzaLife.service.gestion.RolService;
import PizzaLife.service.gestion.UsuarioService;
import PizzaLife.service.gestion.VentaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author henrysebastianoteroalvarez
 */
@Controller
public class Controllers {

    //Max - Min
    private static final int MAX_PRODUCTOS_EN_CARRITO = 10;

    private static final double MONTO_MINIMO_CUPON = 59.0;

    //Carrito
    private List<ProductosEntity> Carrito = new ArrayList<>();

    @Autowired
    private UsuarioService serviciousuario;

    @Autowired
    private UsuarioRepository repositoriousuario;

    @Autowired
    private CuponRepository repositoriocupon;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @GetMapping("/agregar/{id}")
    public String agregarcarrito(@PathVariable("id") long id, Model modelo) {

        modelo.addAttribute("producto", servicioproducto.findAllCustom());
        ProductosEntity producto = servicioproducto.findById(id).get();
        boolean itemFound = false;
        for (ProductosEntity cart : Carrito) {
            if (cart.getCodigo() == id) {
                itemFound = true;
                break;
            }
        }
        if (Carrito.size() < MAX_PRODUCTOS_EN_CARRITO) {
            if (!itemFound) {
                Carrito.add(producto);
                return "redirect:/mostrarproducto?agregado";
            }
        }
        return "redirect:/mostrarproducto?noagregado";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarcarrito(@PathVariable("id") long id) {
        ProductosEntity producto = servicioproducto.findById(id).get();
        if (producto != null) {
            Carrito.remove(producto);
        }
        return "redirect:/mostrarpedidos?removido";
    }

    @GetMapping("/vaciar")
    public String vaciar(Model modelo, HttpSession session) {
        Carrito.clear();
        return "redirect:/mostrarproducto?vaciar";
    }

    //Seguridad
    @Autowired
    private EntityManager entityMaganer;

    @Autowired
    private Seguridad seguridadConfig;

    //Templates 
    @GetMapping("/login")
    public String MostrarLogin() {
        return "login";
    }

    @GetMapping("/graficos")
    public String Graficos(Model model) {
        return "graficos";
    }

    @GetMapping("/email")
    public String Mensajeria() {
        return "email";
    }

    @GetMapping("/perfilusuario/{username}")
    public String Perfil(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UsuarioEntity user = repositoriousuario.findByUsuario(username);
        model.addAttribute("username", user);
        return "/usuario/perfilusuario";

    }

    @PostMapping("/verificarcontrasena")
    public String verifyPassword(
            @RequestParam("contrasenaActual") String contrasenaActual,
            RedirectAttributes redirectAttributes,
            Authentication authentication
    ) {
        String username = authentication.getName();
        UsuarioEntity user = repositoriousuario.findByUsuario(username);

        if (seguridadConfig.passwordEncoder().matches(contrasenaActual, user.getPassword())) {
            redirectAttributes.addFlashAttribute("contrasenaValida", true);
        } else {
            redirectAttributes.addFlashAttribute("contrasenaValida", false);
        }

        return "redirect:/perfilusuario/" + username;
    }

    @PostMapping("/cambiarcontrasena")
    public String changePassword(
            @RequestParam("contrasenaNueva") String contrasenaNueva,
            @RequestParam("correo") String correo,
            RedirectAttributes redirectAttributes,
            Authentication authentication
    ) {
        String username = authentication.getName();

        UsuarioEntity usuario = repositoriousuario.findByCorreo(correo);
        if (usuario != null) {
            String passwordCodificado = seguridadConfig.passwordEncoder().encode(contrasenaNueva);
            usuario.setPassword(passwordCodificado);
            repositoriousuario.save(usuario);
            return "redirect:/perfilusuario/" + username + "?cambiado";
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró el usuario.");
        }

        return "redirect:/perfilusuario/" + username;
    }

    @GetMapping("/principal")
    public String MostrarPrincipal(Model modelo, Principal principal) {
        String username = principal.getName();
        UsuarioEntity usuario = repositoriousuario.findByUsuario(username);
        modelo.addAttribute("username", username);
        modelo.addAttribute("role", usuario.getRol());
        return "principal";
    }

    @GetMapping("/template")
    public String Template(Model modelo, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        UsuarioEntity usuario = repositoriousuario.findByUsuario(username);
        boolean esAdministrador = usuario.getRol().getNombre().equals("Administrador");
        modelo.addAttribute("esAdministrador", esAdministrador);
        return "template";
    }

    //Crud Cliente
    @Autowired
    private ClienteService serviciocliente;

    @GetMapping("/mostrarcliente")
    public String MostrarCliente(Model modelo) {
        modelo.addAttribute("cliente", serviciocliente.findAllCustom());
        return "/clientes/mostrarcliente";
    }

    @GetMapping("/mostrarhabilitarcliente")
    public String MostrarHabilitarCliente(Model modelo) {
        modelo.addAttribute("cliente", serviciocliente.findAll());
        return "/clientes/habilitarcliente";
    }

    @GetMapping("/actualizarcliente/{id}")
    public String MostrarActualizarCliente(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("cliente", serviciocliente.findById(id).get());
        return "/clientes/actualizarcliente";
    }

    @PostMapping("/registrarcliente")
    public String RegistroCliente(@Valid @ModelAttribute("cliente") ClienteEntity c, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "clientes/mostrarcliente";
            }
            serviciocliente.add(c);
            return "redirect:/mostrarcliente?correcto";
        } catch (Exception e) {
            return "redirect:/mostrarcliente?incorrecto";
        }
    }

    @PostMapping("/actualizarcliente/{id}")
    public String ActualizarCliente(@PathVariable Long id, @Valid @ModelAttribute("cliente") ClienteEntity c, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "clientes/actualizarcliente";
            }
            serviciocliente.update(c);
            return "redirect:/mostrarcliente?actualizado";
        } catch (Exception e) {
            return "redirect:/mostrarcliente?noactualizado";
        }
    }

    @GetMapping("/deshabilitarcliente/{id}")
    public String DeshabilitarCliente(@PathVariable Long id, Model modelo) {
        try {
            ClienteEntity objCliente = serviciocliente.findById(id).get();
            serviciocliente.delete(objCliente);
            return "redirect:/mostrarcliente?deshabilitado";
        } catch (Exception e) {
            return "redirect:/mostrarcliente?nodeshabilitado";
        }
    }

    @GetMapping("/habilitarcliente/{id}")
    public String HabilitarCliente(@PathVariable Long id, Model modelo) {
        try {
            ClienteEntity objCliente = serviciocliente.findById(id).get();
            serviciocliente.enabled(objCliente);
            return "redirect:/mostrarcliente?habilitado";
        } catch (Exception e) {
            return "redirect:/mostrarcliente?nohabilitado";

        }
    }

    @GetMapping("/eliminarcliente/{id}")
    public String EliminarCliente(@PathVariable Long id, Model modelo) {
        try {
            ClienteEntity objCliente = serviciocliente.findById(id).get();
            serviciocliente.delete(objCliente);
            return "redirect:/mostrarcliente?eliminado";
        } catch (Exception e) {
            return "redirect:/mostrarcliente?noeliminado";
        }

    }

    @ModelAttribute("cliente")
    public ClienteEntity ModeloCliente() {
        return new ClienteEntity();
    }

    //Crud Categoria
    @Autowired
    private CategoriaService serviciocategoria;

    @GetMapping("/mostrarcategoria")
    public String MostrarCategoria(Model modelo, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        UsuarioEntity usuario = repositoriousuario.findByUsuario(username);
        boolean esAdministrador = usuario.getRol().getNombre().equals("Administrador");
        modelo.addAttribute("esAdministrador", esAdministrador);
        List<CategoriaEntity> categorias = serviciocategoria.findAllCustom();
        modelo.addAttribute("categoria", categorias);
        return "/categoria/mostrarcategoria";
    }

    @GetMapping("/mostrarhabilitarcategoria")
    public String MostrarHabilitarCategoria(Model modelo) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarcategoria?denegado";
        }
        modelo.addAttribute("categoria", serviciocategoria.findAll());
        return "/categoria/habilitarcategoria";
    }

    @GetMapping("/actualizarcategoria/{id}")
    public String MostrarActualizarCategoria(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarcategoria?denegado";
        }
        modelo.addAttribute("categoria", serviciocategoria.findById(id).get());
        return "/categoria/actualizarcategoria";
    }

    @PostMapping("/registrarcategoria")
    public String RegistroCategoria(@Valid @ModelAttribute("categoria") CategoriaEntity c, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "categoria/mostrarcategoria";
            }
            serviciocategoria.add(c);
            return "redirect:/mostrarcategoria?correcto";
        } catch (Exception e) {
            return "redirect:/mostrarcategoria?incorrecto";
        }
    }

    @PostMapping("/actualizarcategoria/{id}")
    public String ActualizarCategoria(@PathVariable Long id, @Valid @ModelAttribute("categoria") CategoriaEntity c, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "categoria/actualizarcategoria";
            }
            serviciocategoria.update(c);
            return "redirect:/mostrarcategoria?actualizado";
        } catch (Exception e) {
            return "redirect:/mostrarcategoria?noactualizado";
        }
    }

    @GetMapping("/deshabilitarcategoria/{id}")
    public String DeshabilitarCategoria(@PathVariable Long id, Model modelo, @AuthenticationPrincipal UserDetails userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarcategoria?denegado";
        }
        try {
            CategoriaEntity objCategoria = serviciocategoria.findById(id).get();
            serviciocategoria.delete(objCategoria);
            return "redirect:/mostrarcategoria?deshabilitado";
        } catch (Exception e) {
            return "redirect:/mostrarcategoria?nodeshabilitado";
        }

    }

    @GetMapping("/habilitarcategoria/{id}")
    public String HabilitarCategoria(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarcategoria?denegado";
        }
        try {
            CategoriaEntity objCategoria = serviciocategoria.findById(id).get();
            serviciocategoria.enabled(objCategoria);
            List<ProductosEntity> productos = repositorioproducto.findByCategoria(objCategoria);
            for (ProductosEntity producto : productos) {
                producto.setEstado(true);
                servicioproducto.add(producto);
            }
            return "redirect:/mostrarcategoria?habilitado";
        } catch (Exception e) {
            return "redirect:/mostrarcategoria?nohabilitado";

        }
    }
    @Autowired
    private ProductoRepository repositorioproducto;

    @GetMapping("/eliminarcategoria/{id}")
    public String EliminarCategoria(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarcategoria?denegado";
        }
        try {
            CategoriaEntity objCategoria = serviciocategoria.findById(id).orElseThrow();
            objCategoria.setEstado(false);
            serviciocategoria.add(objCategoria);
            List<ProductosEntity> productos = repositorioproducto.findByCategoria(objCategoria);
            for (ProductosEntity producto : productos) {
                producto.setEstado(false);
                servicioproducto.add(producto);
            }
            return "redirect:/mostrarcategoria?eliminado";
        } catch (Exception e) {
            return "redirect:/mostrarcategoria?noeliminado";
        }
    }

    @ModelAttribute("categoria")
    public CategoriaEntity ModeloCategoria() {
        return new CategoriaEntity();
    }

    //Crud Roles
    @Autowired
    private RolService serviciorol;

    @GetMapping("/mostrarrol")
    public String MostrarRol(Model modelo, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        UsuarioEntity usuario = repositoriousuario.findByUsuario(username);
        boolean esAdministrador = usuario.getRol().getNombre().equals("Administrador");
        modelo.addAttribute("esAdministrador", esAdministrador);
        modelo.addAttribute("rol", serviciorol.findAllCustom());
        return "/rol/mostrarrol";
    }

    @GetMapping("/mostrarhabilitarrol")
    public String MostrarHabilitarRol(Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarrol?denegado";
        }
        modelo.addAttribute("rol", serviciorol.findAll());
        return "/rol/habilitarrol";
    }

    @GetMapping("/actualizarrol/{id}")
    public String MostrarActualizarRol(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarrol?denegado";
        }
        modelo.addAttribute("rol", serviciorol.findById(id).get());
        return "/rol/actualizarrol";
    }

    @PostMapping("/registrarrol")
    public String RegistroRol(@Valid @ModelAttribute("rol") RolEntity r, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "rol/mostrarrol";
            }
            serviciorol.add(r);
            return "redirect:/mostrarrol?correcto";
        } catch (Exception e) {
            return "redirect:/mostrarrol?incorrecto";
        }
    }

    @PostMapping("/actualizarrol/{id}")
    public String ActualizarRol(@PathVariable Long id, @Valid @ModelAttribute("rol") RolEntity r, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "rol/actualizarrol";
            }
            serviciorol.update(r);
            return "redirect:/mostrarrol?actualizado";
        } catch (Exception e) {
            return "redirect:/mostrarrol?noactualizado";
        }
    }

    @GetMapping("/deshabilitarrol/{id}")
    public String DeshabilitarRol(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarrol?denegado";
        }
        try {
            RolEntity objRol = serviciorol.findById(id).get();
            serviciorol.delete(objRol);
            return "redirect:/mostrarrol?deshabilitado";
        } catch (Exception e) {
            return "redirect:/mostrarrol?nodeshabilitado";
        }
    }

    @GetMapping("/habilitarrol/{id}")
    public String HabilitarRol(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarrol?denegado";
        }
        try {
            RolEntity objRol = serviciorol.findById(id).get();
            serviciorol.enabled(objRol);
            return "redirect:/mostrarrol?habilitado";
        } catch (Exception e) {
            return "redirect:/mostrarrol?nohabilitado";

        }
    }

    @GetMapping("/eliminarrol/{id}")
    public String EliminarRol(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarrol?denegado";
        }
        try {
            RolEntity objRol = serviciorol.findById(id).orElseThrow();
            List<UsuarioEntity> usuariosConRol = repositoriousuario.findByRol(objRol);
            for (UsuarioEntity usuario : usuariosConRol) {
                if (usuario.isEstado()) {
                    return "redirect:/mostrarrol?rol";
                }
            }
            serviciorol.delete(objRol);
            return "redirect:/mostrarrol?eliminado";
        } catch (Exception e) {
            return "redirect:/mostrarrol?noeliminado";
        }
    }

    @ModelAttribute("rol")
    public RolEntity ModeloRol() {
        return new RolEntity();
    }

    //Crud Usuarios
    @GetMapping("/mostrarusuario")
    public String MostrarUsuario(Model modelo, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        UsuarioEntity usuario = repositoriousuario.findByUsuario(username);
        boolean esAdministrador = usuario.getRol().getNombre().equals("Administrador");
        modelo.addAttribute("esAdministrador", esAdministrador);
        modelo.addAttribute("usuario", serviciousuario.findAllCustom());
        modelo.addAttribute("rol", serviciorol.findAllCustom());
        UsuarioEntity user = repositoriousuario.findByUsuario(username);
        modelo.addAttribute("username", user);
        return "/usuario/mostrarusuario";
    }

    @GetMapping("/mostrarhabilitarusuario")
    public String MostrarHabilitarUsuario(Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarusuario?denegado";
        }
        modelo.addAttribute("usuario", serviciousuario.findAll());
        return "/usuario/habilitarusuario";
    }

    @GetMapping("/actualizarusuario/{id}")
    public String MostrarActualizarUsuario(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarusuario?denegado";
        }
        modelo.addAttribute("rol", serviciorol.findAllCustom());
        modelo.addAttribute("usuario", serviciousuario.findById(id).get());
        return "/usuario/actualizarusuario";
    }

    @PostMapping("/registrarusuario")
    public String RegistroUsuario(@Valid @ModelAttribute("usuario") UsuarioEntity u, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "usuario/mostrarusuario";
            }
            String passwordcodificado = seguridadConfig.passwordEncoder().encode(u.getPassword());
            u.setPassword(passwordcodificado);
            serviciousuario.add(u);
            return "redirect:/mostrarusuario?correcto";
        } catch (Exception e) {
            return "redirect:/mostrarusuario?incorrecto";
        }
    }

    @PostMapping("/actualizarusuario/{id}")
    public String actualizarUsuario(@PathVariable Long id, Model model, @Valid @ModelAttribute("usuario") UsuarioEntity usuarioActualizado, BindingResult result, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            UsuarioEntity usuarioExistente = serviciousuario.findById(id).get();
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
            usuarioExistente.setDni(usuarioActualizado.getDni());
            usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
            if (!usuarioExistente.getUsuario().equals(usuarioActualizado.getUsuario())) {
                UsuarioEntity usuarioDuplicado = repositoriousuario.findByUsuario(usuarioActualizado.getUsuario());
                if (usuarioDuplicado != null) {
                    model.addAttribute("usuarioDuplicado", true);
                    return "redirect:/actualizarusuario/{id}?usuariod";
                }
                usuarioExistente.setUsuario(usuarioActualizado.getUsuario());
                if (authentication != null) {
                    new SecurityContextLogoutHandler().logout(request, response, authentication);
                }
            }
            usuarioExistente.setRol(usuarioActualizado.getRol());
            usuarioExistente.setEstado(usuarioActualizado.isEstado());

            if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                String passwordCodificado = seguridadConfig.passwordEncoder().encode(usuarioActualizado.getPassword());
                usuarioExistente.setPassword(passwordCodificado);
            }
            serviciousuario.update(usuarioExistente);
            return "redirect:/mostrarusuario?actualizado";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/mostrarusuario?noactualizado";
        }
    }

    @GetMapping("/deshabilitarusuario/{id}")
    public String DeshabilitarUsuario(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarusuario?denegado";
        }
        try {
            UsuarioEntity objUsuario = serviciousuario.findById(id).get();
            serviciousuario.delete(objUsuario);
            return "redirect:/mostrarusuario?deshabilitado";
        } catch (Exception e) {
            return "redirect:/mostrarusuario?nodeshabilitado";
        }
    }

    @GetMapping("/habilitarusuario/{id}")
    public String HabilitarUsuario(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarusuario?denegado";
        }
        try {
            UsuarioEntity objUsuario = serviciousuario.findById(id).orElseThrow();

            if (objUsuario.getRol().isEstado()) {
                serviciousuario.enabled(objUsuario);
                return "redirect:/mostrarusuario?habilitado";
            } else {
                return "redirect:/mostrarusuario?nou";
            }
        } catch (Exception e) {
            return "redirect:/mostrarusuario?nohabilitado";
        }
    }

    @GetMapping("/eliminarusuario/{id}")
    public String EliminarUsuario(@PathVariable Long id, Model modelo) {
        Authentication rol = SecurityContextHolder.getContext().getAuthentication();
        if (!rol.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarusuario?denegado";
        }
        try {
            UsuarioEntity objUsuario = serviciousuario.findById(id).get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            if (username.equals(objUsuario.getUsuario())) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }
            serviciousuario.delete(objUsuario);
            return "redirect:/mostrarusuario?eliminado";
        } catch (Exception e) {
            return "redirect:/mostrarusuario?noeliminado";
        }
    }

    @ModelAttribute("usuario")
    public UsuarioEntity ModeloUsuario() {
        return new UsuarioEntity();
    }

    //Crud Producto
    @Autowired
    private ProductoService servicioproducto;

    @GetMapping("/mostrarproducto")
    public String MostrarProducto(Model modelo, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        UsuarioEntity usuario = repositoriousuario.findByUsuario(username);
        boolean esAdministrador = usuario.getRol().getNombre().equals("Administrador");
        modelo.addAttribute("esAdministrador", esAdministrador);
        modelo.addAttribute("producto", servicioproducto.findAllCustom());
        modelo.addAttribute("categoria", serviciocategoria.findAllCustom());
        modelo.addAttribute("carrito", Carrito);
        modelo.addAttribute("cliente", serviciocliente.findAllCustom());
        modelo.addAttribute("usuario", serviciousuario.findAllCustom());
        return "/producto/mostrarproducto";
    }

    @GetMapping("/habilitarproducto")
    public String MostrarHabilitarProducto(Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarproducto?denegado";
        }
        modelo.addAttribute("producto", servicioproducto.findAll());
        modelo.addAttribute("categoria", serviciocategoria.findAllCustom());
        return "/producto/habilitarproducto";
    }

    @GetMapping("/actualizarproducto/{id}")
    public String MostrarActualizarProducto(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarproducto?denegado";
        }
        modelo.addAttribute("categoria", serviciocategoria.findAllCustom());
        modelo.addAttribute("producto", servicioproducto.findById(id).get());
        return "/producto/actualizarproducto";
    }

    @PostMapping("/registrarproducto")
    public String RegistroProducto(@Valid @ModelAttribute("producto") ProductosEntity p, BindingResult result, @RequestParam("file") MultipartFile imagen, Model model) throws IOException {
        if (!imagen.isEmpty()) {
            java.nio.file.Path directorioImagen = Paths.get("src//main//resources//static/images");
            String rutaAbsoluta = directorioImagen.toFile().getAbsolutePath();

            try {
                byte[] bytesImg = imagen.getBytes();
                java.nio.file.Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + imagen.getOriginalFilename());
                Files.write(rutaCompleta, bytesImg);
                p.setImagen(imagen.getOriginalFilename());
                servicioproducto.add(p);
                return "redirect:/mostrarproducto?correcto";
            } catch (Exception e) {
                return "redirect:/mostrarproducto?duplicado";

            }
        }
        return "redirect:/mostrarproducto?duplicado";
    }

    @PostMapping("/actualizarproducto/{id}")
    public String ActualizarProducto(@PathVariable Long id, @Valid @ModelAttribute("producto") ProductosEntity p, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "usuario/actualizarproducto";
            }
            servicioproducto.update(p);
            return "redirect:/mostrarproducto?actualizado";
        } catch (Exception e) {
            return "redirect:/mostrarproducto?noactualizado";
        }
    }

    @GetMapping("/deshabilitarproducto/{id}")
    public String DeshabilitarProducto(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarproducto?denegado";
        }
        try {
            ProductosEntity objProducto = servicioproducto.findById(id).get();
            servicioproducto.delete(objProducto);
            return "redirect:/mostrarproducto?deshabilitado";
        } catch (Exception e) {
            return "redirect:/mostrarproducto?nodeshabilitado";
        }
    }

    @GetMapping("/habilitarproducto/{id}")
    public String HabilitarProducto(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarproducto?denegado";
        }
        try {
            ProductosEntity objProducto = servicioproducto.findById(id).orElseThrow();
            CategoriaEntity categoria = objProducto.getCategoria();
            if (!categoria.isEstado()) {
                return "redirect:/mostrarproducto?nohabil";
            }
            servicioproducto.enabled(objProducto);
            return "redirect:/mostrarproducto?habilitado";
        } catch (Exception e) {
            return "redirect:/mostrarproducto?nohabilitado";
        }
    }

    @GetMapping("/eliminarproducto/{id}")
    public String EliminarProducto(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarproducto?denegado";
        }
        try {
            ProductosEntity objProducto = servicioproducto.findById(id).get();
            servicioproducto.delete(objProducto);
            return "redirect:/mostrarproducto?eliminado";
        } catch (Exception e) {
            return "redirect:/mostrarproducto?noeliminado";
        }
    }

    @ModelAttribute("producto")
    public ProductosEntity ModeloProducto() {
        return new ProductosEntity();
    }

    // Venta - Detalle Venta
    @Autowired
    private VentaService servicioventa;

    @Autowired
    private DetalleVentaService serviciodetalle;

    @GetMapping("/mostrarventa")
    public String MostrarVentas(Model modelo) {
        modelo.addAttribute("venta", servicioventa.findAllCustom());
        return "/ventas/mostrarventa";
    }

    @PostMapping("/registrarventa")
    @Transactional
    public String RegistroVenta(@Valid @ModelAttribute("venta") VentaEntity v, BindingResult result, Model model, @RequestParam("cupon") String cupon, RedirectAttributes redirectAttributes) {
        model.addAttribute("cliente", serviciocliente.findAllCustom());
        model.addAttribute("usuario", serviciousuario.findAllCustom());

        if (result.hasErrors()) {
            return "ventas/mostrarventa";
        }

        try {
            double descuento = 0.0;
            boolean cuponValido = true;

            if (!cupon.isEmpty()) {
                CuponEntity cuponEntity = repositoriocupon.findByCupon(cupon);

                if (cuponEntity != null) {
                    descuento = cuponEntity.getDescuento();
                    double totalConDescuento = v.getTotal() - descuento;

                    if (totalConDescuento < MONTO_MINIMO_CUPON) {
                        cuponValido = false;
                        redirectAttributes.addFlashAttribute("montoMinimoNoAlcanzado", true);
                        return "redirect:/mostrarpedidos";
                    }

                    v.setTotal(totalConDescuento);
                    servicioventa.add(v);
                } else {
                    cuponValido = false;
                    redirectAttributes.addFlashAttribute("cuponInvalido", true);
                    return "redirect:/mostrarpedidos";
                }
            } else {
                if (v.getTotal() < MONTO_MINIMO_CUPON) {
                    cuponValido = false;
                    redirectAttributes.addFlashAttribute("montoMinimoNoAlcanzado", true);
                    return "redirect:/mostrarpedidos";
                }

                servicioventa.add(v);
            }

            if (cuponValido) {
                for (ProductosEntity detalle : Carrito) {
                    int cantidad = Integer.parseInt(request.getParameter("cantidad_" + Carrito.indexOf(detalle)));
                    DetalleVentaEntity a = new DetalleVentaEntity();
                    a.setVenta(v);
                    a.setProducto(detalle);
                    double precioUnitario = detalle.getPrecio();
                    double precioTotal = precioUnitario * cantidad;
                    a.setPreciounitario(precioTotal);
                    a.setCantidad(cantidad);
                    serviciodetalle.add(a);
                }

                Carrito.clear();
                return "redirect:/mostrarventa?correcto";
            }
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }

        return "redirect:/mostrarpedidos";
    }

    @ModelAttribute("venta")
    public VentaEntity Modeloventa() {
        return new VentaEntity();
    }

    @ModelAttribute("detalle")
    public DetalleVentaEntity ModeloDetalle() {
        return new DetalleVentaEntity();
    }

    @GetMapping("/mostrardetalle/{id}")
    public String MostrarDetalleVenta(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("venta", servicioventa.findById(id).get());
        modelo.addAttribute("detalle", serviciodetalle.findAllCustom());
        return "/ventas/detalleventa";
    }

    // Pedidos 
    @GetMapping("/mostrarpedidos")
    public String MostrarPedidos(Model modelo) {
        modelo.addAttribute("producto", servicioproducto.findAllCustom());
        modelo.addAttribute("categoria", serviciocategoria.findAllCustom());
        modelo.addAttribute("carrito", Carrito);
        modelo.addAttribute("cliente", serviciocliente.findAllCustom());
        modelo.addAttribute("usuario", serviciousuario.findAllCustom());
        return "/pedidos/mostrarpedidos";
    }

    //Cupones
    @Autowired
    private CuponService serviciocupon;

    @GetMapping("/mostrarcupon")
    public String MostrarCupon(Model modelo, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        UsuarioEntity usuario = repositoriousuario.findByUsuario(username);
        boolean esAdministrador = usuario.getRol().getNombre().equals("Administrador");
        modelo.addAttribute("esAdministrador", esAdministrador);
        modelo.addAttribute("cupon", serviciocupon.findAllCustom());
        return "/cupon/mostrarcupon";

    }

    @GetMapping("/mostrarhabilitarcupon")
    public String MostrarHabilitarCupon(Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarcupon?denegado";
        }
        modelo.addAttribute("cupon", serviciocupon.findAll());
        return "/cupon/habilitarcupon";
    }

    @GetMapping("/actualizarcupon/{id}")
    public String MostrarActualizarCupon(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarcupon?denegado";
        }
        modelo.addAttribute("cupon", serviciocupon.findById(id).get());
        return "/cupon/actualizarcupon";
    }

    @PostMapping("/registrarcupon")
    public String RegistroCupon(@Valid @ModelAttribute("cupon") CuponEntity c, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "cupon/mostrarcupon";
            }
            serviciocupon.add(c);
            return "redirect:/mostrarcupon?correcto";
        } catch (Exception e) {
            return "redirect:/mostrarcupon?incorrecto";
        }
    }

    @PostMapping("/actualizarcupon/{id}")
    public String ActualizarCupon(@PathVariable Long id, @Valid @ModelAttribute("cupon") CuponEntity c, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "cupon/actualizarcupon";
            }
            serviciocupon.update(c);
            return "redirect:/mostrarcupon?actualizado";
        } catch (Exception e) {
            return "redirect:/mostrarcupon?noactualizado";
        }
    }

    @GetMapping("/deshabilitarcupon/{id}")
    public String DeshabilitarCupon(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarcupon?denegado";
        }
        try {
            CuponEntity objCupon = serviciocupon.findById(id).get();
            serviciocupon.delete(objCupon);
            return "redirect:/mostrarcupon?deshabilitado";
        } catch (Exception e) {
            return "redirect:/mostrarcupon?nodeshabilitado";
        }
    }

    @GetMapping("/habilitarcupon/{id}")
    public String HabilitarCupon(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarcupon?denegado";
        }
        try {
            CuponEntity objCupon = serviciocupon.findById(id).get();
            serviciocupon.enabled(objCupon);
            return "redirect:/mostrarcupon?habilitado";
        } catch (Exception e) {
            return "redirect:/mostrarcupon?nohabilitado";

        }
    }

    @GetMapping("/eliminarcupon/{id}")
    public String EliminarCupon(@PathVariable Long id, Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("Administrador"))) {
            return "redirect:/mostrarcupon?denegado";
        }
        try {
            CuponEntity objCupon = serviciocupon.findById(id).get();
            serviciocupon.delete(objCupon);
            return "redirect:/mostrarcupon?eliminado";
        } catch (Exception e) {
            return "redirect:/mostrarcupon?noeliminado";
        }
    }

    @ModelAttribute("cupon")
    public CuponEntity ModeloCupon() {
        return new CuponEntity();
    }

}
