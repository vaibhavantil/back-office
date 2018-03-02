import * as types from '../../main/js/app/store/constants/claims';
import reducer from '../../main/js/app/store/reducers/claimsReducer';

describe('Claims reducer', () => {
    it('should return the initial state', () => {
        expect(reducer(undefined, {})).toEqual({
            list: [],
            requesting: false,
            successful: false,
            errors: [],
            types: []
        });
    });

    it('should handle fetch claims', () => {
        const state = {
            list: [],
            requesting: false,
            successful: false,
            errors: [],
            types: []
        };
        const result = reducer(state, {
            type: types.CLAIMS_REQUEST_SUCCESS,
            claims: [
                {
                    date: '2018-03-02T09:39:39.148Z',
                    claimID: 'sd43-34we',
                    userId: '1243df'
                }
            ]
        });
        expect(!!result.list.length).toEqual(true);
    });

    it('should handle fetch claim types', () => {
        const state = {
            list: [],
            requesting: false,
            successful: false,
            errors: [],
            types: []
        };

        const result = reducer(state, {
            type: types.CLAIM_TYPES_SUCCESS,
            types: [
                {
                    name: 'TYPE_NAME',
                    title: 'TYPE_TITLE'
                }
            ]
        });
        const resultFieldsCount = Object.keys(result.types[0]).length;
        expect(result.types[0].value).toEqual('TYPE_NAME');
        expect(resultFieldsCount).toEqual(5);
    });
});
