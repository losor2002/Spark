<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= application.getContextPath()%>/images/spark-icon.ico" type="image/x-icon">
        <title>Registrazione</title>
    </head>
    <body class="bg-dark text-light">
        <jsp:include page="/header"/>
        <div class="container">
            <div class="position-fixed top-50 start-50 translate-middle">
                <div class="row justify-content-center">
                    <div class="col-12 col-lg-10 col-xl-8 col-xxl-6">
                        <h1 class="text-center">Registrati a Spark</h1>
                        <form id="signUpForm" class="mb-3" action="<%= application.getContextPath()%>/userSignUp" method="post">
                            <div class="mb-3">
                                <label for="nome" class="form-label">Nome</label>
                                <input type="text" class="form-control" id="nome" name="nome" placeholder="Mario" value="${param.nome}">
                            </div>
                            <div class="mb-3">
                                <label for="cognome" class="form-label">Cognome</label>
                                <input type="text" class="form-control" id="cognome" name="cognome" placeholder="Rossi" value="${param.cognome}">
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" name="email" placeholder="mario.rossi@gmail.com" value="${param.email}">
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <input type="password" class="form-control" id="password" name="password" placeholder="********" value="${param.password}">
                                <div class="form-text">
                                    La password deve contenere almeno 8 caratteri, di cui almeno una lettera maiuscola, almeno una minuscola, almeno un numero e almeno un carattere speciale
                                </div>
                            </div>
                            <input type="submit" class="btn btn-primary" value="Registrati">
                        </form>

                        <% if(request.getAttribute("nomeVuoto") != null) { %>

                                <p class="text-danger">Nome non inserito</p>

                        <% } else if(request.getAttribute("cognomeVuoto") != null) { %>

                                <p class="text-danger">Cognome non inserito</p>
                        
                        <% } else if(request.getAttribute("emailVuota") != null) { %>

                                <p class="text-danger">Email non inserita</p>

                        <% } else if(request.getAttribute("passwordVuota") != null) { %>

                                <p class="text-danger">Password non inserita</p>

                        <% } else if(request.getAttribute("emailNonValida") != null) { %>

                                <p class="text-danger">Email non valida</p>

                        <% } else if(request.getAttribute("passwordNonValida") != null) { %>

                                <p class="text-danger">La password deve contenere almeno 8 caratteri, di cui almeno una lettera maiuscola, almeno una minuscola, almeno un numero e almeno un carattere speciale</p>

                        <% } else if(request.getAttribute("emailGiàRegistrata") != null) { %>

                                <p class="text-danger">Esiste già un utente con questa email</p>

                        <% } else if(request.getAttribute("SQLException") != null) { %>

                                <p class="text-danger">Si è verificato un errore temporaneo, riprovare più tardi</p>

                        <% } %>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="footer.jsp"%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="<%= application.getContextPath()%>/scripts/FormUtil.js"></script>
        <script src="<%= application.getContextPath()%>/scripts/signUp.js"></script>
    </body>
</html>