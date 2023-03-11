$(document).ready(
    function ()
    {
        $("#signUpForm").submit(
            function ()
            {
                let flag = true;

                if(!FormUtil.validateWord($("#nome")))
                    flag = false;

                if(!FormUtil.validateWord($("#cognome")))
                    flag = false;

                if(!FormUtil.validateEmail($("#email")))
                    flag = false;

                if(!FormUtil.validatePassword($("#password")))
                    flag = false;

                return flag;
            }
        );
    }
);