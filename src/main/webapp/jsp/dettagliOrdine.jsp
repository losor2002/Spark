<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, losor.model.bean.Ordine, losor.model.bean.Prodotto, losor.model.bean.Versione, losor.model.bean.Indirizzo, losor.model.bean.Composizione, losor.model.bean.Cliente"%>

<%
    String contextPath = application.getContextPath();
    String sparkFiles = getServletContext().getInitParameter("sparkFiles");
    Ordine ordine = (Ordine) request.getAttribute("ordine");
    Indirizzo indirizzo = (Indirizzo) request.getAttribute("indirizzo");
    List<Composizione> composizioni = (List<Composizione>) request.getAttribute("composizioni");
    List<Versione> versioni = (List<Versione>) request.getAttribute("versioni");
    List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("prodotti");
    Cliente cliente = (Cliente) request.getAttribute("cliente");
    boolean isAdmin = request.getAttribute("admin") != null;
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= contextPath%>/images/spark-icon.ico" type="image/x-icon">
        <title>Dettagli ordine #<%= ordine.getNumFattura()%></title>
    </head>
    <body class="bg-dark text-light">
        <% if(!isAdmin) { %>
            <jsp:include page="/header"/>
        <% } %>
        <div class="container<%= isAdmin ? "" : " mb-5"%>">
            <h1>Dettagli ordine #<%= ordine.getNumFattura()%></h1>

            <div class="row mb-3">
                <div class="col-12 col-xl-9">
                    <h3 class="mb-3">Prodotti</h3>

                    <% for(int i = 0; i < composizioni.size(); i++) { Composizione composizione = composizioni.get(i); Versione versione = versioni.get(i); Prodotto prodotto = prodotti.get(i);%>

                        <div class="card mb-3">
                            <div class="row g-0">
                              <div class="col-4">
                                <div class="ratio ratio-1x1">
                                    <div class="d-flex align-items-center">
                                        <img src="<%= sparkFiles + versione.getIcona()%>" class="rounded d-block me-auto mh-100 mw-100 h-auto w-auto" alt="icona">
                                    </div>
                                </div>
                              </div>
                              <div class="col-8">
                                <div class="card-body text-dark">
                                    <div class="row">
                                        <div class="col-auto">
                                            <a href="<%= contextPath%>/productDetails?prodotto=<%= prodotto.getNumProgressivo()%>&versione=<%= composizione.getVersione()%>" class="text-decoration-none">
                                                <h5 class="card-title"><%= prodotto.getNome()%></h5>
                                                <p class="card-text"><small><%= versione.getNome()%></small></p>
                                            </a>
                                        </div>
                                        <div class="col-auto ms-auto">
                                            <h5>
                                                <%= composizione.getPrezzo()%> €
                                            </h5>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-body text-dark position-absolute bottom-0 end-0">
                                    <div class="text-end">
                                        Quantità <%= composizione.getQuantita()%>
                                    </div>
                                </div>
                              </div>
                            </div>
                        </div>

                    <% } %>

                </div>
                <div class="col-12 col-xl-3">
                    <h3 class="mb-3">Riepilogo</h3>

                    <% if(isAdmin) { %>
                        <p>Cliente <span class="float-end"><%= cliente.getCodice()%>. <%= cliente.getNome()%> <%= cliente.getCognome()%></span></p>
                    <% } %>

                    <p>Totale <span class="float-end"><%= ordine.getPrezzoTot()%> €</span></p>

                    <p>Data <span class="float-end"><%= ordine.getData()%></span></p>

                    <p>Numero Carta <span class="float-end">*<%= ordine.getNumCarta().substring(ordine.getNumCarta().length() - 4)%></span></p>

                    <p>
                        <div class="mb-1">Indirizzo</div>
                        <div>
                            <%= indirizzo.getNome()%> <%= indirizzo.getCognome()%><br>
                            <%= indirizzo.getVia()%>, <%= indirizzo.getNumCivico()%><br>
                            <%= indirizzo.getCap()%>
                        </div>
                    </p>

                    <% if(!isAdmin) { %>
                        <div class="d-grid">
                            <a class="btn btn-primary" href="<%= contextPath%>/fattura?numFattura=<%= ordine.getNumFattura()%>">Visualizza la fattura</a>
                        </div>
                    <% } %>
                </div>
            </div>
        </div>
        <% if(!isAdmin) { %>
            <%@ include file="footer.jsp"%>
        <% } %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    </body>
</html>