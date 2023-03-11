package losor.model.bean;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "recensione")
public class Recensione implements Bean
{
    @Id
    @Column(name = "versione")
    private String versione;

    @Id
    @Column(name = "cliente")
    private Integer cliente;

    @Column(name = "voto", nullable = false)
    private Integer voto;

    @Column(name = "testo", nullable = false)
    private String testo;

    public Recensione() {
    }

    public Recensione(String versione, Integer cliente, Integer voto, String testo) {
        this.versione = versione;
        this.cliente = cliente;
        this.voto = voto;
        this.testo = testo;
    }

    public String getVersione() {
        return this.versione;
    }

    public void setVersione(String versione) {
        this.versione = versione;
    }

    public Integer getCliente() {
        return this.cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    public Integer getVoto() {
        return this.voto;
    }

    public void setVoto(Integer voto) {
        this.voto = voto;
    }

    public String getTesto() {
        return this.testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    @Override
    public String toString() {
        return "{" +
            " versione='" + getVersione() + "'" +
            ", cliente='" + getCliente() + "'" +
            ", voto='" + getVoto() + "'" +
            ", testo='" + getTesto() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Recensione)) {
            return false;
        }
        Recensione recensione = (Recensione) o;
        return Objects.equals(versione, recensione.versione) && Objects.equals(cliente, recensione.cliente) && Objects.equals(voto, recensione.voto) && Objects.equals(testo, recensione.testo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versione, cliente, voto, testo);
    }

}
