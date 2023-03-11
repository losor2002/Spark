<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="losor.model.bean.Prodotto, java.util.List, losor.model.bean.Categoria"%>

<%
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    List<Categoria> categorie = (List<Categoria>) request.getAttribute("categorie");
    boolean modifyProduct = request.getParameter("numProgressivo") != null;
    Integer numCategorie = 1;
    try
    {
        numCategorie = Integer.valueOf(request.getParameter("numCategorie"));
    }
    catch(NumberFormatException e) {}
    if(numCategorie <= 0)
        numCategorie = 1;

    if(categorie != null)
        numCategorie = categorie.size();

    String nome = prodotto == null ? (request.getParameter("nome") == null ? "" : request.getParameter("nome")) : prodotto.getNome();
    String produttore = prodotto == null ? (request.getParameter("produttore") == null ? "" : request.getParameter("produttore")) : prodotto.getProduttore();
    String iconaAttuale = prodotto == null ? (request.getParameter("iconaAttuale") == null ? "" : request.getParameter("iconaAttuale")) : prodotto.getIcona();

    String sparkFiles = getServletContext().getInitParameter("sparkFiles");
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= application.getContextPath()%>/images/spark-icon.ico" type="image/x-icon">
        <title><%= modifyProduct ? "Modifica il prodotto" : "Inserisci un prodotto"%></title>
    </head>
    <body class="bg-dark text-light">
        <div class="container">
            <h1 class="text-center"><%= modifyProduct ? "Modifica il prodotto" : "Inserisci un prodotto"%></h1>

            <% if(modifyProduct) { %>

                <div class="text-center">
                    <p>Questa è l'icona attuale</p>
                    <div class="ratio ratio-21x9">
                        <div class="d-flex align-items-center">
                            <img class="d-block mx-auto mh-100 mw-100 h-auto w-auto" src="<%= sparkFiles + iconaAttuale%>" alt="icona">
                        </div>
                    </div>
                </div>

            <% } %>

            <form id="prodottoForm" class="col-xl-9 mx-auto mb-3" action="<%= application.getContextPath()%><%= modifyProduct ? "/modifyProduct" : "/insertProduct"%>" method="post" enctype="multipart/form-data">

                <% if(modifyProduct) { %>

                    <input type="hidden" name="numProgressivo" value="${param.numProgressivo}">

                    <input type="hidden" name="iconaAttuale" value="<%= iconaAttuale%>">

                <% } %>

                <div class="mb-3">
                    <label for="nome" class="form-label">Nome</label>
                    <input type="text" class="form-control" id="nome" name="nome" value="<%= nome%>">
                </div>
                <div class="mb-3">
                    <label for="icona" class="form-label">Icona</label>
                    <input type="file" class="form-control" id="icona" name="icona" accept=".jpg">
                    <% if(modifyProduct) { %>
                        <div class="form-text">
                            Compilare questo campo solamente se si vuole sostituire l'icona attuale
                        </div>
                    <% } %>
                </div>
                <div class="mb-3">
                    <label for="produttore" class="form-label">Produttore</label>
                    <input type="text" class="form-control" id="produttore" name="produttore" value="<%= produttore%>">
                </div>
                <div class="mb-3">
                    <label for="numCategorie" class="form-label">Numero di categorie a cui appartiene il prodotto</label>
                    <input type="number" class="form-control variable-content-amount" id="numCategorie" name="numCategorie" value="<%= numCategorie%>" min="1">
                </div>

                <div id="variable-content-container">
                    <% for(int i = 0; i < numCategorie; i++) { %>
                        <div>
                            <div class="mb-3">
                                <label for="tipo<%= i%>" class="form-label">Tipo <%= i%></label>
                                <input type="text" class="form-control" id="tipo<%= i%>" name="tipo<%= i%>" value="<%= categorie == null ? (request.getParameter("tipo" + i) == null ? "" : request.getParameter("tipo" + i)) : categorie.get(i).getTipo()%>">
                            </div>
                            <div class="mb-3">
                                <label for="sottotipo<%= i%>" class="form-label">Sottotipo <%= i%></label>
                                <input type="text" class="form-control" id="sottotipo<%= i%>" name="sottotipo<%= i%>" value="<%= categorie == null ? (request.getParameter("sottotipo" + i) == null ? "" : request.getParameter("sottotipo" + i)) : categorie.get(i).getSottotipo()%>">
                            </div>
                        </div>
                    <% } %>
                </div>
                
                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#prodottoModal">
                    <%= modifyProduct ? "Modifica" : "Inserisci"%>
                </button>
            </form>
            <div class="modal fade" id="prodottoModal" tabindex="-1" aria-labelledby="prodottoModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered text-dark">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" id="prodottoModalLabel">Conferma</h5>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                      Sei sicuro di voler <%= modifyProduct ? "modificare" : "inserire"%> questo prodotto?
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annulla</button>
                      <button type="submit" form="prodottoForm" class="btn btn-primary">Conferma</button>
                    </div>
                  </div>
                </div>
            </div>

            <% if(request.getAttribute("prodottoEsistente") != null) { %>

                <p class="text-danger">Esiste già un prodotto con questo nome</p>

            <% } else if(request.getAttribute("SQLException") != null) { %>

                <p class="text-danger">Errore di comunicazione con il database</p>

            <% } %>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="<%= application.getContextPath()%>/scripts/variableContent.js"></script>
        <script src="<%= application.getContextPath()%>/scripts/FormUtil.js"></script>
        <script src="<%= application.getContextPath()%>/scripts/insertProduct.js"></script>
    </body>
</html>