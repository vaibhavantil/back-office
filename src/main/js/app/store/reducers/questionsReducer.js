import initialState from '../initialState';
import {
    QUESTIONS_REQUESTING,
    QUESTIONS_REQUEST_SUCCESS
} from '../constants/questions';
import { sortQuestions } from '../../lib/helpers';

export default function(state = initialState.questions, action) {
    switch (action.type) {
        case QUESTIONS_REQUESTING:
            return {
                ...state,
                requesting: true,
                successful: false,
                errors: []
            };
        case QUESTIONS_REQUEST_SUCCESS:
            return {
                requesting: false,
                successful: true,
                errors: [],
                list: sortQuestions({ ...action.questions })
            };
        default:
            return state;
    }
}
