import api from 'app/api';
import config from 'app/api/config';
import { history } from 'app/store';
/**
 * Check credentials in app store
 * @param {function} getState
 */
export function checkLocalAuth({ getState }) {
    const state = getState();
    return !!state.client.id;
}

/**
 * Check client credentails in backend
 */
export async function checkApiAuth() {
    try {
        await api(config.login.login);
        return true;
    } catch (error) {
        history.replace('/login/oauth');
        return false;
    }
}
