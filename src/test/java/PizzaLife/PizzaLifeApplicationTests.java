package PizzaLife;

import PizzaLife.entity.gestion.RolEntity;
import PizzaLife.entity.gestion.UsuarioEntity;
import PizzaLife.seguridad.Seguridad;
import PizzaLife.entity.gestion.RolEntity;
import PizzaLife.service.gestion.RolService;
import PizzaLife.service.gestion.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PizzaLifeApplicationTests {
	@Autowired
	private UsuarioService usuarioservicio;
	@Autowired
	private Seguridad seguridadConfig;
	@Autowired
	private RolService rolservicio;
	@Test
	void crearUsuarioAdmin() {
		RolEntity roladmin = new RolEntity();
		roladmin.setNombre("Administrador");
		roladmin.setEstado(true);
		rolservicio.add(roladmin);

		RolEntity rol = new RolEntity();
		rol.setCodigo(1);

		UsuarioEntity us = new UsuarioEntity();
		us.setUsuario("Jungkook9194");
		us.setNombre("Henry Sebastian Otero Alvarez");
		us.setPassword(seguridadConfig.passwordEncoder().encode("Sebas54147"));
                us.setCorreo("x45970830@gmail.com");
		us.setEstado(true);
		us.setRol(rol);
		us.setDni("75597640");

		usuarioservicio.add(us);
	}

}
