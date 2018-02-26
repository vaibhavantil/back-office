import {
    QUESTIONS_REQUESTING,
    QUESTIONS_REQUEST_SUCCESS,
    QUESTIONS_REQUEST_ERROR
} from '../constants/questions';

export const questionsRequest = () => ({
    type: QUESTIONS_REQUESTING
});

export const questionsReqSuccess = questions => ({
    type: QUESTIONS_REQUEST_SUCCESS,
    questions
});

export const questionsReqError = error => ({
    type: QUESTIONS_REQUEST_ERROR,
    error
});
