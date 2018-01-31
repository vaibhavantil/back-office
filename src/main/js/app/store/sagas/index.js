import LoginSaga from './loginSaga';
import AssetsSaga from './assetsSaga';
import PollingSaga from './pollingSaga';
import ChatUserSaga from './chatUserSaga';
import MessagesSaga from './messagesSaga';
import DashboardSaga from './dashboardSaga';
import ClaimsSaga from './claimsSaga';
import ClaimDetailsSaga from './claimDetailsSaga';

export default function* IndexSaga() {
    yield [
        LoginSaga(),
        AssetsSaga(),
        PollingSaga(),
        ChatUserSaga(),
        MessagesSaga(),
        DashboardSaga(),
        ClaimsSaga(),
        ClaimDetailsSaga()
    ];
}
