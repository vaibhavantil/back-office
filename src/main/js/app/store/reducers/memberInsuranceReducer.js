import initialState from '../initialState';
import { sortMemberInsList } from 'app/lib/helpers';
import {
    MEMBER_INS_REQUESTING,
    MEMBER_INS_SUCCESS,
    MEMBER_INS_ERROR,
    MEMBER_INS_SEARCH_REQUESTING,
    MEMBER_INS_SEARCH_SUCCESS,
    SET_MEMBER_INS_FILTER,
    SORT_MEMBER_INS_LIST
} from '../constants/memberInsurance';

export default function(state = initialState.memberInsurance, action) {
    switch (action.type) {
        case MEMBER_INS_REQUESTING:
        case MEMBER_INS_SEARCH_REQUESTING:
            return {
                ...state,
                requesting: true,
                query: action.query.query
            };

        case MEMBER_INS_SUCCESS:
        case MEMBER_INS_SEARCH_SUCCESS:
            return {
                ...state,
                list: [...action.members,  {
                    productId: '3012ed4f-32c0-41fa-b69b-81f0e694c764',
                    memberId: '529854270',
                    memberFirstName: 'John',
                    memberLastName: 'Ardelius',
                    safetyIncreasers: ['Brandvarnare', 'Säkerhetsdörr'],
                    insuranceStatus: 'PENDING',
                    insuranceState: 'QUOTE',
                    currentTotalPrice: 479.0,
                    newTotalPrice: null,
                    insuredAtOtherCompany: true,
                    insuranceType: 'BRF',
                    insuranceActiveFrom: null,
                    insuranceActiveTo: null
                },
                {
                    productId: '6fdc037e-9cf2-4268-bb9e-b078d7cfdc9e',
                    memberId: '793060627',
                    memberFirstName: 'Philip',
                    memberLastName: 'Harrison',
                    safetyIncreasers: ['Brandvarnare'],
                    insuranceStatus: 'ACTIVE',
                    insuranceState: 'SIGNED',
                    currentTotalPrice: 389.0,
                    newTotalPrice: null,
                    insuredAtOtherCompany: false,
                    insuranceType: 'SUBLET_RENTAL',
                    insuranceActiveFrom: '2018-02-20T12:16:22.907',
                    insuranceActiveTo: null
                }],
                requesting: false
            };

        case MEMBER_INS_ERROR:
            return {
                ...state,
                requesting: false
            };

        case SET_MEMBER_INS_FILTER:
            return {
                ...state,
                filter: action.query.filter,
                requesting: true
            };

        case SORT_MEMBER_INS_LIST:
            return {
                ...state,
                list: sortMemberInsList(
                    [...state.list],
                    action.fieldName,
                    action.isReverse
                )
            };
        default:
            return state;
    }
}
