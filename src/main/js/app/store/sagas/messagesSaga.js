import { takeEvery } from 'redux-saga/effects';
import { ADD_MESSAGE } from 'constants';
import config from 'app/api/config';

const messagesWatcher = function*() {
    yield takeEvery(ADD_MESSAGE, ({ message, socket }) => {
        socket.send(config.ws.messages, {}, JSON.stringify({body: message}));
    });
};

export default messagesWatcher;
