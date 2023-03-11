$(document).ready(
    function()
    {
        let cartaErroreText = $("#cartaErrore").hide().html();

        $("#nomeCarta").blur(
            function()
            {
                FormUtil.validateWord($(this));
            }
        );

        $("#cognomeCarta").blur(
            function()
            {
                FormUtil.validateWord($(this));
            }
        );

        $("#numeroCarta").blur(
            function()
            {
                FormUtil.validateNumber($(this), 13, 16);
            }
        );

        $("#scadenza").blur(
            function()
            {
                FormUtil.validateDate($(this));
            }
        );

        $("#cvc").blur(
            function()
            {
                FormUtil.validateNumber($(this), 3, 4);
            }
        );

        $("#cartaSubmit").click(
            function()
            {
                let nome = $("#nomeCarta");
                let cognome = $("#cognomeCarta");
                let numCarta = $("#numeroCarta");
                let scadenza = $("#scadenza");
                let cvc = $("#cvc");
                let flag = true;

                if(!FormUtil.validateWord(nome))
                    flag = false;

                if(!FormUtil.validateWord(cognome))
                    flag = false;

                if(!FormUtil.validateNumber(numCarta, 13, 16))
                    flag = false;

                if(!FormUtil.validateDate(scadenza))
                    flag = false;

                if(!FormUtil.validateNumber(cvc, 3, 4))
                    flag = false;

                if(!flag)
                    return;

                let submitButtonText = $(this).html();
                FormUtil.loadingButton($(this), "Inserimento...");

                $.post($("#cartaAction").val(),
                    FormUtil.setParameters(nome, cognome, numCarta, scadenza, cvc),
                    function(carta)
                    {
                        if(carta.errorMessage != null)
                        {
                            FormUtil.resetLoadingButton($("#cartaSubmit"), submitButtonText);
                            numCarta.removeClass("is-valid").addClass("is-invalid");
                            $("#cartaErrore").html(`<small>${carta.errorMessage}</small>`).show();
                            return;
                        }

                        $("#carteContainer").append(
                            `<div class="col mb-3">
                                <input form="ordineForm" type="radio" class="btn-check" name="carta" id="carta_${carta.numero}" value="${carta.numero}" required>
                                <label class="btn btn-outline-primary" for="carta_${carta.numero}">
                                    ${carta.nome} ${carta.cognome}<br>
                                    *${carta.numero.substr(-4)}<br>
                                    ${carta.scadenza}
                                </label>
                            </div>`
                        );

                        FormUtil.empty($("#nomeCarta"), $("#cognomeCarta"), $("#numeroCarta"), $("#scadenza"), $("#cvc"));

                        FormUtil.resetLoadingButton($("#cartaSubmit"), submitButtonText);

                        $("#cartaErrore").hide();

                        $("#collapseCartaButton").click();

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
                        FormUtil.resetLoadingButton($("#cartaSubmit"), submitButtonText);

                        $("#cartaErrore").html(cartaErroreText).show();
                    }
                );
            }
        );
    }
);