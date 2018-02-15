import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Form, Button, Segment, Message } from 'semantic-ui-react';
import { Field, reduxForm } from 'redux-form';
import FormField from '../form-field/FormField';
import { required, email } from 'app/lib/validation';

const LoginFormContainer = styled(Segment)`
    display: inline-block;
    min-width: 600px;
`;

const LoginFormErrors = errors =>
    errors.errors.map((err, id) => (
        <Message negative key={id}>
            <p>
                {err.status === 401 ? 'Wrong email or password' : err.message}
            </p>
        </Message>
    ));

const LoginForm = ({ submitting, pristine, handleSubmit, errors }) => (
    <LoginFormContainer>
        <Form name="login" onSubmit={handleSubmit}>
            <Field
                name="email"
                component={FormField}
                as={Form.Input}
                type="text"
                label="Email"
                placeholder="Email"
                validate={[required, email]}
            />
            <Field
                name="password"
                component={FormField}
                as={Form.Input}
                type="password"
                label="Password"
                placeholder="Password"
                validate={required}
            />
            {!!errors.length && <LoginFormErrors errors={errors} />}
            <Button
                loading={submitting}
                disabled={pristine || submitting}
                primary
            >
                Login
            </Button>
        </Form>
    </LoginFormContainer>
);

LoginForm.propTypes = {
    submitting: PropTypes.bool,
    pristine: PropTypes.bool,
    handleSubmit: PropTypes.func.isRequired,
    errors: PropTypes.array
};

export default reduxForm({
    form: 'login'
})(LoginForm);
