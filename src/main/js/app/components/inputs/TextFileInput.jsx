import React from 'react';
import { Form, Input } from 'semantic-ui-react';
import FileInput from './FileInput';
import * as types from 'app/lib/messageTypes';

const TextFileInput = ({ changeHandler, type }) => (
    <React.Fragment>
        <Form.Field>
            <label>Text</label>
            <Input fluid onChange={changeHandler.bind(this, types.TEXT)} />
        </Form.Field>
        <FileInput changeHandler={changeHandler} type={type} />
    </React.Fragment>
);

export default TextFileInput;