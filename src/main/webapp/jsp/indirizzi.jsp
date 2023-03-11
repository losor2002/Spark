<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, losor.model.bean.Indirizzo"%>

<%
    String contextPath = application.getContextPath();
    List<Indirizzo> indirizzi = (List<Indirizzo>) request.getAttribute("indirizzi");
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= contextPath%>/images/spark-icon.ico" type="image/x-icon">
        <title>Area Personale | Indirizzi</title>
    </head>
    <body class="bg-dark text-light">
        <jsp:include page="/header"/>
        <div class="container mb-5">
            <h1 class="mb-3">Area Personale</h1>
            <p>
                <span><a class="text-decoration-none" href="<%= contextPath%>/areaPersonale">Profilo</a></span>
                <span class="ms-5">Indirizzi</span>
                <span class="ms-5"><a class="text-decoration-none" href="<%= contextPath%>/areaPersonale/carte">Carte</a></span>
                <span class="ms-5"><a class="text-decoration-none" href="<%= contextPath%>/areaPersonale/ordini">Ordini</a></span>
            </p>
            <div>
                <% for(Indirizzo indirizzo : indirizzi) { %>
                    <div class="card-and-modal-container">
                        <div class="card card-body bg-dark border-light mb-3">
                            <div class="row">
                                <div class="col-10">
                                    <%= indirizzo.getNome()%> <%= indirizzo.getCognome()%><br>
                                    <%= indirizzo.getVia()%>, <%= indirizzo.getNumCivico()%><br>
                                    <%= indirizzo.getCap()%>
                                </div>
                                <div class="col-2 d-flex align-items-center justify-content-end">
                                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modal_<%= indirizzo.getNumProgressivo()%>">
                                        <img class="img-fluid" src="<%= contextPath%>/images/trash.svg" alt="trash">
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="modal fade" id="modal_<%= indirizzo.getNumProgressivo()%>" tabindex="-1" aria-labelledby="ModalLabel_<%= indirizzo.getNumProgressivo()%>" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered text-dark">
                              <div class="modal-content">
                                <div class="modal-header">
                                  <h5 class="modal-title" id="ModalLabel_<%= indirizzo.getNumProgressivo()%>">Conferma</h5>
                                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                  Sei sicuro di voler eliminare questo indirizzo?
                                </div>
                                <div class="modal-footer">
                                  <div class="text-danger flex-grow-1">Errore di comunicazione col server</div>
                                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annulla</button>
                                  <form action="<%= contextPath%>/deleteIndirizzo" method="post">
                                        <input type="hidden" name="numProgressivo" value="<%= indirizzo.getNumProgressivo()%>">
                                        <button type="submit" class="btn btn-primary">
                                            Conferma
                                        </button>
                                  </form>
                                </div>
                              </div>
                            </div>
                        </div>
                    </div>
                <% } %>
            </div>
        </div>
        <%@ include file="footer.jsp"%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="<%= contextPath%>/scripts/FormUtil.js"></script>
        <script src="<%= contextPath%>/scripts/deleteIndirizzoAndCarta.js"></script>
    </body>
</html>