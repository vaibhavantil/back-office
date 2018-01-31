import { call, takeEvery, put, takeLatest } from 'redux-saga/effects';
import { USER_REQUESTING, ADD_MESSAGE } from 'constants';
import { updateMessageBody } from 'app/lib/sockets/chatSockets';
import api from 'app/api';
import config from 'app/api/config';
import {
    userRequestSuccess,
    userRequestError
} from '../actions/messagesActions';

function* userRequestFlow(action) {
    try {
        const { token, userId } = action;
        const user = yield call(api, token, config.users.get, null, userId);
        yield put(userRequestSuccess(user.data));
    } catch (error) {
        yield put(userRequestError(error));
    }
}

function* messagesWatcher() {
    yield [
        takeEvery(ADD_MESSAGE, ({ message, messageType, socket, userId }) => {
            const content = {
                id: 'message.hello',
                header: {
                    fromId: 1
                },
                body: {
                    type: messageType,
                    text: message.text
                },
                timestamp: new Date().toISOString()
            };
            const updatedContent = updateMessageBody(
                content,
                message,
                messageType
            );
            socket.send(config.ws.send + userId, {}, updatedContent);
        }),
        takeLatest(USER_REQUESTING, userRequestFlow)
    ];
}

export default messagesWatcher;
