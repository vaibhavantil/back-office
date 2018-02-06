import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { connect } from 'react-redux';
import { loginRequest } from 'app/store/actions/loginActions';
import LoginForm from 'components/login/login-form/LoginForm';

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

LoginPage.propTypes = {
    loginRequest: PropTypes.func.isRequired,
    login: PropTypes.object.isRequired
};

export default connect(
    ({ login }) => ({
        login
    }),
    {
        loginRequest
    }
)(LoginPage);
