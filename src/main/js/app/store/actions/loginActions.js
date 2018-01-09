import { LOGIN_REQUESTING } from 'constants';

export const loginRequest = ({ email, password }) => ({
    type: LOGIN_REQUESTING,
    email,
    password
});
