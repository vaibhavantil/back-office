import React from 'react';
import { Form, Button, Segment, Message } from 'semantic-ui-react';
import { Field, reduxForm } from 'redux-form';
import FormField from '../form-field/FormField';
import { required, email } from 'app/lib/validation';
import { Link } from 'react-router-dom';

/* eslint-disable react/prop-types */
class LoginForm extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const { submitting, pristine, handleSubmit, errors } = this.props;
        return (
            <Segment className="login-form">
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

                    <Link to={{ pathname: '/messages/21'}}>go</Link>
                </Form>
            </Segment>
        );
    }
}

const LoginFormErrors = errors => {
    return errors.errors.map((err, id) => (
        <Message negative key={id}>
            <p>
                {err.status === 401 ? 'Wrong email or password' : err.message}
            </p>
        </Message>
    ));
};

export default reduxForm({
    form: 'login'
})(LoginForm);
