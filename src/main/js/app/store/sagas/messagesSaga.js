import { takeEvery } from 'redux-saga/effects';
import { ADD_MESSAGE } from 'constants';

const messagesWatcher = function*({ user, socket }) {
    yield takeEvery(ADD_MESSAGE, action => {
        action.author = user;
        socket.send(JSON.stringify(action));
    });
};

export default messagesWatcher;
