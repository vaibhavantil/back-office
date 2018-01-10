import React from 'react';
import { connect } from 'react-redux';
import { loginRequest } from 'app/store/actions/loginActions';
import LoginForm from 'components/login-form/LoginForm';

class LoginPage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <main className="login-container">
                <h1>Login</h1>
                <LoginForm
                    onSubmit={this.props.loginRequest}
                    errors={this.props.login.errors}
                />
            </main>
        );
    }
}

const mapStateToProps = ({ login }) => ({
    login
});

export default connect(mapStateToProps, {
    loginRequest
})(LoginPage);
