import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Dropdown, Input, Header } from 'semantic-ui-react';
import UsersList from '../users-list/UsersList';
import BackLink from 'components/shared/link/BackLink';
import { userStatus } from 'app/lib/selectOptions';

const UsersFilter = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    width: 100%;
`;
export default class Users extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchValue: '',
            users: [],
            filteredList: []
        };
    }

    inputChangeHandler = (e, { value }) => {
        this.setState({ searchValue: value });
    };

    keyPressHandler = e => {
        if (e.key === 'Enter') this.searchRequest();
    };

    searchRequest = () => {
        this.props.searchUserRequest(this.state.searchValue);
        this.setState({ searchValue: '' });
    };

    filterChangeHandler = (e, { value }) => {
        this.props.setFilter(value);
    };

    componentDidMount() {
        const { users, usersRequest } = this.props;
        if (!users.list.length) usersRequest();
    }

    render() {
        const {
            users,
            messages,
            newMessagesReceived,
            setActiveConnection,
            client
        } = this.props;
        const { searchValue } = this.state;
        return (
            <React.Fragment>
                <Header size="huge">Members</Header>
                <BackLink />
                <UsersFilter>
                    <Input
                        loading={users.requesting}
                        placeholder="Search..."
                        iconPosition="left"
                        onChange={this.inputChangeHandler}
                        onKeyPress={this.keyPressHandler}
                        action={{ icon: 'search', onClick: this.searchRequest }}
                        value={searchValue}
                    />

                    <label>Status: </label>
                    <Dropdown
                        onChange={this.filterChangeHandler}
                        options={userStatus}
                        selection
                        value={users.filter}
                    />
                </UsersFilter>

                <UsersList
                    users={users.list}
                    messages={messages}
                    newMessagesReceived={newMessagesReceived}
                    setActiveConnection={setActiveConnection}
                    client={client}
                />
            </React.Fragment>
        );
    }
}

Users.propTypes = {
    users: PropTypes.object.isRequired,
    messages: PropTypes.object.isRequired,
    newMessagesReceived: PropTypes.func.isRequired,
    setActiveConnection: PropTypes.func.isRequired,
    client: PropTypes.object.isRequired,
    searchUserRequest: PropTypes.func.isRequired,
    usersRequest: PropTypes.func.isRequired,
    setFilter: PropTypes.func
};
