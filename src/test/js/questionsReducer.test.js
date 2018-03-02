import * as types from '../../main/js/app/store/constants/questions';
import reducer from '../../main/js/app/store/reducers/questionsReducer';

describe('questions reducer', () => {
    it('should return the initial state', () => {
        expect(reducer(undefined, {})).toEqual({
            list: {
                answered: [],
                notAnswered: []
            },
            requesting: false,
            successful: false,
            errors: []
        });
    });

    it('should handle requesting questions', () => {
        const state = {
            list: {
                answered: [],
                notAnswered: []
            },
            requesting: false,
            successful: false,
            errors: []
        };
        const result = reducer(state, {
            type: types.QUESTIONS_REQUEST_SUCCESS,
            questions: {
                notAnswered: [
                    {
                        id: 1,
                        date: 1519980139
                    }
                ],
                answered: []
            }
        });
        expect({
            requesting: result.requesting,
            successful: result.successful
        }).toEqual({
            requesting: false,
            successful: true
        });
        expect(result.requesting).toEqual(false);
        expect(result.successful).toEqual(true);
        expect(!!result.list.notAnswered.length).toEqual(true);
    });
});
