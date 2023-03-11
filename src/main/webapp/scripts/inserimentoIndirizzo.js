$(document).ready(
    function()
    {
        let indirizzoErroreText = $("#indirizzoErrore").hide().text();

        $("#nomeIndirizzo").blur(
            function()
            {
                FormUtil.validateWord($(this));
            }
        );

        $("#cognomeIndirizzo").blur(
            function()
            {
                FormUtil.validateWord($(this));
            }
        );

        $("#via").blur(
            function()
            {
                FormUtil.validateWords($(this));
            }
        );

        $("#numCivico").blur(
            function()
            {
                FormUtil.validateNumber($(this));
            }
        );

        $("#cap").blur(
            function()
            {
                FormUtil.validateNumber($(this), 5);
            }
        );

        $("#indirizzoSubmit").click(
            function()
            {
                let nome = $("#nomeIndirizzo");
                let cognome = $("#cognomeIndirizzo");
                let via = $("#via");
                let numCivico = $("#numCivico");
                let cap = $("#cap");
                let flag = true;

                if(!FormUtil.validateWord(nome))
                    flag = false;

                if(!FormUtil.validateWord(cognome))
                    flag = false;

                if(!FormUtil.validateWords(via))
                    flag = false;

                if(!FormUtil.validateNumber(numCivico))
                    flag = false;

                if(!FormUtil.validateNumber(cap, 5))
                    flag = false;

                if(!flag)
                    return;

                let submitButtonText = $(this).html();
                FormUtil.loadingButton($(this), "Inserimento...");

                $.post($("#indirizzoAction").val(),
                    FormUtil.setParameters(nome, cognome, via, numCivico, cap),
                    function(indirizzo)
                    {
                        if(indirizzo.errorMessage != null)
                        {
                            FormUtil.resetLoadingButton($("#indirizzoSubmit"), submitButtonText);

                            $("#indirizzoErrore").text(indirizzo.errorMessage).show();
                            return;
                        }

                        $("#indirizziContainer").append(
                            `<div class="col mb-3">
                                <input form="ordineForm" type="radio" class="btn-check" name="indirizzo" id="indirizzo_${indirizzo.numProgressivo}" value="${indirizzo.numProgressivo}" required>
                                <label class="btn btn-outline-primary" for="indirizzo_${indirizzo.numProgressivo}">
                                    ${indirizzo.nome} ${indirizzo.cognome}<br>
                                    ${indirizzo.via}, ${indirizzo.numCivico}<br>
                                    ${indirizzo.cap}
                                </label>
                            </div>`
                        );

                        FormUtil.empty($("#nomeIndirizzo"), $("#cognomeIndirizzo"), $("#via"), $("#numCivico"), $("#cap"));

                        FormUtil.resetLoadingButton($("#indirizzoSubmit"), submitButtonText);

                        $("#indirizzoErrore").hide();

                        $("#collapseIndirizzoButton").click();

                        $("input[form='ordineForm']").click(
                            function()
                            {
                                if($("input[form='ordineForm']:checked").length == 2)
                                    $("#ordineFormSubmit").prop("disabled", false);
                            }
                        );
                    },
                    'json'
                ).fail(
                    function()
                    {
                        FormUtil.resetLoadingButton($("#indirizzoSubmit"), submitButtonText);

                        $("#indirizzoErrore").text(indirizzoErroreText).show();
                    }
                );
            }
        );
    }
);