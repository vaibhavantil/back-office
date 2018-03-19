import { setClient } from '../store/actions/clientActions';

export function checkAuthorization(dispatch, setClientAction) {
    /* eslint-disable no-undef */
    const storedToken = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    if (storedToken) {
        const token = JSON.parse(storedToken);
        if (setClientAction) {
            setClientAction(token, user);
            return true;
        }
        dispatch(setClient(token, user));
        return true;
    }
    return false;
}

export function checkAssetAuthorization({ dispatch, getState }) {
    const client = getState().client;
    if (client && client.creditals) return true;
    return checkAuthorization(dispatch);
}

export const getAuthToken = () => {
    const token = localStorage.getItem('token');
    return token ? JSON.parse(token) : '';
};
