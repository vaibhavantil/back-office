import React from 'react';
import { Input, Dropdown, TextArea, Select } from 'semantic-ui-react';
import DateInput from '../inputs/DateInput';
import FileInput from '../inputs/FileInput';

// todo get options through props
const stateOptions = [
    { key: 'AL', value: 'AL', text: 'Al' },
    { key: 'EL', value: 'EL', text: 'El' }
];

/**
 * returns input by message type from boot service
 */
export default {
    text(onChangeHandler, value) {
        const changeHandler = (e, { value }) => onChangeHandler(value);
        return <Input onChange={changeHandler} value={value} />;
    },

    number(onChangeHandler, value) {
        const changeHandler = (e, { value }) => onChangeHandler(value);
        return <Input type="number" onChange={changeHandler} value={value} />;
    },

    date_picker(onChangeHandler) {
        return <DateInput onChangeHandler={onChangeHandler} />;
    },

    multiple_select(onChangeHandler) {
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

    single_select(onChangeHandler) {
        const changeHandler = (e, { value }) => onChangeHandler(value);
        return (
            <Select
                placeholder="State"
                options={stateOptions}
                onChange={changeHandler}
            />
        );
    },

    paragraph(onChangeHandler, value) {
        const changeHandler = (e, { value }) => onChangeHandler(value);
        return (
            <TextArea type="number" onChange={changeHandler} value={value} />
        );
    },

    audio(onChangeHandler) {
        return <FileInput changeHandler={onChangeHandler} />;
    },

    video(onChangeHandler) {
        return <FileInput changeHandler={onChangeHandler} />;
    },

    photo_upload(onChangeHandler) {
        return <FileInput changeHandler={onChangeHandler} />;
    }
};
