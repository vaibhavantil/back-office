import initialState from '../initialState';
import { CLIENT_SET, CLIENT_UNSET } from 'constants/login';

export default function clientReducer(state = initialState.client, action) {
    switch (action.type) {
        case CLIENT_SET:
            return {
                ...action.creditals
            };
        case CLIENT_UNSET:
            return {};
        default:
            return state;
    }
}
