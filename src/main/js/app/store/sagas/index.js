import LoginSaga from './loginSaga';
import AssetsSaga from './assetsSaga';
import PollingSaga from './pollingSaga';
import ChatUserSaga from './chatUserSaga';
import MessagesSaga from './messagesSaga';

export default function* IndexSaga() {
    yield [LoginSaga(), AssetsSaga(), PollingSaga(), ChatUserSaga(), MessagesSaga()];
}
