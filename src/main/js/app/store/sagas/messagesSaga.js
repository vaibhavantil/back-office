import { call, takeEvery, put, takeLatest } from 'redux-saga/effects';
import { USER_REQUESTING, ADD_MESSAGE } from 'constants/chatUsers';
import api from 'app/api';
import config from 'app/api/config';
import {
    userRequestSuccess,
    userRequestError
} from '../actions/messagesActions';
import { showNotification } from '../actions/notificationsActions';

function* userRequestFlow({ userId }) {
    try {
        const user = yield call(api, config.users.get, null, userId);
        yield put(userRequestSuccess(user.data));
    } catch (error) {
        yield [
            put(showNotification({ message: error.message, header: 'Users' })),
            put(userRequestError(error))
        ];
    }
}

function* messagesWatcher() {
    yield [
        takeEvery(ADD_MESSAGE, ({ message, socket, userId }) => {
            socket.send(
                config.ws.send + userId,
                {},
                JSON.stringify({
                    memberId: userId,
                    msg: message
                })
            );
        }),
        takeLatest(USER_REQUESTING, userRequestFlow)
    ];
}

export default messagesWatcher;
