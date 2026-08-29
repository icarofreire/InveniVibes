package api.vibes.service.dto;

import api.vibes.domain.User;
import api.vibes.service.dto.AdminUserDTO;

/**
 * A record representing a user.
 */
public record UserRecord (
  User user,

  /*\/ nome da unidade que o usuário está registrado; */
  String nomeUnidade
)
{

}

