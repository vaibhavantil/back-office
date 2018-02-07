import React from 'react';
import { Dropdown } from 'semantic-ui-react';
import DateInput from 'components/shared/inputs/DateInput';
import TextInput from 'components/shared/inputs/TextInput';
import * as types from 'app/lib/claimFieldsTypes';

const boolOptions = [
    { key: 1, value: 'YES', text: 'Yes' },
    { key: 2, value: 'NO', text: 'No' }
];
export default {
    [types.TEXT](onChangeHandler, cleanupForm) {
        return (
            <TextInput
                changeHandler={onChangeHandler}
                cleanupForm={cleanupForm}
            />
        );
    },

    [types.DATE](onChangeHandler, cleanupForm) {
        return (
            <DateInput
                changeHandler={onChangeHandler}
                cleanupForm={cleanupForm}
            />
        );
    },

    [types.BOOL](onChangeHandler) {
        return (
            <Dropdown
                onChange={onChangeHandler.bind(this, types.BOOL)}
                options={boolOptions}
                selection
            />
        );
    },

    [types.ASSET](onChangeHandler) {
        return <TextInput changeHandler={onChangeHandler} />;
    }
};
