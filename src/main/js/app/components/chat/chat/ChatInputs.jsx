import React from 'react';
import DateInput from '../inputs/DateInput';
import TextFileInput from '../inputs/TextFileInput';
import NumberInput from '../inputs/NumberInput';
import SelectCreator from '../inputs/SelectCreator';
import TextInput from '../inputs/TextInput';
import * as types from 'app/lib/messageTypes';

/**
 * returns input by message typename from boot service
 */
export default {
    [types.TEXT](onChangeHandler, cleanupForm) {
        return (
            <TextInput
                changeHandler={onChangeHandler}
                cleanupForm={cleanupForm}
            />
        );
    },

    [types.NUMBER](onChangeHandler) {
        return <NumberInput changeHandler={onChangeHandler} />;
    },

    [types.DATE](onChangeHandler, cleanupForm) {
        return (
            <DateInput
                changeHandler={onChangeHandler}
                cleanupForm={cleanupForm}
            />
        );
    },

    select(onChangeHandler, type, cleanupForm) {
        return (
            <SelectCreator
                changeHandler={onChangeHandler}
                selectType={type}
                cleanupForm={cleanupForm}
            />
        );
    },

    file(onChangeHandler, type, cleanupForm) {
        return (
            <TextFileInput
                changeHandler={onChangeHandler}
                type={type}
                cleanupForm={cleanupForm}
            />
        );
    }
};
