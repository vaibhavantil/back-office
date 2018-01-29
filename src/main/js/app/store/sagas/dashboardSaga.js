import { takeEvery } from 'redux-saga/effects';
import { CLEANUP_DASHDBOARD_ITEM } from 'constants';
import config from 'app/api/config';

function* messagesWatcher() {
    yield [
        takeEvery(CLEANUP_DASHDBOARD_ITEM, ({ name, socket }) => {
            socket.send(`${config.ws.cleanupDashboard}${name}`, {}, '');
        })
    ];
}

export default messagesWatcher;
