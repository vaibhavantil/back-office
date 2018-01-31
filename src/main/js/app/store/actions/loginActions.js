import { LOGIN_REQUESTING } from 'constants/login';

export const loginRequest = ({ email, password }) => ({
    type: LOGIN_REQUESTING,
    email,
    password
});
