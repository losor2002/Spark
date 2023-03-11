<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="losor.model.util.VersioneAggregata"%>

<%
    boolean modifyVersion = request.getAttribute("modifyVersion") != null;
    VersioneAggregata versioneAggregata = (VersioneAggregata) request.getAttribute("versioneAggregata");
    Integer numImmagini = 1;
    try
    {
        numImmagini = Integer.valueOf(request.getParameter("numImmagini"));
    }
    catch(NumberFormatException e) {}
    if(numImmagini <= 0)
        numImmagini = 1;

    if(versioneAggregata != null)
        numImmagini = versioneAggregata.getImmagini().size();

    String nome = versioneAggregata == null ? (request.getParameter("nome") == null ? "" : request.getParameter("nome")) : versioneAggregata.getVersione().getNome();
    String dataDiUscita = versioneAggregata == null ? (request.getParameter("dataDiUscita") == null ? "" : request.getParameter("dataDiUscita")) : versioneAggregata.getDatiVersione().getDataDiUscita().toString();
    String descrizione = versioneAggregata == null ? (request.getParameter("descrizione") == null ? "" : request.getParameter("descrizione")) : versioneAggregata.getDatiVersione().getDescrizione();
    String iva = versioneAggregata == null ? (request.getParameter("iva") == null ? "" : request.getParameter("iva")) : versioneAggregata.getDatiVersione().getIva().toString();
    String prezzo = versioneAggregata == null ? (request.getParameter("prezzo") == null ? "" : request.getParameter("prezzo")) : versioneAggregata.getDatiVersione().getPrezzo().toString();
    String quantita = versioneAggregata == null ? (request.getParameter("quantita") == null ? "" : request.getParameter("quantita")) : versioneAggregata.getDatiVersione().getQuantita().toString();
    String iconaAttuale = versioneAggregata == null ? (request.getParameter("iconaAttuale") == null ? "" : request.getParameter("iconaAttuale")) : versioneAggregata.getVersione().getIcona();

    Integer numImmaginiAttuali = 0;
    if(modifyVersion)
        numImmaginiAttuali = versioneAggregata == null ? Integer.valueOf(request.getParameter("numImmaginiAttuali")) : versioneAggregata.getImmagini().size();

    String sparkFiles = getServletContext().getInitParameter("sparkFiles");
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= application.getContextPath()%>/images/spark-icon.ico" type="image/x-icon">
        <title><%= modifyVersion ? "Modifica " +  request.getParameter("codice") : "Inserisci una nuova Versione"%></title>
    </head>
    <body class="bg-dark text-light">
        <div class="container">
            <h1 class="text-center"><%= modifyVersion ? "Modifica " +  request.getParameter("codice") : "Inserisci una nuova versione per il prodotto #" + request.getParameter("prodotto")%></h1>

            <% if(modifyVersion) { %>

                <div class="text-center">
                    <p>Questa è l'icona attuale</p>
                    <div class="ratio ratio-21x9">
                        <div class="d-flex align-items-center">
                            <img class="d-block mx-auto mh-100 mw-100 h-auto w-auto" src="<%= sparkFiles + iconaAttuale%>" alt="icona">
                        </div>
                    </div>
                </div>

            <% } %>

            <form id="versioneForm" class="col-xl-9 mx-auto mb-3" action="<%= application.getContextPath()%><%= modifyVersion ? "/modifyVersion" : "/insertVersion"%>" method="post" enctype="multipart/form-data">

                <% if(!modifyVersion) { %>

                    <input type="hidden" name="prodotto" value="${param.prodotto}">

                    <div class="mb-3">
                        <label for="codice" class="form-label">Codice</label>
                        <input type="text" class="form-control" id="codice" name="codice" value="${param.codice}">
                    </div>

                <% } else { %>

                    <input type="hidden" name="codice" value="${param.codice}">

                    <input type="hidden" name="iconaAttuale" value="<%= iconaAttuale%>">

                    <input type="hidden" name="numImmaginiAttuali" value="<%= numImmaginiAttuali%>">

                    <% for(int i = 0; i < numImmaginiAttuali; i++) { %>

                        <input type="hidden" name="immagineAttuale<%= i%>" value="<%= versioneAggregata == null ? request.getParameter("immagineAttuale" + i) : versioneAggregata.getImmagini().get(i).getImg()%>">

                    <% } %>

                <% } %>

                <div class="mb-3">
                    <label for="nome" class="form-label">Nome</label>
                    <input type="text" class="form-control" id="nome" name="nome" value="<%= nome%>">
                </div>
                <div class="mb-3">
                    <label for="icona" class="form-label">Icona</label>
                    <input type="file" class="form-control" id="icona" name="icona" accept=".jpg">
                    <% if(modifyVersion) { %>
                        <div class="form-text">
                            Compilare questo campo solamente se si vuole sostituire l'icona attuale
                        </div>
                    <% } %>
                </div>
                <div class="mb-3">
                    <label for="dataDiUscita" class="form-label">Data di uscita</label>
                    <input type="date" class="form-control" id="dataDiUscita" name="dataDiUscita" value="<%= dataDiUscita%>">
                </div>
                <div class="mb-3">
                    <label for="descrizione" class="form-label">Descrizione</label>
                    <textarea class="form-control" id="descrizione" name="descrizione"><%= descrizione%></textarea>
                </div>
                <div class="mb-3">
                    <label for="iva" class="form-label">IVA</label>
                    <input type="text" class="form-control" id="iva" name="iva" value="<%= iva%>">
                    <div class="form-text">
                        Il numero in questo campo deve avere anche la parte decimale
                    </div>
                </div>
                <div class="mb-3">
                    <label for="prezzo" class="form-label">Prezzo</label>
                    <input type="text" class="form-control" id="prezzo" name="prezzo" value="<%= prezzo%>">
                    <div class="form-text">
                        Il numero in questo campo deve avere anche la parte decimale
                    </div>
                </div>
                <div class="mb-3">
                    <label for="quantita" class="form-label">Quantità</label>
                    <input type="number" class="form-control" id="quantita" name="quantita" value="<%= quantita%>" min="0">
                </div>
                <div class="mb-3">
                    <label for="numImmagini" class="form-label">Numero di immagini</label>
                    <input type="number" class="form-control variable-content-amount" id="numImmagini" name="numImmagini" value="<%= numImmagini%>" min="1">
                </div>

                <div id="variable-content-container">
                    <% for(int i = 0; i < numImmagini; i++) { %>

                        <div class="mb-3">
                            <label for="immagine<%= i%>" class="form-label">Immagine <%= i%></label>
                            <input type="file" class="form-control" id="immagine<%= i%>" name="immagine<%= i%>" accept=".jpg">
                            <% if(modifyVersion && (i < numImmaginiAttuali)) { %>
                                <div class="form-text">
                                    Compilare questo campo solamente se si vuole sostituire l'immagine attuale
                                </div>
                            <% } %>
                        </div>

                    <% } %>
                </div>
                
                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#versioneModal">
                    <%= modifyVersion ? "Modifica" : "Inserisci"%>
                </button>
            </form>
            <div class="modal fade" id="versioneModal" tabindex="-1" aria-labelledby="versioneModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered text-dark">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" id="versioneModalLabel">Conferma</h5>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                      Sei sicuro di voler <%= modifyVersion ? "modificare" : "inserire"%> questa versione?
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annulla</button>
                      <button type="submit" form="versioneForm" class="btn btn-primary">Conferma</button>
                    </div>
                  </div>
                </div>
            </div>

            <% if(modifyVersion) { %>

                <h2 class="text-center">Immagini attualmente salvate</h2>

                <% for(int i = 0; i < numImmaginiAttuali; i++) { %>

                    <div class="text-center mb-3">
                        <p>Immagine <%= i%></p>
                        <div class="ratio ratio-21x9">
                            <div class="d-flex align-items-center">
                                <img class="d-block mx-auto mh-100 mw-100 h-auto w-auto" src="<%= sparkFiles%><%= versioneAggregata == null ? request.getParameter("immagineAttuale" + i) : versioneAggregata.getImmagini().get(i).getImg()%>" alt="immagine">
                            </div>
                        </div>
                    </div>

                <% } %>

            <% } %>

            <% if(request.getAttribute("prodottoNonEsistente") != null) { %>

                <p class="text-danger">Non esiste il prodotto ${param.prodotto}</p>

            <% } else if(request.getAttribute("SQLException") != null) { %>

                <p class="text-danger">Errore di comunicazione con il database</p>

            <% } else if(request.getAttribute("ivaErrata") != null) { %>

                <p class="text-danger">IVA inserita nel formato errato</p>

            <% } else if(request.getAttribute("prezzoErrato") != null) { %>

                <p class="text-danger">Prezzo inserito nel formato errato</p>

            <% } else if(request.getAttribute("almenoUnaImmagine") != null) { %>

                <p class="text-danger">Bisogna inserire almeno un'immagine</p>

            <% } else if(request.getAttribute("versioneEsistente") != null) { %>

                <p class="text-danger">Esiste già una versione con lo stesso codice</p>

            <% } %>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="<%= application.getContextPath()%>/scripts/variableContentImmagini.js"></script>
        <script src="<%= application.getContextPath()%>/scripts/FormUtil.js"></script>
        <script src="<%= application.getContextPath()%>/scripts/insertVersion.js"></script>
    </body>
</html>