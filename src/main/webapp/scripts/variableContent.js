$(document).ready(
    function ()
    {
        $("input.variable-content-amount").change(
            function ()
            {
                const container = $("#variable-content-container");

                const newValue = $(this).val();
                const oldValue = container.children().length;

                if(newValue > oldValue)
                {
                    for(let i = oldValue; i < newValue; i++)
                    {
                        container.append(`<div>
                                              <div class="mb-3">
                                                  <label for="tipo${i}" class="form-label">Tipo ${i}</label>
                                                  <input type="text" class="form-control" id="tipo${i}" name="tipo${i}">
                                              </div>
                                              <div class="mb-3">
                                                  <label for="sottotipo${i}" class="form-label">Sottotipo ${i}</label>
                                                  <input type="text" class="form-control" id="sottotipo${i}" name="sottotipo${i}">
                                              </div>
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