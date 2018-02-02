import React from 'react';
import { List } from 'semantic-ui-react';

const PayoutsList = ({ list }) => (
    <List>
        {list.map((item, id) => (
            <List.Item key={item.id || id}>{JSON.stringify(item)}</List.Item>
        ))}
    </List>
);

export default PayoutsList;
