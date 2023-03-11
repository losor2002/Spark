<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, losor.model.bean.Prodotto"%>

<%
    String contextPath = application.getContextPath();
    List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("prodotti");
    String sparkFiles = getServletContext().getInitParameter("sparkFiles");
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= contextPath%>/images/spark-icon.ico" type="image/x-icon">
        <title>Home Amministratore</title>
    </head>
    <body class="bg-dark text-light">
        <div class="container">
            <h1 class="text-center mb-3">Benvenuto su Spark Amministratore ${requestScope.nome} ${requestScope.cognome}</h1>

            <p class="text-center">
                <a class="text-decoration-none" href="<%= contextPath%>/home">Torna alla home del sito</a>
            </p>

            <p class="text-center">
                <a class="text-decoration-none" href="<%= contextPath%>/logout?who=admin">Logout</a>
            </p>

            <p class="text-center">
                <a class="text-decoration-none" href="<%= contextPath%>/utenti">Visualizza gli utenti</a>
            </p>

            <p class="text-center">
                <a class="text-decoration-none" href="<%= contextPath%>/adminOrdini">Visualizza gli ordini</a>
            </p>

            <p class="text-center">
                <a class="text-decoration-none" href="<%= contextPath%>/insertProduct">Inserisci un prodotto</a>
            </p>

            <h3>Prodotti presenti nel database</h3>
            <table class="table table-dark table-hover align-middle text-center">

                <tr>
                    <th>Numero Progressivo</th>
                    <th>Nome</th>
                    <th>Icona</th>
                    <th>Produttore</th>
                    <th>Gestione</th>
                </tr>

                <% for(Prodotto prodotto : prodotti) { %>

                    <tr>
                        <td><%= prodotto.getNumProgressivo()%></td>
                        <td><%= prodotto.getNome()%></td>
                        <td class="col-3 col-lg-2">
                            <div class="ratio ratio-1x1">
                                <div class="d-flex align-items-center">
                                    <img class="d-block mx-auto mh-100 mw-100 h-auto w-auto" src="<%= sparkFiles + prodotto.getIcona()%>" alt="icona">
                                </div>
                            </div>
                        </td>
                        <td><%= prodotto.getProduttore()%></td>
                        <td>
                            <a class="text-decoration-none" href="<%= contextPath%>/modifyProduct?numProgressivo=<%= prodotto.getNumProgressivo()%>">Modifica</a>
                            <br><br>
                            <button type="button" class="btn btn-link text-decoration-none" data-bs-toggle="modal" data-bs-target="#modal_<%= prodotto.getNumProgressivo()%>">
                                Elimina
                            </button>
                            <br><br>
                            <a class="text-decoration-none" href="<%= contextPath%>/viewVersions?prodotto=<%= prodotto.getNumProgressivo()%>">Visualizza le versioni</a>
                        </td>
                        <div class="modal fade" id="modal_<%= prodotto.getNumProgressivo()%>" tabindex="-1" aria-labelledby="ModalLabel_<%= prodotto.getNumProgressivo()%>" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered text-dark">
                              <div class="modal-content">
                                <div class="modal-header">
                                  <h5 class="modal-title" id="ModalLabel_<%= prodotto.getNumProgressivo()%>">Conferma</h5>
                                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                  Sei sicuro di voler eliminare <%= prodotto.getNome()%>?
                                </div>
                                <div class="modal-footer">
                                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annulla</button>
                                  <a class="btn btn-primary" href="<%= contextPath%>/deleteProduct?numProgressivo=<%= prodotto.getNumProgressivo()%>">Conferma</a>
                                </div>
                              </div>
                            </div>
                        </div>
                    </tr>

                <% } %>

            </table>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    </body>
</html>