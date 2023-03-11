class FormUtil
{
    /**
     * 
     * @param {jQuery} field 
     * @param {RegExp} regex 
     * @returns boolean
     */
    static validateField(field, regex)
    {
        if(!(field instanceof jQuery) || !(regex instanceof RegExp))
            throw new TypeError();

        field.removeClass("is-valid is-invalid");

        if(field.val().match(regex) == null)
        {
            field.addClass("is-invalid");
            return false;
        }
        else
        {
            field.addClass("is-valid");
            return true;
        }
    }

    /**
     * 
     * @param  {...jQuery} fields 
     */
    static empty(...fields)
    {
        for(const field of fields)
        {
            if(!(field instanceof jQuery))
                throw new TypeError();

            field.removeClass("is-valid is-invalid");
            field.val("");
        }
    }

    /**
     * 
     * @param {jQuery} field 
     * @returns boolean
     */
    static validateWord(field)
    {
        return this.validateField(field, /^[A-Za-z]+$/);
    }

    /**
     * 
     * @param {jQuery} field 
     * @returns boolean
     */
    static validateWords(field)
    {
        return this.validateField(field, /^[A-Za-z]+( [A-Za-z]+)*$/);
    }

    /**
     * 
     * @param {jQuery} field 
     * @param {Number} length 
     * @param {Number} maxLength
     * @returns boolean
     */
    static validateNumber(field, length, maxLength)
    {
        if(length == null)
            return this.validateField(field, /^\d+$/);
        else if(maxLength == null)
            return this.validateField(field, new RegExp(`^\\d{${length}}\$`));
        else
            return this.validateField(field, new RegExp(`^\\d{${length},${maxLength}}\$`));
    }

    /**
     * 
     * @param {jQuery} field 
     * @returns boolean
     */
    static validateDate(field)
    {
        return this.validateField(field, /^[12]\d{3}-([1-9]|0[1-9]|1[0-2])-([1-9]|0[1-9]|[12]\d|3[01])$/);
    }

    /**
     * 
     * @param {jQuery} field 
     * @returns boolean
     */
    static validateEmail(field)
    {
        return this.validateField(field, /^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$/);
    }

    /**
     * 
     * @param {jQuery} field 
     * @returns boolean
     */
    static validatePassword(field)
    {
        return this.validateField(field, /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*\p{P})[A-Za-z\d\p{P}]{8,}$/u);
    }

    /**
     * 
     * @param {jQuery} field 
     * @returns boolean
     */
    static validateFilled(field)
    {
        return this.validateField(field, /^.+$/m);
    }

    /**
     * 
     * @param {jQuery} field 
     * @returns boolean
     */
    static validateWordAllChars(field)
    {
        return this.validateField(field, /^\S+$/);
    }

    /**
     * 
     * @param {jQuery} field 
     * @returns boolean
     */
    static validateFloat(field)
    {
        return this.validateField(field, /^\d+[.]\d+$/);
    }

    /**
     * 
     * @param {jQuery} button 
     * @param {String} text 
     */
    static loadingButton(button, text)
    {
        if(!(button instanceof jQuery))
            throw new TypeError();

        button.prop("disabled", true);
        button.html(`<span class='spinner-border spinner-border-sm' role='status' aria-hidden='true'></span> ${text}`);
    }

    /**
     * 
     * @param {jQuery} button 
     * @param {String} text 
     */
    static resetLoadingButton(button, text)
    {
        if(!(button instanceof jQuery))
            throw new TypeError();

        button.prop("disabled", false);
        button.html(text);
    }

    /**
     * 
     * @param  {...jQuery} fields 
     * @returns Object
     */
    static setParameters(...fields)
    {
        let object = new Object();

        for(const field of fields)
        {
            if(!(field instanceof jQuery))
                throw new TypeError();

            object[field.attr("name")] = field.val();
        }
        return object;
    }
}