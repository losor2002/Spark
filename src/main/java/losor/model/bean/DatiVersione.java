package losor.model.bean;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dativersione")
public class DatiVersione implements Bean
{
    @Id
    @Column(name = "versione")
    private String versione;

    @Column(name = "dataDiUscita", nullable = false)
    private Date dataDiUscita;

    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    @Column(name = "iva", nullable = false)
    private BigDecimal iva;

    @Column(name = "prezzo", nullable = false)
    private BigDecimal prezzo;

    @Column(name = "quantita", nullable = false)
    private Integer quantita;

    public DatiVersione() {
    }

    public DatiVersione(String versione, Date dataDiUscita, String descrizione, BigDecimal iva, BigDecimal prezzo, Integer quantita) {
        this.versione = versione;
        this.dataDiUscita = dataDiUscita;
        this.descrizione = descrizione;
        this.iva = iva;
        this.prezzo = prezzo;
        this.quantita = quantita;
    }

    public String getVersione() {
        return this.versione;
    }

    public void setVersione(String versione) {
        this.versione = versione;
    }

    public Date getDataDiUscita() {
        return this.dataDiUscita;
    }

    public void setDataDiUscita(Date dataDiUscita) {
        this.dataDiUscita = dataDiUscita;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getIva() {
        return this.iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getPrezzo() {
        return this.prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public Integer getQuantita() {
        return this.quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    @Override
    public String toString() {
        return "{" +
            " versione='" + getVersione() + "'" +
            ", dataDiUscita='" + getDataDiUscita() + "'" +
            ", descrizione='" + getDescrizione() + "'" +
            ", iva='" + getIva() + "'" +
            ", prezzo='" + getPrezzo() + "'" +
            ", quantita='" + getQuantita() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof DatiVersione)) {
            return false;
        }
        DatiVersione datiVersione = (DatiVersione) o;
        return Objects.equals(versione, datiVersione.versione) && Objects.equals(dataDiUscita, datiVersione.dataDiUscita) && Objects.equals(descrizione, datiVersione.descrizione) && Objects.equals(iva, datiVersione.iva) && Objects.equals(prezzo, datiVersione.prezzo) && Objects.equals(quantita, datiVersione.quantita);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versione, dataDiUscita, descrizione, iva, prezzo, quantita);
    }

}
