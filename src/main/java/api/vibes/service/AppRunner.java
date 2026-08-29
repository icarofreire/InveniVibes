package api.vibes.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

import api.vibes.security.AuthoritiesConstants;
import api.vibes.domain.User;
import api.vibes.domain.Authority;
import api.vibes.service.UserService;
import api.vibes.service.PontoTuristicoService;
import api.vibes.service.dto.AdminUserDTO;
import api.vibes.repository.AuthorityRepository;
import api.vibes.repository.UserRepository;
// import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

@Component
@Order(1)
public class AppRunner implements ApplicationRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PontoTuristicoService pontoTuristicoService;

    public AppRunner(
        AuthorityRepository authorityRepository,
        UserRepository userRepository,
        UserService userService,
        PontoTuristicoService pontoTuristicoService
    ){
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.pontoTuristicoService = pontoTuristicoService;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        System.out.println( ">>> run method Started !! @Order(1)" );

        preAuthoritys();
        preUsers();
        /**\/ usuario que poderá ser utilizado para registrar as unidades institucionais;
        gerenciar unidades;
        */
        // preUser("api-gateway", "736824824982987214272", AuthoritiesConstants.roles.ROLE_ROOT.name(), AuthoritiesConstants.roles.ROLE_USER.name());
        preUser("root", "562531568685745253753", AuthoritiesConstants.roles.ROLE_ROOT.name(), AuthoritiesConstants.roles.ROLE_USER.name());

        pontoTuristicoService.preencherPontosTuristicosBasicos();
        pontoTuristicoService.calcularDistanciasPontosTuristicos("SE");
    }

    /**\/ inicia algumas ROLE'S de uruários para a entidade Authority; */
    private void preAuthoritys(){
        for (AuthoritiesConstants.roles role : AuthoritiesConstants.roles.values()){
            Authority auth = authorityRepository.findFirstByName(role.name());
            if(auth == null){
                auth = new Authority();
                auth.setName(role.name());
                authorityRepository.save(auth);
            }
        }
    }

    /**\/ cria alguns uruários para o sistema; */
    private void preUsers(){
        User user1 = userRepository.findOneByLogin("admin").orElse(null);
        if(user1 == null){
            AdminUserDTO userDTO = new AdminUserDTO();
            userDTO.setLogin("admin");
            userDTO.setFirstName("admin");
            userDTO.setEmail("admin@admin.com");
            userDTO.setActivated(true);
            /**\/ Role de admin; */
            Set<String> authorities = new HashSet<String>();
            authorities.add(AuthoritiesConstants.roles.ROLE_ADMIN.name());
            authorities.add(AuthoritiesConstants.roles.ROLE_USER.name());
            userDTO.setAuthorities(authorities);
            user1 = userService.createUser(userDTO, "admin");
        }
    }

    private void preUser(String login, String password, String... roles){
        User user1 = userRepository.findOneByLogin(login).orElse(null);
        if(user1 == null){
            AdminUserDTO userDTO = new AdminUserDTO();
            userDTO.setLogin(login);
            userDTO.setFirstName(login);
            // userDTO.setEmail("admin@admin.com");
            userDTO.setActivated(true);
            /**\/ Role de admin; */
            Set<String> authorities = new HashSet<String>();
            for (String role : roles) {
                authorities.add(role);
            }
            // authorities.add(AuthoritiesConstants.roles.ROLE_ADMIN.name());
            // authorities.add(AuthoritiesConstants.roles.ROLE_USER.name());
            userDTO.setAuthorities(authorities);
            user1 = userService.createUser(userDTO, password);
        }
    }
}

@Component
@Order(2)
class AppRunner2C implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // System.out.println(">>> run method Started !! @Order(2)");
    }

}
