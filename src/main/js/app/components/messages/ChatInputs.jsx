import React from 'react';
import { Form, Dropdown } from 'semantic-ui-react';
import DateInput from '../inputs/DateInput';
import FileInput from '../inputs/FileInput';

const stateOptions = [
    { key: 'AL', value: 'AL', text: 'Al' },
    { key: 'EL', value: 'EL', text: 'El' }
];

export default {
    text(onChangeHandler) {
        const changeHandler = (e, { value }) => onChangeHandler(value);
        return <Form.Input onChange={changeHandler} />;
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
