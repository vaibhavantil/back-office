import initialState from '../initialState';
import {
    MEMBER_INS_REQUESTING,
    MEMBER_INS_SUCCESS,
    MEMBER_INS_ERROR,
    MEMBER_INS_SEARCH_REQUESTING,
    MEMBER_INS_SEARCH_SUCCESS
} from '../constants/memberInsurance';

export default function(state = initialState.memberInsurance, action) {
    switch (action.type) {
        case MEMBER_INS_REQUESTING:
        case MEMBER_INS_SEARCH_REQUESTING:
            return {
                ...state,
                requesting: true
            };

        case MEMBER_INS_SUCCESS:
        case MEMBER_INS_SEARCH_SUCCESS:
            return {
                ...state,
                list: action.members,
                requesting: false
            };

        case MEMBER_INS_ERROR:
            return {
                ...state,
                requesting: false
            };

        default:
            return state;
    }
}
