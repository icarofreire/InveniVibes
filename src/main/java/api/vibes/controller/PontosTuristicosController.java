package api.vibes.controller;

import api.vibes.config.Constants;
import api.vibes.domain.PontoTuristico;
import api.vibes.service.dto.DistanciaPontosRecord;
import api.vibes.service.PontoTuristicoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

/**
 * Controller to servidorns.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class PontosTuristicosController {

    private final PontoTuristicoService pontoTuristicoService;

    public PontosTuristicosController(PontoTuristicoService pontoTuristicoService) {
        this.pontoTuristicoService = pontoTuristicoService;
    }

    @PostMapping("/pontoTuristico")
    public Map<String, Object> salvarPontoTuristico(@Valid @RequestBody PontoTuristico pontoTuristico) {
        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("return", pontoTuristicoService.save(pontoTuristico));
        return mp;
    }

    @PutMapping("/servidor")
    public PontoTuristico updatePontoTuristico(@Valid @RequestBody PontoTuristico pontoTuristico) {
        return pontoTuristicoService.atualizarEntidade(pontoTuristico);
    }

    @GetMapping("/pontos-turisticos")
    public List<PontoTuristico> getPontosTuristicosPage(
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size
    ) {
        Pageable pageable = Constants.createPageable(page, size);
        return pontoTuristicoService.findAllByOrderByDataRegistroDesc(pageable);
    }

    @GetMapping("/pontos-proximos")
    public List<DistanciaPontosRecord> getPontosProximos(
        @RequestParam("uf") Optional<String> uf,
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size
    ) {
        if(uf.isPresent()){
            return pontoTuristicoService.calcularDistanciasPontosTuristicos(uf.get());
        }else{
            return new ArrayList<>();
        }
    }

    @DeleteMapping("/delete-ponto/{id}")
    public void deleteEntityById(@PathVariable(value = "id", required = true) final Long id) {
        pontoTuristicoService.deleteEntityById(id);
    }

}
