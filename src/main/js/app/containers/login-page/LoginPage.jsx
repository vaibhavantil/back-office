import React from 'react';
import styled from 'styled-components';
import GoogleButton from 'react-google-button';
import { Header, Segment } from 'semantic-ui-react';

const LoginContainer = styled(Segment)`
    &&& {
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: flex-start;
        width: 400px;
        margin: 200px auto;

        & a {
            margin: 20px auto;
        }
    }
`;

const LoginPage = () => (
    <LoginContainer>
        <Header size="huge">Login</Header>
        <a href="/api/login/google">
            <GoogleButton label="Login with Google"/>
        </a>
    </LoginContainer>
);

export default LoginPage;
