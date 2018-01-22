import { takeEvery } from 'redux-saga/effects';
import { ADD_MESSAGE } from 'constants';
import config from 'app/api/config';

const messagesWatcher = function*() {
    yield takeEvery(ADD_MESSAGE, ({ message, messageType, socket, userId }) => {

        const content = {
            id: "message.hello",
            header: {
                fromId: 1
            },
            body: {
                'type': messageType || 'text',
                'text': message
            },
            timestamp: new Date().toISOString()
        };

        socket.send(config.ws.send + userId, {}, JSON.stringify(content));
    });
};

export default messagesWatcher;
