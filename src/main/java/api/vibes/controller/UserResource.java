package api.vibes.controller;

import api.vibes.config.Constants;
import api.vibes.domain.User;
import api.vibes.repository.UserRepository;
import api.vibes.security.AuthoritiesConstants;
// import api.vibes.domain.ROLE;
// import gencode.service.MailService;
import api.vibes.service.UserService;
// import api.vibes.service.UnidadeService;
import api.vibes.service.dto.AdminUserDTO;
import api.vibes.service.dto.UserRecord;
// import gencode.web.rest.errors.BadRequestAlertException;
// import gencode.web.rest.errors.EmailAlreadyUsedException;
// import gencode.web.rest.errors.LoginAlreadyUsedException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
// import tech.jhipster.web.util.HeaderUtil;
// import tech.jhipster.web.util.PaginationUtil;
// import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link gencode.domain.User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class UserResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList(
            "id",
            "login",
            "firstName",
            "lastName",
            "email",
            "activated",
            "langKey",
            "createdBy",
            "createdDate",
            "lastModifiedBy",
            "lastModifiedDate"
        )
    );

    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    // @Value("${jhipster.clientApp.name}")
    // private String applicationName;

    private final UserService userService;
    // private final UnidadeService unidadeService;

    private final UserRepository userRepository;

    // private final MailService mailService;

    public UserResource(UserService userService, UserRepository userRepository/*, UnidadeService unidadeService*/) {
        this.userService = userService;
        this.userRepository = userRepository;
        // this.unidadeService = unidadeService;
    }

    /**
     * {@code POST  /admin/users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends a
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public User createUser(@Valid @RequestBody AdminUserDTO userDTO) throws URISyntaxException, Exception {
        LOG.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            // throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            throw new Exception("BadRequestAlertException");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            // throw new LoginAlreadyUsedException();
            throw new Exception("LoginAlreadyUsedException");
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            // throw new EmailAlreadyUsedException();
            throw new Exception("EmailAlreadyUsedException");
        } else {
            User newUser = userService.createUser(userDTO);
            // // mailService.sendCreationEmail(newUser);
            // return ResponseEntity.created(new URI("/api/admin/users/" + newUser.getLogin()))
            //     .headers(
            //         HeaderUtil.createAlert(applicationName, "A user is created with identifier " + newUser.getLogin(), newUser.getLogin())
            //     )
            //     .body(newUser);
            return newUser;
        }
    }

    /**
     * {@code PUT /admin/users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping({ "/users", "/users/{login}" })
    // @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public AdminUserDTO updateUser(
        @PathVariable(name = "login", required = false) @Pattern(regexp = Constants.LOGIN_REGEX) String login,
        @Valid @RequestBody AdminUserDTO userDTO
    ) {
        Optional<AdminUserDTO> updatedUser = userService.updateUser(userDTO);
        // if(updatedUser.isPresent()){
        //     // unidadeService.registrarUsuarioEmUmaUnidade(userRepository.findById(updatedUser.get().getId()).get(), userDTO.getIdUnidade());
        // }

        return updatedUser.orElse(null);
    }

    /**
     * {@code GET /admin/users} : get all users with all the details - calling this are only allowed for the administrators.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    // @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<AdminUserDTO> getAllUsers(
        // @org.springdoc.core.annotations.ParameterObject Pageable pageable
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size
    ) {
        // LOG.debug("REST request to get all User for an admin");
        // if (!onlyContainsAllowedProperties(pageable)) {
        //     // return ResponseEntity.badRequest().build();
        //     return null;
        // }

        final Page<AdminUserDTO> pageContent = userService.getAllManagedUsers(Constants.createPageable(page, size));
        return pageContent.getContent();
        // Pageable pageable = Constants.createPageable(page, size);
        // return userService.todosUsuariosComUnidadePaginada(pageable);
    }

    @GetMapping("/busca-users")
    public List<AdminUserDTO> getBuscaUsers(
        @RequestParam("idUsuario") Optional<Long> idUsuario,
        @RequestParam("busca") Optional<String> busca,
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size
    ) {
        if(idUsuario.isPresent()){
            Pageable pageable = Constants.createPageable(page, size);
            // return userService.buscaPorUsuariosEmUmaUnidade(idUsuario.get(), busca.orElse(null), pageable);
        }
        return new ArrayList<>();
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * {@code GET /admin/users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{login}")
    // @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public AdminUserDTO getUser(@PathVariable("login") @Pattern(regexp = Constants.LOGIN_REGEX) String login) {
        /*\/ se o login passado for um email; */
        if(userService.isValidEmail(login)){
            Optional<User> usuario = userService.findOneByEmailIgnoreCase(login);
            if(usuario.isPresent()){
                return userService.getUserWithAuthoritiesByLogin(usuario.get().getLogin()).map(AdminUserDTO::new).orElse(null);
            }
        }
        return userService.getUserWithAuthoritiesByLogin(login).map(AdminUserDTO::new).orElse(null);
    }

    /**
     * {@code DELETE /admin/users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{login}")
    // @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public void deleteUser(@PathVariable("login") @Pattern(regexp = Constants.LOGIN_REGEX) String login) {
        // LOG.debug("REST request to delete User: {}", login);
        Optional<User> usuario = userRepository.findOneByLogin(login);
        if(usuario.isPresent()){
            // unidadeService.removerUsuarioUnidadePertencente(usuario.get().getId());
            userService.deleteUser(login);
        }
    }
}
