import React from 'react';
import { Form, Input, Message } from 'semantic-ui-react';

/* eslint-disable react/prop-types */
const FormField = ({
    input,
    type,
    label,
    placeholder,
    meta: { touched, error, warning },
    as: As = Input,
    ...props
}) => {
    function handleChange(e, { value }) {
        return input.onChange(value);
    }
    return (
        <Form.Field>
            <As
                {...props}
                {...input}
                value={input.value}
                type={type}
                label={label}
                placeholder={placeholder}
                onChange={handleChange}
            />
            {touched &&
                ((error && <Message negative>{error}</Message>) ||
                    (warning && <Message warning>{warning}</Message>))}
        </Form.Field>
    );
};

export default FormField;
