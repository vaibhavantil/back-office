import React from 'react';
import PropTypes from 'prop-types';
import { Form, Input, Message } from 'semantic-ui-react';

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

FormField.propTypes = {
    input: PropTypes.object.isRequired,
    type: PropTypes.string,
    label: PropTypes.string,
    placeholder: PropTypes.string,
    meta: PropTypes.object.isRequired,
    as: PropTypes.element.isRequired
};

export default FormField;
