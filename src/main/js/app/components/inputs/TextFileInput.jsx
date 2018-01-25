import React from 'react';
import FileInput from './FileInput';
import TextInput from './TextInput';

const TextFileInput = ({ changeHandler, type, cleanupForm }) => (
    <React.Fragment>
        <TextInput changeHandler={changeHandler} cleanupForm={cleanupForm} />
        <FileInput
            changeHandler={changeHandler}
            type={type}
            cleanupForm={cleanupForm}
        />
    </React.Fragment>
);

export default TextFileInput;
