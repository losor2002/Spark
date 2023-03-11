<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= application.getContextPath()%>/images/spark-icon.ico" type="image/x-icon">
        <title>Login</title>
    </head>
    <body class="bg-dark text-light">
        <% if(request.getAttribute("adminLogin") == null) { %>

                <jsp:include page="/header"/>

        <% } %>
        <div class="container">
                <div class="position-fixed top-50 start-50 translate-middle">
                        <h1 class="text-center">Accedi a Spark<%= request.getAttribute("adminLogin") == null ? "" : "<br>come Amministratore"%></h1>
                        <form id="loginForm" class="mb-3" action="<%= request.getAttribute("filteredResource") == null ? application.getContextPath() + "/userLogin" : request.getAttribute("filteredResource")%>" method="post">
                                <div class="mb-3">
                                        <label for="email" class="form-label">Indirizzo Email</label>
                                        <input type="email" class="form-control" id="email" name="email" placeholder="mario.rossi@gmail.com" value="${param.email}" autofocus>
                                </div>
                                <div class="mb-3">
                                        <label for="password" class="form-label">Password</label>
                                        <input type="password" class="form-control" id="password" name="password" placeholder="********" value="${param.password}">
                                </div>
                                <input type="submit" class="btn btn-primary" value="Login">
                        </form>
                        <% if(request.getAttribute("adminLogin") == null) { %>

                                <p>
                                        Non sei ancora iscritto? <a class="text-decoration-none float-end" href="<%= application.getContextPath()%>/userSignUp">Iscriviti</a>
                                </p>

                        <% } %>

                        <% if(request.getAttribute("emailVuota") != null) { %>

                                <p class="text-danger">Email non inserita</p>

                        <% } else if(request.getAttribute("passwordVuota") != null) { %>

                                <p class="text-danger">Password non inserita</p>
                        
                        <% } else if(request.getAttribute("SQLException") != null) { %>

                                <p class="text-danger">Si è verificato un errore temporaneo, riprovare più tardi</p>

                        <% } else if(request.getAttribute("invalidEmail") != null) { %>

                                <p class="text-danger">Email non valida</p>

                        <% } else if(request.getAttribute("invalidPassword") != null) { %>

                                <p class="text-danger">Password non valida</p>

                        <% } else if(request.getAttribute("emailNotPresent") != null) { %>

                                <p class="text-danger">Nessun <%= request.getAttribute("adminLogin") == null ? "utente" : "amministratore"%> è registrato con questa email</p>
                        
                        <% } else if(request.getAttribute("wrongPassword") != null) { %>

                                <p class="text-danger">Password errata</p>

                        <% } %>
                </div>
        </div>
        <% if(request.getAttribute("adminLogin") == null) { %>

                <%@ include file="footer.jsp"%>

        <% } %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="<%= application.getContextPath()%>/scripts/FormUtil.js"></script>
        <script src="<%= application.getContextPath()%>/scripts/login.js"></script>
    </body>
</html>