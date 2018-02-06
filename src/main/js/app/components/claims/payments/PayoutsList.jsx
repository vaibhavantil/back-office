import React from 'react';
import PropTypes from 'prop-types';
import { List } from 'semantic-ui-react';

const PayoutsList = ({ list }) => (
    <React.Fragment>
        <h2>Payouts List:</h2>
        {list.length ? (
            <List>
                {list.map((item, id) => (
                    <List.Item key={item.id || id}>
                        {JSON.stringify(item)}
                    </List.Item>
                ))}
            </List>
        ) : (
            <span>----</span>
        )}
    </React.Fragment>
);

PayoutsList.propTypes = {
    list: PropTypes.array.isRequired
};

export default PayoutsList;
