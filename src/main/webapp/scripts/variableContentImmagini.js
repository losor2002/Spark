$(document).ready(
    function ()
    {
        let numImmaginiAttuali = $("#versioneForm").find('input[name="numImmaginiAttuali"]').val();
        if(numImmaginiAttuali == null)
            numImmaginiAttuali = 0;

        const container = $("#variable-content-container");

        $("input.variable-content-amount").change(
            function ()
            {
                const newValue = $(this).val();
                const oldValue = container.children().length;

                if(newValue > oldValue)
                {
                    for(let i = oldValue; i < newValue; i++)
                    {
                        container.append(`<div class="mb-3">
                                              <label for="immagine${i}" class="form-label">Immagine ${i}</label>
                                              <input type="file" class="form-control" id="immagine${i}" name="immagine${i}" accept=".jpg">
                                              ${i < numImmaginiAttuali ? "<div class='form-text'>Compilare questo campo solamente se si vuole sostituire l'immagine attuale</div>" : ""}
                                          </div>`);
                    }
                }
                else if(newValue < oldValue)
                {
                    for(let i = newValue; i < oldValue; i++)
                    {
                        container.children().last().remove();
                    }
                }
            }
        ).keypress(
            function(e)
            {
                if(e.which == 13)
                {
                    $(this).change();
                    return false;
                }
            }
        );
    }
);