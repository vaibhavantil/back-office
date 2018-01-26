import { takeEvery } from 'redux-saga/effects';
import { CLEANUP_DASHDBOARD_ITEM } from 'constants';
import config from 'app/api/config';

function* messagesWatcher() {
    yield [
        takeEvery(CLEANUP_DASHDBOARD_ITEM, ({ name, socket, userId }) => {
            socket.send(config.ws.send + userId, {}, { name });
        })
    ];
}

export default messagesWatcher;
