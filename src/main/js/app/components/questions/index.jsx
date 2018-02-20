import React from 'react';
import PropTypes from 'prop-types';
import { List, Header } from 'semantic-ui-react';
import BackLink from 'components/shared/link/BackLink';

export default class Questions extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.props.usersRequest();
    }

    render() {
        return (
            <React.Fragment>
                <Header size="huge">Questions</Header>
                <BackLink />
                <List>
                    {this.props.users.list.map((item, id) => (
                        <List.Item key={id}>{JSON.stringify(item)}</List.Item>
                    ))}
                </List>
                <div>{JSON.stringify(this.props.users.list)}</div>
            </React.Fragment>
        );
    }
}

Questions.propTypes = {
    users: PropTypes.object.isRequired,
    usersRequest: PropTypes.func.isRequired
};
