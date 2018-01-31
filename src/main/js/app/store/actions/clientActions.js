import { CLIENT_SET, CLIENT_UNSET } from 'constants/login';

export const setClient = (token, user) => ({
    type: CLIENT_SET,
    token,
    user
});

export const unsetClient = () => ({
    type: CLIENT_UNSET
});
