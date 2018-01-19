import React from 'react';
import { Input, Form } from 'semantic-ui-react';
import DateInput from '../inputs/DateInput';
import FileInput from '../inputs/FileInput';
import TextFileInput from '../inputs/TextFileInput';
import NumberInput from '../inputs/NumberInput';
import SingleSelect from '../inputs/SingleSelectInput';
import MultipleSelect from '../inputs/MultipleSelectInput';

import * as types from 'app/lib/messageTypes';

/**
 * returns input by message typename from boot service
 */
export default {
    [types.TEXT](onChangeHandler) {
        return (
            <Form.Field>
                <label>Text</label>
                <Input
                    fluid
                    onChange={onChangeHandler.bind(this, types.TEXT)}
                />
            </Form.Field>
        );
    },

    [types.NUMBER](onChangeHandler) {
        return <NumberInput changeHandler={onChangeHandler} />;
    },

    [types.DATE](onChangeHandler) {
        return <DateInput changeHandler={onChangeHandler} />;
    },

    [types.MULTIPLE_SELECT](onChangeHandler, options) {
        return (
            <MultipleSelect options={options} changeHandler={onChangeHandler} />
        );
    },

    [types.SINGLE_SELECT](onChangeHandler, options) {
        return (
            <SingleSelect changeHandler={onChangeHandler} options={options} />
        );
    },

    file(onChangeHandler, type) {
        return <TextFileInput changeHandler={onChangeHandler} type={type} />;
    },

    [types.BANK_ID_COLLECT](onChangeHandler) {
        return <FileInput changeHandler={onChangeHandler} />;
    }
};
