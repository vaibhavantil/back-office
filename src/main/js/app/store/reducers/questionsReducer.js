import initialState from '../initialState';
import {
    QUESTIONS_REQUESTING,
    QUESTIONS_REQUEST_SUCCESS,
    QUESTION_ANSWERING,
    QUESTION_ANSWER_SUCCESS
} from '../constants/questions';
import { sortQuestions, replaceAnswer } from '../../lib/helpers';

export default function(state = initialState.questions, action) {
    switch (action.type) {
        case QUESTIONS_REQUESTING:
        case QUESTION_ANSWERING:
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

        case QUESTION_ANSWER_SUCCESS:
            return {
                ...state,
                requesting: false,
                successful: true,
                list: replaceAnswer({...state.list}, action.data)
            }
        default:
            return state;
    }
}
