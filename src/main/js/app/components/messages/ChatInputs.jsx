import React from 'react';
import { Form, Dropdown } from 'semantic-ui-react';
import DateInput from '../inputs/DateInput';
import FileInput from '../inputs/FileInput';

// todo get options through props 
const stateOptions = [
    { key: 'AL', value: 'AL', text: 'Al' },
    { key: 'EL', value: 'EL', text: 'El' }
];

export default {
    text(onChangeHandler, value) {
        const changeHandler = (e, { value }) => onChangeHandler(value);
        return <Form.Input onChange={changeHandler} value={value} />;
    },

    Date(onChangeHandler) {
        return <DateInput onChangeHandler={onChangeHandler} />;
    },

    MultipleSelect(onChangeHandler) {
        const changeHandler = (e, { value }) => onChangeHandler(value);
        return (
            <Dropdown
                placeholder="State"
                multiple
                search
                selection
                options={stateOptions}
                onChange={changeHandler}
            />
        );
    },

    File(onChangeHandler) {
        return <FileInput changeHandler={onChangeHandler} />;
    }
};
