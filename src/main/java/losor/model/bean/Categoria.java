package losor.model.bean;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "categoria")
public class Categoria implements Bean
{
    @Id @GeneratedValue
    @Column(name = "numProgressivo")
    private Integer numProgressivo;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "sottotipo", nullable = false)
    private String sottotipo;

    public Categoria() {
    }

    public Categoria(Integer numProgressivo, String tipo, String sottotipo) {
        this.numProgressivo = numProgressivo;
        this.tipo = tipo;
        this.sottotipo = sottotipo;
    }

    public Integer getNumProgressivo() {
        return this.numProgressivo;
    }

    public void setNumProgressivo(Integer numProgressivo) {
        this.numProgressivo = numProgressivo;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSottotipo() {
        return this.sottotipo;
    }

    public void setSottotipo(String sottotipo) {
        this.sottotipo = sottotipo;
    }

    @Override
    public String toString() {
        return "{" +
            " numProgressivo='" + getNumProgressivo() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", sottotipo='" + getSottotipo() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Categoria)) {
            return false;
        }
        Categoria categoria = (Categoria) o;
        return Objects.equals(numProgressivo, categoria.numProgressivo) && Objects.equals(tipo, categoria.tipo) && Objects.equals(sottotipo, categoria.sottotipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numProgressivo, tipo, sottotipo);
    }

}
