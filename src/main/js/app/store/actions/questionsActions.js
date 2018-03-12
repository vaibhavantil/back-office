import {
    QUESTIONS_REQUESTING,
    QUESTIONS_REQUEST_SUCCESS,
    QUESTIONS_REQUEST_ERROR,
    QUESTION_ANSWERING,
    QUESTION_ANSWER_SUCCESS
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

export const sendAnswer = data => ({
    type: QUESTION_ANSWERING,
    data
});

export const answerSuccess = data => ({
    type: QUESTION_ANSWER_SUCCESS,
    data
});
