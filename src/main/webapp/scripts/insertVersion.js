$(document).ready(
    function ()
    {
        $("#versioneForm").submit(
            function ()
            {
                let flag = true;

                if($(this).find("input[name='prodotto']").length != 0)
                {
                    if(!FormUtil.validateWordAllChars($("#codice")))
                        flag = false;

                    if(!FormUtil.validateFilled($("#icona")))
                        flag = false;
                }
            
                if(!FormUtil.validateFilled($("#nome")))
                    flag = false;
            
                if(!FormUtil.validateDate($("#dataDiUscita")))
                    flag = false;
            
                if(!FormUtil.validateFilled($("#descrizione")))
                    flag = false;

                if(!FormUtil.validateFloat($("#iva")))
                    flag = false;

                if(!FormUtil.validateFloat($("#prezzo")))
                    flag = false;

                if(!FormUtil.validateNumber($("#quantita")))
                    flag = false;
                
                if(!FormUtil.validateNumber($("#numImmagini")))
                    flag = false;
            
                const numImmagini = $("#numImmagini").val();
                let numImmaginiAttuali = $(this).find('input[name="numImmaginiAttuali"]').val();
                if(numImmaginiAttuali == null)
                    numImmaginiAttuali = 0;
                for(let i = numImmaginiAttuali; i < numImmagini; i++)
                {
                    if(!FormUtil.validateFilled($(`#immagine${i}`)))
                        flag = false;
                }
            
                if(!flag)
                    $("#versioneModal button[data-bs-dismiss='modal']").click();
            
                return flag;
            }
        );
    }
);