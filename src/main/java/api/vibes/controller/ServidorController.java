package api.vibes.controller;

import api.vibes.config.Constants;
// import api.vibes.domain.Servidor;
// import api.vibes.service.ServidorService;
// import api.vibes.service.TestConnection;
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
public class ServidorController {

    // private final ServidorService servidorService;

    // public ServidorController(ServidorService servidorService) {
    //     this.servidorService = servidorService;
    // }

    // @PostMapping("/servidor")
    // public Map<String, Object> salvarServidor(@Valid @RequestBody Servidor servidor) {
    //     Map<String, Object> mp = new HashMap<String, Object>();
    //     mp.put("return", servidorService.registrarNovoServidor(servidor));
    //     return mp;
    // }

    // @PutMapping("/servidor")
    // public Servidor updateServidor(@Valid @RequestBody Servidor servidor) {
    //     return servidorService.atualizarEntidade(servidor);
    // }

    // @GetMapping("/servidores")
    // public List<Servidor> getRecentServidors(
    //     @RequestParam("page") Optional<Integer> page,
    //     @RequestParam("size") Optional<Integer> size
    // ) {
    //     Pageable pageable = Constants.createPageable(page, size);
    //     return servidorService.findAllByAtivoIsTrueOrderByDataRegistroDesc(pageable);
    // }

    // @GetMapping("/busca-servidores")
    // public List<Servidor> getRecentServidors(
    //     @RequestParam("idUsuario") Optional<Long> idUsuario,
    //     @RequestParam("busca") Optional<String> busca,
    //     @RequestParam("page") Optional<Integer> page,
    //     @RequestParam("size") Optional<Integer> size
    // ) {
    //     Pageable pageable = Constants.createPageable(page, size);
    //     if(idUsuario.isPresent()){
    //         return servidorService.buscaPorServidoresEmUmaUnidade(idUsuario.get(), busca.orElse(null), pageable);
    //     }
    //     return new ArrayList<>();
    // }

    // @GetMapping("/servidor/{id}")
    // public Servidor getServidorPorId(@PathVariable(value = "id", required = true) final Long id) {
    //     return servidorService.findById(id);
    // }

    // @GetMapping("/teste-conexao-servidor")
    // public Map<String, Object> getTesteConexaoServidor(@RequestParam(required = true) Map<String, String> params) {
    //     Map<String, Object> mp = new HashMap<String, Object>();
    //     if(params.containsKey("host") && params.containsKey("username") && params.containsKey("password")){
    //         TestConnection teste = new TestConnection(params.get("host"), params.get("username"), params.get("password"));
    //         mp.put("conexao", teste.seConexaoEstabelecida());
    //         mp.put("message", teste.getMessageError());
    //     }
    //     return mp;
    // }

    // @DeleteMapping("/delete-servidor/{id}")
    // public void deleteEntityById(@PathVariable(value = "id", required = true) final Long id) {
    //     servidorService.removerServidorUnidadePertencente(id);
    //     servidorService.deleteEntityById(id);
    // }

}
