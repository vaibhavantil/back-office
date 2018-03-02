import * as types from '../../main/js/app/store/constants/assets';
import reducer from '../../main/js/app/store/reducers/assetsReducer';

describe('assets reducer', () => {
    it('should return the initial state', () => {
        expect(reducer(undefined, {})).toEqual({
            list: [],
            requesting: false,
            successful: false,
            errors: []
        });
    });

    it('should handle requesting actions', () => {
        const state = {
            list: [],
            requesting: false,
            successful: false,
            errors: []
        };
        const result = reducer(state, { type: types.ASSET_REQUESTING });
        expect({
            requesting: result.requesting,
            successful: result.successful
        }).toEqual({
            requesting: true,
            successful: false
        });
    });

    it('should handle success fetch', () => {
        const state = {
            list: [],
            requesting: false,
            successful: false,
            errors: []
        };

        const action = {
            type: types.ASSET_REQUEST_SUCCESS,
            assets: [
                {
                    id: 'b0a60474-2eb7-4791-9351-e632b0c38864',
                    state: 'PENDING',
                    title: 'Asset number 1'
                }
            ]
        };
        const result = reducer(state, action);
        expect(result.successful).toEqual(true);
        expect(result.list.length).toEqual(1);
    });

    it('should handle success update', () => {
        const state = {
            list: [
                {
                    id: '4791',
                    state: 'PENDING'
                }
            ],
            requesting: false,
            successful: false,
            errors: []
        };

        const action = {
            type: types.ASSET_UPDATE_SUCCESS,
            asset: {
                id: '4791',
                state: 'CREATED'
            }
        };
        const result = reducer(state, action);
        expect(result.list[0].state).toEqual(action.asset.state);
    });
});
