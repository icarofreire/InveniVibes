package api.vibes.repository;

import api.vibes.domain.User;
import api.vibes.service.dto.AdminUserDTO;
import api.vibes.service.dto.UserRecord;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByActivationKey(String activationKey);
    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
    Optional<User> findOneByResetKey(String resetKey);
    Optional<User> findOneByEmailIgnoreCase(String email);
    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    // Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
    Page<AdminUserDTO> findAllByOrderByCreatedDateDesc(Pageable pageable);

    // @Query(
    //     "SELECT user \n" +
    //     "FROM Unidade unidade \n" +
    //     "LEFT JOIN unidade.usuarios user \n" +
    //     "WHERE \n" +
    //     "user.id = :idUsuario \n" +
    //     "and user.activated = true \n" +
    //     "and lower(user.login) LIKE '%' || lower(:valorBusca) || '%' \n" +
    //     "or lower(user.firstName) LIKE '%' || lower(:valorBusca) || '%' \n" +
    //     "or lower(user.email) LIKE '%' || lower(:valorBusca) || '%'"
	// )
	// List<AdminUserDTO> buscaPorUsuariosEmUmaUnidade(@Param("idUsuario") Long idUsuario, @Param("valorBusca") String valorBusca, Pageable pageable);

    // @Query(
    //     "SELECT user, unidade.nome \n" +
    //     "FROM Unidade unidade \n" +
    //     "RIGHT JOIN unidade.usuarios user \n" +
    //     "ORDER BY user.createdDate DESC"
	// )
	// List<UserRecord> todosUsuariosComUnidadePaginada(Pageable pageable);

}
