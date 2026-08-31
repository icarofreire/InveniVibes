package api.vibes.service;

import api.vibes.service.dto.DistanciaPontosRecord;
import api.vibes.domain.PontoTuristico;
import api.vibes.service.GeoService;
import api.vibes.repository.PontoTuristicoRepository;
import java.util.*;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.PriorityQueue;

/**
 * Service class for managing tags.
 */
@Service
@Transactional
public class PontoTuristicoService {

    private final PontoTuristicoRepository pontoTuristicoRepository;
    private final GeoService geoService;

    public PontoTuristicoService(PontoTuristicoRepository pontoTuristicoRepository, GeoService geoService) {
        this.pontoTuristicoRepository = pontoTuristicoRepository;
        this.geoService = geoService;
    }

    public PontoTuristico save(PontoTuristico pontoTuristico) {
        return pontoTuristicoRepository.save(pontoTuristico);
    }

    public PontoTuristico atualizarEntidade(PontoTuristico pontoTuristico){
        PontoTuristico entidade = pontoTuristicoRepository.findById(pontoTuristico.getId()).orElse(null);
        if (entidade != null) {
            if(pontoTuristico.getId() != null && entidade.getId() != null){
                if(pontoTuristico.getUf() != null) entidade.setUf(pontoTuristico.getUf());
                if(pontoTuristico.getEstado() != null) entidade.setEstado(pontoTuristico.getEstado());
                if(pontoTuristico.getCidade() != null) entidade.setCidade(pontoTuristico.getCidade());
                if(pontoTuristico.getNome() != null) entidade.setNome(pontoTuristico.getNome());
                if(pontoTuristico.getCategoria() != null) entidade.setCategoria(pontoTuristico.getCategoria());
                if(pontoTuristico.getLatitude() != null) entidade.setLatitude(pontoTuristico.getLatitude());
                if(pontoTuristico.getLongitude() != null) entidade.setLongitude(pontoTuristico.getLongitude());
            }
            return save(entidade);
        }
        return null;
    }

    public List<PontoTuristico> findAllByOrderByDataRegistroDesc(Pageable pageable){
        return pontoTuristicoRepository.findAllByOrderByDataRegistroDesc(pageable);
    }

    public void preencherPontosTuristicosBasicos(){
        String filePath = "pontos-turisticos-br.txt";
        if(Files.exists(Path.of(filePath))){
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] partes = line.split("\\|");
                    String uf = partes[0];
                    String nomePontoTuristico = partes[3];
                    if(!pontoTuristicoRepository.findTop1ByNomeAndUf(nomePontoTuristico, uf).isPresent()){

                        PontoTuristico ponto = new PontoTuristico();
                        ponto.setUf(uf);
                        ponto.setEstado(partes[1]);
                        ponto.setCidade(partes[2]);
                        ponto.setNome(nomePontoTuristico);
                        ponto.setCategoria(partes[4]);
                        ponto.setLatitude(Double.parseDouble(partes[5]));
                        ponto.setLongitude(Double.parseDouble(partes[6]));

                        save(ponto);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{ System.out.println("*** arquivo de pontos turisticos basicos não existe;"); }
    }

    public List<DistanciaPontosRecord> calcularDistanciasPontosTuristicos(String uf){

        HashMap<Integer, Double> pontoQueue= new HashMap<>();
        HashMap<Integer, DistanciaPontosRecord> pontoMinObjt= new HashMap<>();
    
        List<PontoTuristico> pontos = pontoTuristicoRepository.findAllByUfOrderByDataRegistroDesc(uf);
        if(pontos != null && !pontos.isEmpty()){
            for (int i = 0; i < pontos.size(); i++) {
                for (int j = 0; j < pontos.size(); j++) {
                    if(i != j){
                        double distancia = geoService.H(pontos.get(i).getLatitude(), pontos.get(i).getLongitude(), pontos.get(j).getLatitude(), pontos.get(j).getLongitude());
                        if(Double.valueOf(distancia).compareTo(0.0) > 0){ // << se retorno da distancia é maior que 0.0;
                            // System.out.println(i + ", " + j + " : " + distancia);

                            if(!pontoQueue.containsKey(i)){
                                pontoQueue.put(i, Double.MAX_VALUE);
                            }else{
                                double minima = Double.min(pontoQueue.get(i), distancia);
                                pontoQueue.put(i, minima);
                                DistanciaPontosRecord distanciaRecord = new DistanciaPontosRecord(
                                    pontos.get(i),
                                    pontos.get(j),
                                    minima,
                                    geoService.kmToMeters(minima),
                                    geoService.kmToMin(minima)
                                );
                                pontoMinObjt.put(i, distanciaRecord);
                            }
                        }
                    }
                }//for
            }//for
            
            // for (Map.Entry<Integer, DistanciaPontosRecord> entry : pontoMinObjt.entrySet()) {
            //     // System.out.println("MIN::" + entry.getKey() + " => " + entry.getValue());
            //     System.out.println("MIN::" + entry.getKey() + " => " +
            //     entry.getValue().pontoA().getNome() + " => " +
            //     entry.getValue().pontoB().getNome() + " => " +
            //     entry.getValue().distancia() + " => " +
            //     entry.getValue().metros() + " => " +
            //     entry.getValue().minutos()
            //     );
            // }
            List<DistanciaPontosRecord> cidadesDistancias = new ArrayList<>(pontoMinObjt.values());
            /*\/ ordenar por distancias; */
            cidadesDistancias.sort(java.util.Comparator.comparing(DistanciaPontosRecord::distancia));
            return cidadesDistancias;
        }//if
        return null;
    }

    @Transactional // Ensure the operation is within a transaction
    public void deleteEntityById(Long id) {
        pontoTuristicoRepository.deleteById(id);
    }

}
