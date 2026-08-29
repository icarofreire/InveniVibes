package api.vibes.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * ;
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PontoTuristico {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    // UF|Estado|Cidade|Ponto Turístico|Categoria|Latitude|Longitude
    @Column
    private String uf;
    @Column
    private String estado;
    @Column
    private String cidade;
    @Column
    private String nome;
    @Column
    private String categoria;
    @Column
    private Double latitude;
    @Column
    private Double longitude;
    @Column
    private LocalDateTime dataRegistro = LocalDateTime.now();

    
    /* *** */

    public Long getId(){
        return this.id;
    }

    public String getUf(){
        return this.uf;
    }

    public void setUf(String uf){
        this.uf = uf;
    }

    public String getEstado(){
        return this.estado;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }

    public String getCidade(){
        return this.cidade;
    }

    public void setCidade(String cidade){
        this.cidade = cidade;
    }

    public String getNome(){
        return this.nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getCategoria(){
        return this.categoria;
    }

    public void setCategoria(String categoria){
        this.categoria = categoria;
    }

    public Double getLatitude(){
        return this.latitude;
    }

    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }

    public Double getLongitude(){
        return this.longitude;
    };

    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }

    public LocalDateTime getDataRegistro() {
        return this.dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

}
