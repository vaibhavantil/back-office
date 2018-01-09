import LoginSaga from './loginSaga';
import AssetsSaga from './assetsSaga';
import PollingSaga from './pollingSaga';

export default function* IndexSaga() {
    yield [LoginSaga(), AssetsSaga(), PollingSaga()];
}
