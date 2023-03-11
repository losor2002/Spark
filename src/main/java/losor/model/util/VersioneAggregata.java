package losor.model.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import losor.controller.util.ParameterUtil;
import losor.model.bean.DatiVersione;
import losor.model.bean.Immagine;
import losor.model.bean.Prodotto;
import losor.model.bean.Versione;
import losor.model.dao.Dao;
import losor.model.dao.DatiVersioneDao;
import losor.model.dao.ImmagineDao;
import losor.model.dao.ProdottoDao;
import losor.model.dao.VersioneDao;

public class VersioneAggregata
{
    private Versione versione;
    private DatiVersione datiVersione;
    private List<Immagine> immagini;

    public VersioneAggregata() {
    }

    public VersioneAggregata(Versione versione, DatiVersione datiVersione, List<Immagine> immagini) {
        this.versione = versione;
        this.datiVersione = datiVersione;
        setImmagini(immagini);
    }

    public Versione getVersione() {
        return this.versione;
    }

    public void setVersione(Versione versione) {
        this.versione = versione;
    }

    public DatiVersione getDatiVersione() {
        return this.datiVersione;
    }

    public void setDatiVersione(DatiVersione datiVersione) {
        this.datiVersione = datiVersione;
    }

    public List<Immagine> getImmagini() {
        return this.immagini;
    }

    public void setImmagini(List<Immagine> immagini) {
        this.immagini = immagini.stream().sorted(Comparator.comparing(Immagine::getNumProgressivo)).toList();
    }

    @Override
    public String toString() {
        return "{" +
            " versione='" + getVersione() + "'" +
            ", datiVersione='" + getDatiVersione() + "'" +
            ", immagini='" + getImmagini() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof VersioneAggregata)) {
            return false;
        }
        VersioneAggregata versioneAggregata = (VersioneAggregata) o;
        return Objects.equals(versione, versioneAggregata.versione) && Objects.equals(datiVersione, versioneAggregata.datiVersione) && Objects.equals(immagini, versioneAggregata.immagini);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versione, datiVersione, immagini);
    }

    public static VersioneAggregata getVersioneAggregata(String codice) throws SQLException
    {
        if(ParameterUtil.isEmpty(codice))
            throw new IllegalArgumentException();

        Dao<Versione> versioneDao = VersioneDao.getInstance();
        Versione versione = new Versione();
        versione.setCodice(codice);
        versione.setCancellata(false);
        Optional<Versione> optional = versioneDao.get(versione);
        if(optional.isEmpty())
            throw new IllegalArgumentException();
        versione = optional.get();

        Dao<DatiVersione> datiVersioneDao = DatiVersioneDao.getInstance();
        DatiVersione datiVersione = new DatiVersione();
        datiVersione.setVersione(codice);
        datiVersione = datiVersioneDao.get(datiVersione).get();

        Dao<Immagine> immagineDao = ImmagineDao.getInstance();
        Immagine immagine = new Immagine();
        immagine.setVersione(codice);
        List<Immagine> immagini = immagineDao.getAll(immagine);

        return new VersioneAggregata(versione, datiVersione, immagini);
    }

    public static List<VersioneAggregata> getVersioniAggregate(Integer prodotto) throws SQLException
    {
        if(prodotto == null)
            throw new NullPointerException();
            
        Dao<Prodotto> prodottoDao = ProdottoDao.getInstance();
        Prodotto prodottoObj = new Prodotto();
        prodottoObj.setNumProgressivo(prodotto);
        prodottoObj.setCancellato(false);
        if(!prodottoDao.exists(prodottoObj))
            throw new IllegalArgumentException();

        Dao<Versione> versioneDao = VersioneDao.getInstance();
        Versione versione = new Versione();
        versione.setProdotto(prodotto);
        versione.setCancellata(false);
        List<Versione> versioni = versioneDao.getAll(versione);

        List<VersioneAggregata> versioniAggregate = new ArrayList<>();
        for(Versione ver : versioni)
        {
            VersioneAggregata versioneAggregata = getVersioneAggregata(ver.getCodice());
            versioniAggregate.add(versioneAggregata);
        }
        return versioniAggregate;
    }
}
