package com.example.ms_usuario;

import com.example.ms_usuario.model.Empleado;
import com.example.ms_usuario.model.Permiso;
import com.example.ms_usuario.model.Rol;
import com.example.ms_usuario.model.Usuario;
import com.example.ms_usuario.model.UsuarioRol;
import com.example.ms_usuario.repository.EmpleadoRepository;
import com.example.ms_usuario.repository.PermisoRepository;
import com.example.ms_usuario.repository.RolRepository;
import com.example.ms_usuario.repository.UsuarioRepository;
import com.example.ms_usuario.repository.UsuarioRolRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PermisoRepository permisoRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        // Evitar duplicar datos si ya existen
        if (usuarioRepository.count() > 0 || rolRepository.count() > 0 || permisoRepository.count() > 0) {
            return;
        }

        // =========================
        // GENERAR PERMISOS
        // =========================
        List<Permiso> permisos = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Permiso permiso = new Permiso();
            permiso.setCodigo("PERM_" + faker.regexify("[A-Z]{5}") + "_" + i);
            permisoRepository.save(permiso);
            permisos.add(permiso);
        }

        // =========================
        // GENERAR ROLES
        // =========================
        List<Rol> roles = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Rol rol = new Rol();

            String nombreRol;
            if (i == 0) {
                nombreRol = "ADMIN";
            } else if (i == 1) {
                nombreRol = "USER";
            } else {
                nombreRol = "EMPLEADO";
            }

            rol.setNombre(nombreRol);

            Set<Permiso> permisosRol = new HashSet<>();
            int cantidadPermisos = random.nextInt(permisos.size()) + 1;

            for (int j = 0; j < cantidadPermisos; j++) {
                permisosRol.add(permisos.get(random.nextInt(permisos.size())));
            }

            rol.setPermisos(permisosRol);
            rolRepository.save(rol);
            roles.add(rol);
        }

        // =========================
        // GENERAR USUARIOS
        // =========================
        List<Usuario> usuarios = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Usuario usuario = new Usuario();
            usuario.setNombre(faker.name().fullName());
            usuario.setEmail("usuario" + i + "_" + faker.internet().emailAddress());
            usuario.setPasswordHash(passwordEncoder.encode("123456"));
            usuario.setActivo(faker.bool().bool());

            usuarioRepository.save(usuario);
            usuarios.add(usuario);
        }

        // =========================
        // ASIGNAR ROLES A USUARIOS
        // =========================
        for (Usuario usuario : usuarios) {
            UsuarioRol usuarioRol = new UsuarioRol();
            usuarioRol.setUsuario(usuario);
            usuarioRol.setRol(roles.get(random.nextInt(roles.size())));
            usuarioRolRepository.save(usuarioRol);
        }

        // =========================
        // GENERAR EMPLEADOS
        // =========================
        for (int i = 0; i < 5; i++) {
            Usuario usuario = usuarios.get(i);

            if (!empleadoRepository.existsByUsuarioId(usuario.getId())) {
                Empleado empleado = new Empleado();
                empleado.setUsuario(usuario);
                empleado.setActivo(true);
                empleadoRepository.save(empleado);
            }
        }

        System.out.println("✅ DataLoader ejecutado correctamente en perfil DEV");
    }
}