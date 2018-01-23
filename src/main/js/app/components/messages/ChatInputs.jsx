import React from 'react';
import { Input, Form } from 'semantic-ui-react';
import DateInput from '../inputs/DateInput';
import TextFileInput from '../inputs/TextFileInput';
import NumberInput from '../inputs/NumberInput';
import SelectCreator from '../inputs/SelectCreator';

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

    select(onChangeHandler, type) {
        return (
            <SelectCreator changeHandler={onChangeHandler} selectType={type} />
        );
    },

    file(onChangeHandler, type) {
        return <TextFileInput changeHandler={onChangeHandler} type={type} />;
    }
};
