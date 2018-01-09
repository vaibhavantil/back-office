import { take, fork, cancel, call, put, cancelled } from 'redux-saga/effects';
import { history } from 'app/app';
import api from 'app/api/LoginApi';

import {
    LOGIN_REQUESTING,
    LOGIN_SUCCESS,
    LOGIN_ERROR,
    CLIENT_UNSET
} from '../constants/actionTypes';

import { setClient, unsetClient } from '../actions/clientActions';

export function* logout() {
    yield put(unsetClient());
    // eslint-disable-next-line no-undef
    localStorage.removeItem('token');
    history.push('/login');
}

function* loginFlow(email, password) {
    let request;
    try {
        request = yield call(api.login, email, password);
        const token = request.data.token;
        yield put(setClient(token));
        yield put({ type: LOGIN_SUCCESS });
        // eslint-disable-next-line no-undef
        localStorage.setItem('token', JSON.stringify(token));
        history.push('/assets');
    } catch (error) {
        yield put({ type: LOGIN_ERROR, error });
    } finally {
        if (yield cancelled()) {
            history.push('/login');
        }
    }

    return request;
}

function* loginWatcher() {
    while (true) {
        const { email, password } = yield take(LOGIN_REQUESTING);
        const task = yield fork(loginFlow, email, password);
        const action = yield take([CLIENT_UNSET, LOGIN_ERROR]);
        if (action.type === CLIENT_UNSET) {
            yield cancel(task);
        }
        yield call(logout);
    }
}

export default loginWatcher;
