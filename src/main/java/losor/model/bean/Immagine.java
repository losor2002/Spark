package losor.model.bean;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "immagine")
public class Immagine implements Bean
{
    @Id
    @Column(name = "versione")
    private String versione;

    @Id
    @Column(name = "numProgressivo")
    private Integer numProgressivo;

    @Column(name = "img", nullable = false)
    private String img;

    public Immagine() {
    }

    public Immagine(String versione, Integer numProgressivo, String img) {
        this.versione = versione;
        this.numProgressivo = numProgressivo;
        this.img = img;
    }

    public String getVersione() {
        return this.versione;
    }

    public void setVersione(String versione) {
        this.versione = versione;
    }

    public Integer getNumProgressivo() {
        return this.numProgressivo;
    }

    public void setNumProgressivo(Integer numProgressivo) {
        this.numProgressivo = numProgressivo;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "{" +
            " versione='" + getVersione() + "'" +
            ", numProgressivo='" + getNumProgressivo() + "'" +
            ", img='" + getImg() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Immagine)) {
            return false;
        }
        Immagine immagine = (Immagine) o;
        return Objects.equals(versione, immagine.versione) && Objects.equals(numProgressivo, immagine.numProgressivo) && Objects.equals(img, immagine.img);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versione, numProgressivo, img);
    }

}
