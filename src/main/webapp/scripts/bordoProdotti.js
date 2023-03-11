$(document).ready(
    function()
    {
        $(".prodotto").hover(
            function()
            {
                $(this).addClass("border border-primary border-4");
            },
            function()
            {
                $(this).removeClass("border border-primary border-4");
            }
        );
    }
);