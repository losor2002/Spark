<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="container-fluid position-fixed bottom-0 w-100 bg-dark">
    <footer class="row justify-content-between align-items-center py-2">
      <div class="col-auto d-flex align-items-center">
        <span class="text-muted">© 2022 Spark, Inc</span>
      </div>
  
      <ul class="nav col-auto list-unstyled">
        <li class="ms-3">
            <a href="https://twitter.com/">
              <img class="img-fluid" src="<%= application.getContextPath()%>/images/twitter.svg" alt="twitter" width="30">
            </a>
        </li>
        <li class="ms-3">
            <a href="https://www.instagram.com/">
              <img class="img-fluid" src="<%= application.getContextPath()%>/images/instagram.svg" alt="instagram" width="30">
            </a>
        </li>
        <li class="ms-3">
            <a href="https://www.facebook.com/">
              <img class="img-fluid" src="<%= application.getContextPath()%>/images/facebook.svg" alt="facebook" width="30">
            </a>
        </li>
      </ul>
    </footer>
</div>