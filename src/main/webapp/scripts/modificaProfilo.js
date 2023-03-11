$(document).ready(
    function ()
    {
        $("#collapsePasswordButton > img").hide();

        let defaultError = $("form .text-danger").hide().html();

        let defaultButtonHtml = $("form.single-value-form button[type='submit']").click(enableModification).html();

        $("form.single-value-form").submit(
            function (event)
            {
                event.preventDefault();

                let form = $(this);
                let toChange = form.find("input[name='toChange']").val();
                let value = form.find("input[name='value']");
                let flag = false;

                if (toChange == "nome" || toChange == "cognome")
                    flag = FormUtil.validateWord(value);
                else if (toChange == "email")
                    flag = FormUtil.validateEmail(value);

                if(!flag)
                    return;

                let button = form.find("button[type='submit']");
                let buttonHtml = button.html();
                FormUtil.loadingButton(button, "");

                $.ajax({
                  type: form.attr("method"),
                  url: form.attr("action"),
                  data: form.serialize(),
                  dataType: "json"
                }).done(
                    function (data)
                    {
                        if(data.errorMessage != null)
                        {
                            FormUtil.resetLoadingButton(button, buttonHtml);
                            form.find(".text-danger").text(data.errorMessage).show();
                            value.removeClass("is-valid").addClass("is-invalid");
                            return;
                        }

                        value.val(data.value).prop("readonly", true).removeClass("is-valid");
                        FormUtil.resetLoadingButton(button, defaultButtonHtml);
                        button.click(enableModification);
                        form.find(".text-danger").hide();

                        if (toChange == "nome" || toChange == "cognome")
                            $("#dropdown_user").text(` Ciao, ${$("#nome").val()} ${$("#cognome").val()} `);
                    }
                ).fail(
                    function ()
                    {
                        FormUtil.resetLoadingButton(button, buttonHtml);
                        form.find(".text-danger").html(defaultError).show();
                    }
                );
            }
        );

        $("#passwordForm").submit(
            function (event)
            {
                event.preventDefault();

                let passwordAttuale = $("#passwordAttuale");
                let passwordNuova = $("#passwordNuova");
                let flag = true;

                if(!FormUtil.validatePassword(passwordAttuale))
                    flag = false;

                if(!FormUtil.validatePassword(passwordNuova))
                    flag = false;

                if(!flag)
                    return;

                let form = $(this);
                let button = form.find("button[type='submit']");
                let buttonHtml = button.html();
                FormUtil.loadingButton(button, "Modificando...");

                $.ajax({
                  type: form.attr("method"),
                  url: form.attr("action"),
                  data: form.serialize(),
                  dataType: "json"
                }).done(
                    function (data)
                    {
                        if(data.errorMessage != null)
                        {
                            FormUtil.resetLoadingButton(button, buttonHtml);
                            form.find(".text-danger").text(data.errorMessage).show();
                            $("#collapsePasswordButton > img").hide();
                            passwordAttuale.removeClass("is-valid").addClass("is-invalid");
                            return;
                        }

                        FormUtil.empty(passwordAttuale, passwordNuova);
                        FormUtil.resetLoadingButton(button, buttonHtml);
                        form.find(".text-danger").hide();
                        $("#collapsePasswordButton").click().children().show();
                    }
                ).fail(
                    function ()
                    {
                        FormUtil.resetLoadingButton(button, buttonHtml);
                        form.find(".text-danger").html(defaultError).show();
                        $("#collapsePasswordButton > img").hide();
                    }
                );
            }
        );
    }
);

function enableModification(event)
{
    event.preventDefault();
    
    $(this).off('click').children().attr({src: "/spark/images/check-lg.svg", alt: "check-lg"});

    $(this).parentsUntil("form", ".row").find("input[name='value']").prop("readonly", false);
}