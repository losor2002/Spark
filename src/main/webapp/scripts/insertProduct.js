$(document).ready(
    function ()
    {
        $("#prodottoForm").submit(
            function ()
            {
                let flag = true;
            
                if(!FormUtil.validateFilled($("#nome")))
                    flag = false;
            
                if(!FormUtil.validateFilled($("#produttore")))
                    flag = false;
            
                if(!FormUtil.validateNumber($("#numCategorie")))
                    flag = false;
            
                $("[id^='tipo'], [id^='sottotipo']").each(
                    function ()
                    {
                        if(!FormUtil.validateWords($(this)))
                            flag = false;
                    }
                );
                
                if($(this).find("input[name='numProgressivo']").length == 0)
                {
                    if(!FormUtil.validateFilled($("#icona")))
                        flag = false;
                }
            
                if(!flag)
                    $("#prodottoModal button[data-bs-dismiss='modal']").click();
            
                return flag;
            }
        );
    }
);