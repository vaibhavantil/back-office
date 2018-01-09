import { setClient } from '../store/actions/clientActions';

export function checkAuthorization(dispatch, setClientAction) {
    // eslint-disable-next-line no-undef
    const storedToken = localStorage.getItem('token');
    if (storedToken) {
        const token = JSON.parse(storedToken);
        if (setClientAction) {
            setClientAction(token);
            return true;
        } 
        dispatch(setClient(token));
        return true;
    }
    return false;
}

export function checkAssetAuthorization({ dispatch, getState }) {
    const client = getState().client;
    if (client && client.token) return true;
    return checkAuthorization(dispatch);
}
