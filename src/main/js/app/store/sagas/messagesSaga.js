import { takeEvery } from 'redux-saga/effects';
import { ADD_MESSAGE } from 'constants';
import config from 'app/api/config';
import { updateMessageBody } from 'app/lib/sockets';

const messagesWatcher = function*() {
    yield takeEvery(ADD_MESSAGE, ({ message, messageType, socket, userId }) => {
        const content = {
            id: "message.hello",
            header: {
                fromId: 1
            },
            body: {
                type: messageType,
                text: message.text
            },
            timestamp: new Date().toISOString()
        };
        const updatedContent = updateMessageBody(content, message, messageType);
        socket.send(config.ws.send + userId, {}, updatedContent);
    });
};

export default messagesWatcher;
