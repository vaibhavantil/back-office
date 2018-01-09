import initialState from '../initialState';
import { POLL_START, POLL_STOP } from 'constants';

export default function(state = initialState.poll, action) {
    switch (action.type) {
        case POLL_START:
            return {
                ...state,
                polling: true
            };
        case POLL_STOP:
            return {
                ...state,
                polling: false
            };
        default:
            return state;
    }
}
