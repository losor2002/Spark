$(document).ready(
    function ()
    {
        $("#loginForm").submit(
            function ()
            {
                let flag = true;

                if(!FormUtil.validateEmail($("#email")))
                    flag = false;

                if(!FormUtil.validatePassword($("#password")))
                    flag = false;

                return flag;
            }
        );
    }
);