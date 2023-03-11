<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, losor.model.util.VersioneAggregata, losor.model.bean.Immagine, losor.controller.util.TextUtil"%>

<%
    String contextPath = application.getContextPath();
    List<VersioneAggregata> versioniAggregate = (List<VersioneAggregata>) request.getAttribute("versioniAggregate");
    String sparkFiles = getServletContext().getInitParameter("sparkFiles");
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= contextPath%>/images/spark-icon.ico" type="image/x-icon">
        <title>Versioni prodotto #${param.prodotto}</title>
    </head>
    <body class="bg-dark text-light">
        <div class="container">
            <h1 class="text-center">Versioni del prodotto #${param.prodotto}</h1>

            <p class="text-center">
                <a class="text-decoration-none" href="<%= contextPath%>/adminHome">Torna alla home dell'amministratore</a>
            </p>

            <p class="text-center">
                <a class="text-decoration-none" href="<%= contextPath%>/insertVersion?prodotto=${param.prodotto}">Inserisci una nuova versione</a>
            </p>

            <div>
                <table class="table table-dark table-hover align-middle text-center table-sm">

                    <tr>
                        <th>Codice</th>
                        <th>Nome</th>
                        <th>Icona</th>
                        <th>Data di uscita</th>
                        <th>Descrizione</th>
                        <th>IVA</th>
                        <th>Prezzo</th>
                        <th>Quantità</th>
                        <th>Gestione</th>
                    </tr>

                    <% for(VersioneAggregata versioneAggregata : versioniAggregate) { %>

                        <tr>
                            <td><%= versioneAggregata.getVersione().getCodice()%></td>
                            <td><%= versioneAggregata.getVersione().getNome()%></td>
                            <td class="col-1 col-lg-2">
                                <div class="ratio ratio-1x1">
                                    <div class="d-flex align-items-center">
                                        <img class="d-block mx-auto mh-100 mw-100 h-auto w-auto" src="<%= sparkFiles + versioneAggregata.getVersione().getIcona()%>" alt="icona">
                                    </div>
                                </div>
                            </td>
                            <td><%= versioneAggregata.getDatiVersione().getDataDiUscita()%></td>
                            <td class="col-8"><%= TextUtil.htmlEncode(versioneAggregata.getDatiVersione().getDescrizione())%></td>
                            <td><%= versioneAggregata.getDatiVersione().getIva()%></td>
                            <td><%= versioneAggregata.getDatiVersione().getPrezzo()%></td>
                            <td><%= versioneAggregata.getDatiVersione().getQuantita()%></td>
                            <td>
                                <a class="text-decoration-none" href="<%= contextPath%>/modifyVersion?codice=<%= versioneAggregata.getVersione().getCodice()%>">Modifica</a>
                                <br><br>
                                <button type="button" class="btn btn-link text-decoration-none" data-bs-toggle="modal" data-bs-target="#modal_<%= TextUtil.idEncode(versioneAggregata.getVersione().getCodice())%>">
                                    Elimina
                                </button>
                            </td>
                            <div class="modal fade" id="modal_<%= TextUtil.idEncode(versioneAggregata.getVersione().getCodice())%>" tabindex="-1" aria-labelledby="ModalLabel_<%= TextUtil.idEncode(versioneAggregata.getVersione().getCodice())%>" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered text-dark">
                                  <div class="modal-content">
                                    <div class="modal-header">
                                      <h5 class="modal-title" id="ModalLabel_<%= TextUtil.idEncode(versioneAggregata.getVersione().getCodice())%>">Conferma</h5>
                                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                      Sei sicuro di voler eliminare <%= versioneAggregata.getVersione().getCodice()%>?
                                    </div>
                                    <div class="modal-footer">
                                      <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annulla</button>
                                      <a class="btn btn-primary" href="<%= contextPath%>/deleteVersion?codice=<%= versioneAggregata.getVersione().getCodice()%>">Conferma</a>
                                    </div>
                                  </div>
                                </div>
                            </div>
                        </tr>

                    <% } %>

                </table>
            </div>

            <% if(request.getAttribute("SQLException") != null) { %>

                <p class="text-danger">Errore di comunicazione con il database</p>

            <% } %>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    </body>
</html>