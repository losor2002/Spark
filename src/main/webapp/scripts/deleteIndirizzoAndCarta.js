$(document).ready(
    function ()
    {
        $(".card-and-modal-container div.text-danger").hide();

        $(".card-and-modal-container form").submit(
            function (event)
            {
                event.preventDefault();

                let form = $(this);
                let button = form.find("button[type='submit']");
                let buttonHtml = button.html();
                FormUtil.loadingButton(button, "Cancellazione...");

                $.ajax({
                  type: form.attr("method"),
                  url: form.attr("action"),
                  data: form.serialize(),
                  dataType: "json"
                }).done(
                    function ()
                    {
                        FormUtil.resetLoadingButton(button, buttonHtml);
                        form.parent().find("div.text-danger").hide();
                        form.parent().find('button[data-bs-dismiss="modal"]').click();
                        form.parents("div.card-and-modal-container").remove();
                    }
                ).fail(
                    function ()
                    {
                        FormUtil.resetLoadingButton(button, buttonHtml);
                        form.parent().find("div.text-danger").show();
                    }
                );
            }
        );
    }
);