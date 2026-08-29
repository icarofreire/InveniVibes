package api.vibes.service.dto;

import api.vibes.domain.PontoTuristico;

/**
 * A record representing a...
 */
public record DistanciaPontosRecord (
  PontoTuristico pontoA,
  PontoTuristico pontoB,
  double distancia,
  double metros,
  double minutos
)
{

}

