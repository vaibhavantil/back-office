import { call, takeEvery, put, takeLatest } from 'redux-saga/effects';
import { USER_REQUESTING, ADD_MESSAGE } from 'constants/chatUsers';
import { updateMessageBody } from 'app/lib/sockets/chat';
import api from 'app/api';
import config from 'app/api/config';
import {
    userRequestSuccess,
    userRequestError
} from '../actions/messagesActions';

function* userRequestFlow({ userId }) {
    try {
        const user = yield call(api, config.users.get, null, userId);
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
