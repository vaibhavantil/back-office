import React from 'react';
import { Form, Dropdown, Input } from 'semantic-ui-react';
import DateInput from '../dateinput/DateInput';

const stateOptions = [{ key: 'AL', value: 'AL', text: 'Alabama' }];

export default {
    Text() {
        return <Form.Input />;
    },

    Date(onChangeHandler) {
        return <DateInput onChangeHandler={onChangeHandler} />;
    },

    MultipleSelect() {
        return (
            <Dropdown
                placeholder="State"
                multiple
                search
                selection
                options={stateOptions}
            />
        );
    },

    Audio() {
        return <Input type="file" placeholer="Audio" />;
    }
};
