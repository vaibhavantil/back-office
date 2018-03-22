import initialState from '../initialState';
import {
    QUESTIONS_REQUESTING,
    QUESTIONS_REQUEST_SUCCESS,
    QUESTION_ANSWERING,
    QUESTION_ANSWER_SUCCESS,
    QUESTION_ANSWER_ERROR
} from '../constants/questions';
import {
    sortQuestions,
    replaceAnswer,
    setAnswerError
} from '../../lib/helpers';

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
                list: replaceAnswer({ ...state.list }, action.data)
            };

        case QUESTION_ANSWER_ERROR:
            return {
                ...state,
                list: {
                    answered: state.list.answered,
                    notAnswered: setAnswerError(
                        state.list.notAnswered,
                        action.error
                    )
                },
                errors: [...state.errors, action.error],
                requesting: false,
                successful: false
            };
        default:
            return state;
    }
}
