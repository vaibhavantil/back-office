import React from 'react';
import styled from 'styled-components';
import { connect } from 'react-redux';
import { loginRequest } from 'app/store/actions/loginActions';
import LoginForm from 'components/login-form/LoginForm';

const LoginContainer = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
`;

const LoginPage = ({ loginRequest, login }) => (
    <LoginContainer>
        <h1>Login</h1>
        <LoginForm onSubmit={loginRequest} errors={login.errors} />
    </LoginContainer>
);
const mapStateToProps = ({ login }) => ({
    login
});

export default connect(mapStateToProps, {
    loginRequest
})(LoginPage);
