<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, losor.model.bean.Carrello, losor.model.bean.Prodotto, losor.model.util.VersioneAggregata, java.math.BigDecimal, losor.model.bean.Indirizzo, losor.model.bean.Carta"%>

<%
    String contextPath = application.getContextPath();
    String sparkFiles = getServletContext().getInitParameter("sparkFiles");
    List<Carrello> carrello = (List<Carrello>) request.getAttribute("carrello");
    List<VersioneAggregata> versioniAggregate = (List<VersioneAggregata>) request.getAttribute("versioniAggregate");
    List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("prodotti");
    List<Carta> carte = (List<Carta>) request.getAttribute("carte");
    List<Indirizzo> indirizzi = (List<Indirizzo>) request.getAttribute("indirizzi");
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= contextPath%>/images/spark-icon.ico" type="image/x-icon">
        <title>Effettua l'ordine</title>
    </head>
    <body class="bg-dark text-light">
        <jsp:include page="/header"/>
        <div class="container mb-5">
            <h1 class="mb-3">Effettua l'ordine</h1>
            <form action="<%= contextPath%>/ordine" method="post" id="ordineForm"></form>
            <div class="row">
                <div class="col-12 col-xl-8">
                    <div>
                        <h3 class="mb-3">Indirizzi</h3>
                        <div id="indirizziContainer" class="row text-center">
                            <%boolean first = true; for(Indirizzo indirizzo : indirizzi) { %>
                                <div class="col mb-3">
                                    <input form="ordineForm" type="radio" class="btn-check" name="indirizzo" id="indirizzo_<%= indirizzo.getNumProgressivo()%>" <%= first ? "checked" : ""%> value="<%= indirizzo.getNumProgressivo()%>" required>
                                    <label class="btn btn-outline-primary" for="indirizzo_<%= indirizzo.getNumProgressivo()%>">
                                        <%= indirizzo.getNome()%> <%= indirizzo.getCognome()%><br>
                                        <%= indirizzo.getVia()%>, <%= indirizzo.getNumCivico()%><br>
                                        <%= indirizzo.getCap()%>
                                    </label>
                                </div>
                            <% first = false; } %>
                        </div>
                        <p>
                            <button id="collapseIndirizzoButton" class="btn btn-link text-decoration-none" type="button" data-bs-toggle="collapse" data-bs-target="#collapseIndirizzoForm" aria-expanded="false" aria-controls="collapseIndirizzoForm">
                                Aggiungi un nuovo indirizzo
                            </button>
                        </p>
                        <div class="collapse" id="collapseIndirizzoForm">
                            <div class="card card-body bg-dark border-light mb-3">
                                <div class="row g-3">
                                    <input type="hidden" id="indirizzoAction" value="<%= contextPath%>/insertIndirizzo">
                                    <div class="col-md-6">
                                      <label for="nomeIndirizzo" class="form-label">Nome</label>
                                      <input type="text" class="form-control" id="nomeIndirizzo" name="nome">
                                    </div>
                                    <div class="col-md-6">
                                      <label for="cognomeIndirizzo" class="form-label">Cognome</label>
                                      <input type="text" class="form-control" id="cognomeIndirizzo" name="cognome">
                                    </div>
                                    <div class="col-12">
                                      <label for="via" class="form-label">Via</label>
                                      <input type="text" class="form-control" id="via" name="via">
                                    </div>
                                    <div class="col-md-6">
                                      <label for="numCivico" class="form-label">Civico</label>
                                      <input type="text" class="form-control" id="numCivico" name="numCivico">
                                    </div>
                                    <div class="col-md-6">
                                      <label for="cap" class="form-label">Cap</label>
                                      <input type="text" class="form-control" id="cap" name="cap">
                                    </div>
                                    <div class="col-12">
                                      <button type="button" class="btn btn-primary" id="indirizzoSubmit">Inserisci</button>
                                      <span id="indirizzoErrore" class="text-danger float-end">Errore di comunicazione con il server</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div>
                        <h3 class="mb-3">Carte di credito</h3>
                        <div id="carteContainer" class="row text-center">
                            <%first = true; for(Carta carta : carte) { %>
                                <div class="col mb-3">
                                    <input form="ordineForm" type="radio" class="btn-check" name="carta" id="carta_<%= carta.getNumero()%>" <%= first ? "checked" : ""%> value="<%= carta.getNumero()%>" required>
                                    <label class="btn btn-outline-primary" for="carta_<%= carta.getNumero()%>">
                                        <%= carta.getNome()%> <%= carta.getCognome()%><br>
                                        *<%= carta.getNumero().substring(carta.getNumero().length() - 4)%><br>
                                        <%= carta.getScadenza()%>
                                    </label>
                                </div>
                            <% first = false; } %>
                        </div>
                        <p>
                            <button id="collapseCartaButton" class="btn btn-link text-decoration-none" type="button" data-bs-toggle="collapse" data-bs-target="#collapseCartaForm" aria-expanded="false" aria-controls="collapseCartaForm">
                                Aggiungi una nuova carta
                            </button>
                        </p>
                        <div class="collapse" id="collapseCartaForm">
                            <div class="card card-body bg-dark border-light mb-3">
                                <div class="row g-3">
                                    <input type="hidden" id="cartaAction" value="<%= contextPath%>/insertCarta">
                                    <div class="col-md-6">
                                      <label for="nomeCarta" class="form-label">Nome</label>
                                      <input type="text" class="form-control" id="nomeCarta" name="nome">
                                    </div>
                                    <div class="col-md-6">
                                      <label for="cognomeCarta" class="form-label">Cognome</label>
                                      <input type="text" class="form-control" id="cognomeCarta" name="cognome">
                                    </div>
                                    <div class="col-12">
                                      <label for="numeroCarta" class="form-label">Numero</label>
                                      <input type="text" class="form-control" id="numeroCarta" name="numero">
                                    </div>
                                    <div class="col-md-6">
                                      <label for="scadenza" class="form-label">Scadenza</label>
                                      <input type="date" class="form-control" id="scadenza" name="scadenza">
                                    </div>
                                    <div class="col-md-6">
                                      <label for="cvc" class="form-label">CVC</label>
                                      <input type="text" class="form-control" id="cvc" name="cvc">
                                    </div>
                                    <div class="col-12">
                                      <button type="button" class="btn btn-primary" id="cartaSubmit">Inserisci</button>
                                      <span id="cartaErrore" class="text-danger float-end">Errore di comunicazione con il server</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-12 col-xl-4">
                    <h3 class="mb-3">Riepilogo</h3>

                    <%
                        BigDecimal totale = new BigDecimal(0);
                        for(int i = 0; i < carrello.size(); i++)
                        {
                            BigDecimal quantita = new BigDecimal(carrello.get(i).getQuantita());
                            BigDecimal prezzo = versioniAggregate.get(i).getDatiVersione().getPrezzo();
                            BigDecimal totaleVersione = prezzo.multiply(quantita);
                            totale = totale.add(totaleVersione);
                    %>

                            <p>
                                <small>
                                    <%= prodotti.get(i).getNome()%> - <%= versioniAggregate.get(i).getVersione().getNome()%> x <%= carrello.get(i).getQuantita()%> <span class="float-end"><%= totaleVersione%> €</span>
                                </small>
                            </p>

                    <%
                        }
                    %>

                    <p>Totale <span class="float-end"><%= totale%> €</span></p>

                    <div class="d-grid">
                        <button id="ordineFormSubmit" class="btn btn-primary" type="submit" form="ordineForm" <%= indirizzi.isEmpty() || carte.isEmpty() ? "disabled" : ""%>>
                            Ordina
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="footer.jsp"%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="<%= contextPath%>/scripts/FormUtil.js"></script>
        <script src="<%= contextPath%>/scripts/inserimentoIndirizzo.js"></script>
        <script src="<%= contextPath%>/scripts/inserimentoCarta.js"></script>
    </body>
</html>