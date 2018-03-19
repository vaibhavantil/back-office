import React from 'react';
import styled from 'styled-components';

const LoginContainer = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
`;

const LoginPage = () => (
    <LoginContainer>
        <h1><a href="/api/login/google">Login via Google</a></h1>
    </LoginContainer>
);

export default LoginPage;
