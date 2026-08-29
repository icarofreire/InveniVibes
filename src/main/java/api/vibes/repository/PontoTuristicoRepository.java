package api.vibes.repository;

import api.vibes.domain.PontoTuristico;
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
public interface PontoTuristicoRepository extends JpaRepository<PontoTuristico, Long> {
    // Optional<User> findOneByActivationKey(String activationKey);
    // List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
    // Optional<User> findOneByResetKey(String resetKey);
    // Optional<User> findOneByEmailIgnoreCase(String email);
    Optional<PontoTuristico> findTop1ByNomeAndUf(String Nome, String uf);
    List<PontoTuristico> findAllByUfOrderByDataRegistroDesc(String uf);

    // @EntityGraph(attributePaths = "authorities")
    // Optional<User> findOneWithAuthoritiesByLogin(String login);

    // @EntityGraph(attributePaths = "authorities")
    // Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    // // Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
    // Page<AdminUserDTO> findAllByOrderByCreatedDateDesc(Pageable pageable);
    List<PontoTuristico> findAllByOrderByDataRegistroDesc(Pageable pageable);


}
