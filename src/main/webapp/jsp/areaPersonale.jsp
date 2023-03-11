<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    String contextPath = application.getContextPath();
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= contextPath%>/images/spark-icon.ico" type="image/x-icon">
        <title>Area Personale</title>
    </head>
    <body class="bg-dark text-light">
        <jsp:include page="/header"/>
        <div class="container mb-5">
            <h1 class="mb-3">Area Personale</h1>
            <p>
                <span>Profilo</span>
                <span class="ms-5"><a class="text-decoration-none" href="<%= contextPath%>/areaPersonale/indirizzi">Indirizzi</a></span>
                <span class="ms-5"><a class="text-decoration-none" href="<%= contextPath%>/areaPersonale/carte">Carte</a></span>
                <span class="ms-5"><a class="text-decoration-none" href="<%= contextPath%>/areaPersonale/ordini">Ordini</a></span>
            </p>
            <div>
                <form action="<%= contextPath%>/modificaProfilo" method="post" class="mb-3 single-value-form" novalidate>
                    <input type="hidden" name="toChange" value="nome">
                    <label for="nome" class="form-label">Nome</label>
                    <div class="row justify-content-start">
                        <div class="col-10 col-lg-5">
                            <input type="text" class="form-control" name="value" id="nome" value="${requestScope.nome}" readonly>
                        </div>
                        <div class="col-2 col-lg-1">
                            <button type="submit" class="btn btn-primary">
                                <img class="img-fluid" src="<%= contextPath%>/images/pencil.svg" alt="pencil">
                            </button>
                        </div>
                        <div class="col-12 text-danger">
                            Errore di comunicazione col server
                        </div>
                    </div>
                </form>
                <form action="<%= contextPath%>/modificaProfilo" method="post" class="mb-3 single-value-form" novalidate>
                    <input type="hidden" name="toChange" value="cognome">
                    <label for="cognome" class="form-label">Cognome</label>
                    <div class="row justify-content-start">
                        <div class="col-10 col-lg-5">
                            <input type="text" class="form-control" name="value" id="cognome" value="${requestScope.cognome}" readonly>
                        </div>
                        <div class="col-2 col-lg-1">
                            <button type="submit" class="btn btn-primary">
                                <img class="img-fluid" src="<%= contextPath%>/images/pencil.svg" alt="pencil">
                            </button>
                        </div>
                        <div class="col-12 text-danger">
                            Errore di comunicazione col server
                        </div>
                    </div>
                </form>
                <form action="<%= contextPath%>/modificaProfilo" method="post" class="mb-3 single-value-form" novalidate>
                    <input type="hidden" name="toChange" value="email">
                    <label for="email" class="form-label">Email</label>
                    <div class="row justify-content-start">
                        <div class="col-10 col-lg-5">
                            <input type="email" class="form-control" name="value" id="email" value="${requestScope.email}" readonly>
                        </div>
                        <div class="col-2 col-lg-1">
                            <button type="submit" class="btn btn-primary">
                                <img class="img-fluid" src="<%= contextPath%>/images/pencil.svg" alt="pencil">
                            </button>
                        </div>
                        <div class="col-12 text-danger">
                            Errore di comunicazione col server
                        </div>
                    </div>
                </form>
                <p>
                    <button id="collapsePasswordButton" class="btn btn-primary" type="button" data-bs-toggle="collapse" data-bs-target="#collapsePasswordForm" aria-expanded="false" aria-controls="collapsePasswordForm">
                        Cambia password
                        <img class="img-fluid" src="<%= contextPath%>/images/check-lg.svg" alt="check-lg">
                    </button>
                </p>
                <div class="collapse" id="collapsePasswordForm">
                    <div class="row justify-content-start">
                        <div class="col-12 col-lg-6">
                            <div class="card card-body bg-dark border-light mb-3">
                                <form id="passwordForm" action="<%= contextPath%>/modificaProfilo" method="post" novalidate>
                                    <input type="hidden" name="toChange" value="password">
                                    <div class="mb-3">
                                      <label for="passwordAttuale" class="form-label">Password attuale</label>
                                      <input type="password" class="form-control" id="passwordAttuale" name="passwordAttuale">
                                    </div>
                                    <div class="mb-3">
                                      <label for="passwordNuova" class="form-label">Password nuova</label>
                                      <input type="password" class="form-control" id="passwordNuova" name="passwordNuova">
                                      <div class="form-text">
                                        La password deve contenere almeno 8 caratteri, di cui almeno una lettera maiuscola, almeno una minuscola, almeno un numero e almeno un carattere speciale
                                      </div>
                                    </div>
                                    <div>
                                      <button type="submit" class="btn btn-primary" id="passwordSubmit">Modifica</button>
                                      <span id="passwordErrore" class="text-danger float-end">Errore di comunicazione con il server</span>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="footer.jsp"%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="<%= contextPath%>/scripts/FormUtil.js"></script>
        <script src="<%= contextPath%>/scripts/modificaProfilo.js"></script>
    </body>
</html>