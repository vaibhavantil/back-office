import React from 'react';
import { Form, Select, Input } from 'semantic-ui-react';
import { TEXT, SINGLE_SELECT } from 'app/lib/messageTypes';

const SingleSelectInput = ({ changeHandler, options }) => (
    <React.Fragment>
        <Form.Field>
            <label>Text</label>
            <Input fluid onChange={changeHandler.bind(this, TEXT)} />
        </Form.Field>
        <Form.Field>
            <label>Select</label>
            <Select
                placeholder="State"
                options={options}
                onChange={changeHandler.bind(this, SINGLE_SELECT)}
            />
        </Form.Field>
    </React.Fragment>
);

export default SingleSelectInput;
